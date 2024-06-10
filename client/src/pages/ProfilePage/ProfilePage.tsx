import styles from './ProfilePage.module.scss'
import { useEffect, useState } from 'react'
import axios from 'axios'
import { useForm } from 'react-hook-form'
import validator from 'email-validator'
import Cookies from 'js-cookie'

import razvernut from '../../assets/развернуть.png'

import { Client } from '../../ts/types/Client'
import { OrderDiscount } from '../../ts/types/OrderDiscount'
import { TakeawayOrder } from '../../ts/types/TakeawayOrder'
import { TakeawayOrderPosition } from '../../ts/types/TakeawayOrderPosition'

import TextInput from '../../components/TextInput/TextInput'
import Title from '../../components/Title/Title'
import Button from '../../components/Button/Button'
import Modal from '../../components/Modal/Modal'

type ClientUpdateContactFields = {
   firstName: string,
   lastName: string,
   patronymic: string,
   phone: string,
   email: string
}

interface ProfilePageProps {
   currentClient: Client | null,
   setCurrentClient: (currentClient: Client) => void
}

function ProfilePage({ currentClient, setCurrentClient }: ProfilePageProps) {
   const [orderDiscounts, setOrderDiscounts] = useState<OrderDiscount[]>([])
   const [modal, setModal] = useState<boolean>(false)
   const [takeawayOrders, setTakeawayOrders] = useState<TakeawayOrder[]>([])
   const [isFormVisible, setFormVisible] = useState(false);

   const toggleFormVisibility = () => {
      setFormVisible(!isFormVisible);
   };

   const { register, handleSubmit, formState: { errors } } = useForm<ClientUpdateContactFields>({
      mode: 'onBlur',
      defaultValues: {
         firstName: currentClient?.firstName,
         lastName: currentClient?.lastName,
         patronymic: currentClient?.patronymic,
         phone: currentClient?.phone,
         email: currentClient?.email
      }
   })

   const onlyLettersRegex = /^[A-Za-zА-Яа-яЁё]+$/
   const phoneRegex = /^\+?\d{1,3}?[-.\s]?\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{4}$/

   useEffect(() => {
      axios.get(import.meta.env.VITE_BACKEND_URL + '/api/order-discounts')
         .then(response => {
            console.log(response.data)
            setOrderDiscounts(response.data)
         })
   }, [])

   useEffect(() => {
      axios.get(import.meta.env.VITE_BACKEND_URL + '/api/takeaway-orders', {
         headers: {
            Authorization: `Bearer ${Cookies.get('clientToken')}`
         }
      })
         .then(response => {
            console.log(response.data)

            const takeawayOrders: TakeawayOrder[] = response.data.map((orderFromResponse: any) => {
               const takeawayOrderPositions: TakeawayOrderPosition[] = orderFromResponse.takeawayOrderPositions.map((position: any) => {
                  return {
                     id: position.id,
                     menuPositionName: position.menuPosition.name,
                     number: position.number,
                     totalPrice: position.totalPrice
                  };
               });

               return {
                  id: orderFromResponse.id,
                  discountedCost: orderFromResponse.discountedCost,
                  paymentMethod: orderFromResponse.paymentMethod,
                  orderDate: orderFromResponse.orderDate,
                  receiptDate: orderFromResponse.receiptDate,
                  receiptOption: orderFromResponse.receiptOption,
                  takeawayOrderPositions: takeawayOrderPositions
               }
            })
            setTakeawayOrders(takeawayOrders)
         })
   }, [])

   const onSubmit = (data: ClientUpdateContactFields) => {
      const requestBody = {
         firstName: data.firstName,
         lastName: data.lastName,
         patronymic: data.patronymic,
         phone: data.phone,
         email: data.email
      }

      axios.patch(import.meta.env.VITE_BACKEND_URL + '/api/users/update-client-contact', requestBody, {
         headers: {
            'Authorization': `Bearer ${Cookies.get('clientToken')}`
         }
      })
         .then(response => {
            const client: Client = response.data
            setCurrentClient(client)
            setModal(true)
         })
         .catch(error => console.log(error))
   }

   return (
      <div className={`${styles['profile__container']} ${styles['container']}`}>

         {modal && <Modal modalText='Ваши контактные данные успешно обновлены' buttonConfirmText='Ок' confirmHandler={() => {
            setModal(false)
         }} />}

         <Title>
            Просмотр профиля
         </Title>

         <div className={styles['profile__client-discount']}>Ваша текущая скидка: <span className={styles['profile__red']}>{currentClient?.orderDiscount.discount}%</span></div>
         {orderDiscounts.map((orderDiscount: OrderDiscount) => (
            <div className={styles['profile__order-discount']} key={orderDiscount.id}>При &gt;{orderDiscount.requiredNumberOrders} заказах скидка <span className={styles['profile__red']}>{orderDiscount.discount}%</span></div>
         ))}
         <div className={styles['profile__client-discount']}>Вы сделали заказов: <span className={styles['profile__red']}>{currentClient?.numberOrders}</span></div>

         <div className={`${styles['mini__title']} ${styles['profile__client-discount']}`}>
            Изменение контактных данных
            <button type="button" onClick={toggleFormVisibility} className={styles['toggle-button']}>
               {isFormVisible ? <img src={razvernut} className={`${styles['icon']} ${styles['icon__transform']}`} alt="Свернуть" /> : <img className={`${styles['icon']}`} src={razvernut} alt="Развернуть" />}
            </button>
         </div>

         {isFormVisible &&
            <form className={styles['profile__form']} autoComplete='off' onSubmit={handleSubmit(onSubmit)}>

               <TextInput
                  inputTitle='Изменить имя'
                  placeholder='Введите новое имя'
                  errorMessage={typeof errors.firstName?.message === 'string' ? errors.firstName.message : ''}
                  inputProps={{
                     type: 'text',
                     autoComplete: 'off',
                     maxLength: 30,
                     style: { maxWidth: '700px' }
                  }}
                  fieldName='firstName'
                  register={register}
                  validationRules={{
                     required: 'Поле обязательно к заполнению',
                     minLength: {
                        value: 2,
                        message: 'Минимальное количество символов: 2'
                     },
                     maxLength: {
                        value: 30,
                        message: 'Максимальное количество символов: 30'
                     },
                     pattern: {
                        value: onlyLettersRegex,
                        message: 'Допускаются только буквы'
                     }
                  }} />

               <TextInput
                  inputTitle='Изменить фамилию'
                  placeholder='Введите новую фамилию'
                  errorMessage={typeof errors.lastName?.message === 'string' ? errors.lastName.message : ''}
                  inputProps={{
                     type: 'text',
                     autoComplete: 'off',
                     maxLength: 30,
                     style: { maxWidth: '700px' }
                  }}
                  fieldName='lastName'
                  register={register}
                  validationRules={{
                     required: 'Поле обязательно к заполнению',
                     minLength: {
                        value: 2,
                        message: 'Минимальное количество символов: 2'
                     },
                     maxLength: {
                        value: 30,
                        message: 'Максимальное количество символов: 30'
                     },
                     pattern: {
                        value: onlyLettersRegex,
                        message: 'Допускаются только буквы'
                     }
                  }} />

               <TextInput
                  inputTitle='Изменить отчество'
                  placeholder='Введите новое отчество'
                  errorMessage={typeof errors.patronymic?.message === 'string' ? errors.patronymic.message : ''}
                  inputProps={{
                     type: 'text',
                     autoComplete: 'off',
                     maxLength: 30,
                     style: { maxWidth: '700px' }
                  }}
                  fieldName='patronymic'
                  register={register}
                  validationRules={{
                     required: 'Поле обязательно к заполнению',
                     minLength: {
                        value: 2,
                        message: 'Минимальное количество символов: 2'
                     },
                     maxLength: {
                        value: 30,
                        message: 'Максимальное количество символов: 30'
                     },
                     pattern: {
                        value: onlyLettersRegex,
                        message: 'Допускаются только буквы'
                     }
                  }} />

               <TextInput
                  inputTitle='Изменить телефон'
                  placeholder='Введите новый телефон'
                  errorMessage={typeof errors.phone?.message === 'string' ? errors.phone.message : ''}
                  inputProps={{
                     type: 'text',
                     autoComplete: 'off',
                     style: { maxWidth: '700px' }
                  }}
                  fieldName='phone'
                  register={register}
                  validationRules={{
                     required: 'Поле обязательно к заполнению',
                     pattern: {
                        value: phoneRegex,
                        message: 'Некорректный формат телефона'
                     }
                  }} />

               <TextInput
                  inputTitle='Изменить электронную почту'
                  placeholder='Введите новую электронную почту'
                  errorMessage={typeof errors.email?.message === 'string' ? errors.email.message : ''}
                  inputProps={{
                     type: 'text',
                     autoComplete: 'off',
                     maxLength: 60,
                     style: { maxWidth: '700px' }
                  }}
                  fieldName='email'
                  register={register}
                  validationRules={{
                     required: 'Поле обязательно к заполнению',
                     maxLength: {
                        value: 60,
                        message: 'Максимальное количество символов: 60'
                     },
                     validate: (value: string) => validator.validate(value) || 'Неверный формат email'
                  }} />

               <Button buttonProps={{ style: { maxWidth: '700px' } }} text='Сохранить изменения' onClick={() => console.log('hello')} />
            </form>
         }

         <div className={`${styles['mini__title']} ${styles['profile__client-discount']}`}>Созданные заказы</div>

         <div className={styles['orders']}>
            {takeawayOrders.map((order: TakeawayOrder) =>
               <div key={order.id} className={styles['order']}>
                  <div className={`${styles['order__item']} ${styles['order__title']}`}>Заказ от {new Date(order.orderDate).toLocaleString()}</div>
                  <div className={styles['order__item']}>Стоимость: <span className={styles['red']}>{order.discountedCost}</span></div>
                  <div className={styles['order__item']}>Способ оплаты: <span className={styles['red']}>{order.paymentMethod}</span></div>
                  <div className={styles['order__item']}>Дата получения: <span className={styles['red']}>{new Date(order.receiptDate).toLocaleString()}</span></div>
                  <div className={styles['order__item']}>Способ получения: <span className={styles['red']}>{order.receiptOption}</span></div>
                  <div className={styles['order__item']}>Позиции меню:</div>
                  <div className={styles['order__positions']}>
                     {order.takeawayOrderPositions.map((position: TakeawayOrderPosition) =>
                        <div className={styles['position']}>
                           <div className={styles['position__item']}>{position.menuPositionName}</div>
                           <div className={styles['position__item']}>{position.number} штук</div>
                           <div className={styles['position__item']}>{position.totalPrice}₽</div>
                        </div>
                     )}
                  </div>
               </div>)}
         </div>
      </div>
   )
}

export default ProfilePage