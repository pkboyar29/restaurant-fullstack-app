package com.example.backend.services;

import com.example.backend.dto.TakeawayOrder.TakeawayOrderPositionRequestDTO;
import com.example.backend.dto.TakeawayOrder.TakeawayOrderRequestDTO;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.exceptions.UserRoleException;
import com.example.backend.models.*;
import com.example.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TakeawayOrderService {
    private final TakeawayOrderRepository takeawayOrderRepository;
    private final TakeawayOrderPositionRepository takeawayOrderPositionRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final MenuPositionRepository menuPositionRepository;
    private final OrderDiscountRepository orderDiscountRepository;

    @Autowired
    public TakeawayOrderService(TakeawayOrderRepository takeawayOrderRepository, TakeawayOrderPositionRepository takeawayOrderPositionRepository,
                                ClientRepository clientRepository, UserRepository userRepository, MenuPositionRepository menuPositionRepository,
                                OrderDiscountRepository orderDiscountRepository) {
        this.takeawayOrderRepository = takeawayOrderRepository;
        this.takeawayOrderPositionRepository = takeawayOrderPositionRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.menuPositionRepository = menuPositionRepository;
        this.orderDiscountRepository = orderDiscountRepository;
    }

    public void addTakeawayOrder(String username, TakeawayOrderRequestDTO takeawayOrderRequestDTO) {
        try {
            TakeawayOrder newTakeawayOrder = new TakeawayOrder();
            int clientDiscountValue = 0;

            if (username != null) {
                Optional<User> optionalUser = userRepository.findByUsername(username);
                if (optionalUser.isEmpty()) { throw new ObjectNotFoundException("User with this username doesn't exist"); }
                User user = optionalUser.get();

                if (!user.getRole().getName().equals("client")) { throw new UserRoleException("Permission denied"); }

                newTakeawayOrder.setUser(user);

                Optional<Client> optionalClient = clientRepository.findByUser(user);
                if (optionalClient.isEmpty()) { throw new ObjectNotFoundException("Client with this user_id doesn't exist"); }
                Client client = optionalClient.get();
                client.setNumberOrders(client.getNumberOrders() + 1);

                clientDiscountValue = client.getOrderDiscount().getDiscount();
                System.out.println("current client discount" + clientDiscountValue);

                // change client order discount if necessary
                for (OrderDiscount orderDiscount: orderDiscountRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))) {
                    if (client.getNumberOrders() >= orderDiscount.getRequiredNumberOrders() && client.getOrderDiscount() != orderDiscount) {
                        client.setOrderDiscount(orderDiscount);
                    }
                }

            } else {
                newTakeawayOrder.setUser(null);
            }
            newTakeawayOrder.setClientName(takeawayOrderRequestDTO.getClientName());
            newTakeawayOrder.setClientPhone(takeawayOrderRequestDTO.getClientPhone());
            newTakeawayOrder.setRequirements(takeawayOrderRequestDTO.getRequirements());
            newTakeawayOrder.setOrderDate(LocalDateTime.now());
            newTakeawayOrder.setReceiptDate(takeawayOrderRequestDTO.getReceiptDate());
            newTakeawayOrder.setReceiptOption(takeawayOrderRequestDTO.getReceiptOption());
            newTakeawayOrder.setPaymentMethod(takeawayOrderRequestDTO.getPaymentMethod());

            newTakeawayOrder = takeawayOrderRepository.save(newTakeawayOrder);

            double cost = 0;
            double discountedCost = 0;

            // создать сущности TakeawayOrderPosition
            for (TakeawayOrderPositionRequestDTO takeawayOrderPositionRequestDTO : takeawayOrderRequestDTO.getTakeawayOrderPositionList()) {
                Optional<MenuPosition> optionalMenuPosition = menuPositionRepository.findById(takeawayOrderPositionRequestDTO.getMenuPositionId());
                if (optionalMenuPosition.isEmpty()) { throw new ObjectNotFoundException("Menu position with this id doesn't exist"); }
                MenuPosition menuPosition = optionalMenuPosition.get();
                int menuPositionPrice = menuPosition.getPrice();

                TakeawayOrderPosition newTakeawayOrderPosition = new TakeawayOrderPosition();
                newTakeawayOrderPosition.setMenuPosition(optionalMenuPosition.get());
                newTakeawayOrderPosition.setNumber(takeawayOrderPositionRequestDTO.getNumber());
                newTakeawayOrderPosition.setTotalPrice(menuPositionPrice * takeawayOrderPositionRequestDTO.getNumber());
                newTakeawayOrderPosition.setTakeawayOrder(newTakeawayOrder);
                takeawayOrderPositionRepository.save(newTakeawayOrderPosition);

                cost += newTakeawayOrderPosition.getTotalPrice();
            }
            if (clientDiscountValue == 0) {
                discountedCost = cost;
            } else {
                discountedCost = cost  * (1 - (clientDiscountValue / 100.0));
            }
            newTakeawayOrder.setCost((int) cost);
            newTakeawayOrder.setDiscountedCost((int) discountedCost);
            takeawayOrderRepository.save(newTakeawayOrder);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
