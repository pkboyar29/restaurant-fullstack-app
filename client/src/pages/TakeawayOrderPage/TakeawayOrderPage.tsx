import styles from './TakeawayOrderPage.module.scss'
import { Client } from '../../ts/types/Client'
import { OrderRequest } from '../../ts/types/OrderRequest'
import { OrderPosition } from '../../ts/types/OrderPosition'
import { OrderPositionResponse } from '../../ts/types/OrderPositionResponse'
import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import axios from 'axios'
import { useNavigate } from 'react-router-dom'

import Modal from '../../components/Modal/Modal'
import Title from '../../components/Title/Title'
import Button from '../../components/Button/Button'
import SelectInput from '../../components/SelectInput/SelectInput'
import TextInput from '../../components/TextInput/TextInput'
import plusIcon from '../../assets/plus.svg'
import minusIcon from '../../assets/minus.svg'
import deleteIcon from '../../assets/delete.svg'
import nullimage from '../../assets/nullimage.jpeg'

interface TakeawayOrderPageProps {
   cart: OrderPosition[],
   currentClient: Client | null,
   deleteCartItem: (menuPositionId: number) => void,
   changeNumberCartItem: (MenuPositionId: number, plus: boolean) => void,
   clearCart: () => void
}

type TakeawayOrderFields = {
   clientName: string,
   clientPhone: string,
   requirements: string,
   paymentMethod: string,
   receiptDate: Date,
   receiptOption: string
}

function TakeawayOrderPage({ cart, currentClient, deleteCartItem, changeNumberCartItem, clearCart }: TakeawayOrderPageProps) {

   const [cost, setCost] = useState<number>(0)
   const [discountedCost, setDiscountedCost] = useState<number>(0)
   const [submitModal, setSubmitModal] = useState<boolean>(false)
   const [deleteModal, setDeleteModal] = useState<boolean>(false)
   const [itemIdToDelete, setItemIdToDelete] = useState<number>(0)

   const navigate = useNavigate()

   const onlyLettersRegex = /^[A-Za-zА-Яа-яЁё]+$/
   const phoneRegex = /^\+?\d{1,3}?[-.\s]?\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{4}$/

   const { register, handleSubmit, formState: { errors } } = useForm<TakeawayOrderFields>({
      mode: "onBlur", defaultValues: {
         clientName: currentClient ? currentClient.firstName : '',
         clientPhone: currentClient ? currentClient.phone : ''
      }
   })

   function convertToOrderPositionResponse(orderPositions: OrderPosition[]): OrderPositionResponse[] {
      return orderPositions.map((orderPosition: OrderPosition) => {
         return {
            number: orderPosition.number,
            totalPrice: orderPosition.totalPrice,
            menuPositionId: orderPosition.menuPositon.id
         };
      });
   }
   const onSubmit = (data: TakeawayOrderFields) => {

      const takeawayOrderPositionList: OrderPositionResponse[] = convertToOrderPositionResponse(cart)

      const requestData: OrderRequest = {
         clientName: data.clientName,
         clientPhone: data.clientPhone,
         userId: currentClient ? Number(currentClient.id) : null,
         requirements: data.requirements,
         paymentMethod: data.paymentMethod,
         receiptDate: data.receiptDate,
         receiptOption: data.receiptOption,
         takeawayOrderPositionList: takeawayOrderPositionList
      }

      console.log('перед отправкой запроса: ', requestData)

      axios.post(import.meta.env.VITE_BACKEND_URL + '/api/takeaway-orders', requestData)
         .then(response => {
            console.log(response.data)
            setSubmitModal(true)
         })
         .catch(error => console.log(error))
   }

   useEffect(() => {
      let totalCost = 0
      let discountedTotalCost = 0

      cart.map((item: OrderPosition) => {
         totalCost += item.totalPrice
      })
      setCost(totalCost)

      if (currentClient) {
         discountedTotalCost = totalCost - ((totalCost * currentClient.orderDiscount.discount) / 100)
         setDiscountedCost(discountedTotalCost)
      }
   }, [cart])

   if (cart.length === 0) {
      return (
         <>
            <Title>Ваш заказ</Title>

            <Link to='/menu'>Ваша корзина пуста. Перейти к покупкам</Link>
         </>
      )
   }

   return (
      <>
         {submitModal && <Modal modalText='Заказ успешно создан!' buttonConfirmText='Ок' confirmHandler={() => {
            setSubmitModal(false)
            navigate('/menu')
            clearCart()
         }} />}

         {deleteModal && <Modal modalText='Вы уверены, что хотите удалить этот товар?' buttonConfirmText='Да' confirmHandler={() => {
            setDeleteModal(false)
            deleteCartItem(itemIdToDelete)
         }} />}

         <Title>Ваш заказ</Title>

         <div className={styles['container']}>
            <div className={styles['cart']}>

               {cart.map((item: OrderPosition, index: number) => (
                  <div key={index} className={styles['cart__item']}>
                     <div className={styles['cart__left']}>
                        <div className={styles['cart__image']}>{item.menuPositon.image1 ? <img src={item.menuPositon.image1} alt='image' /> : <img src={nullimage} alt='null image' />}</div>
                        <div className={styles['cart__name']}>{item.menuPositon.name}</div>
                     </div>
                     <div className={styles['cart__right']}>
                        <div className={styles['cart__plusminus']}>
                           <img onClick={() => changeNumberCartItem(item.menuPositon.id, true)} className={styles['cart__icon']} src={plusIcon} alt='plus icon' />
                           <div className={styles['cart__number']}>{item.number}</div>
                           <img onClick={() => changeNumberCartItem(item.menuPositon.id, false)} className={styles['cart__icon']} src={minusIcon} alt='minus icon' />
                        </div>
                        <div className={styles['cart__totalPrice']}>{item.totalPrice} ₽</div>
                        <img onClick={() => {
                           setDeleteModal(true)
                           setItemIdToDelete(item.menuPositon.id)
                        }} className={styles['cart__delete']} src={deleteIcon} alt='delete icon' />
                     </div>
                  </div>
               ))}

               <div className={styles['cart__cost']}>Общая стоимость: {cost} ₽</div>
               {currentClient
                  ? (<div className={styles['cart__discountedcost']}>Общая стоимость со скидкой: {discountedCost} ₽. Оформляйте больше заказов, чтобы иметь большую скидку</div>)
                  : (<Link className={styles['link']} to='/sign-in'>Войдите или создайте аккаунт, чтобы получить скидку</Link>)}

               <form className={styles['takeaway__form']} onSubmit={handleSubmit(onSubmit)}>

                  <TextInput
                     inputTitle='Имя'
                     placeholder='Введите свое имя'
                     errorMessage={typeof errors.clientName?.message === 'string' ? errors.clientName.message : ''}
                     fieldName='clientName'
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
                     }}
                     inputProps={{
                        type: 'text',
                        autoComplete: 'off',
                        maxLength: 30,
                        style: { maxWidth: '700px' }
                     }}
                  />

                  <TextInput
                     inputTitle='Телефон'
                     placeholder='Введите свой телефон'
                     errorMessage={typeof errors.clientPhone?.message === 'string' ? errors.clientPhone.message : ''}
                     fieldName='clientPhone'
                     register={register}
                     validationRules={{
                        required: 'Поле обязательно к заполнению',
                        pattern: {
                           value: phoneRegex,
                           message: 'Некорректный формат телефона'
                        }
                     }}
                     inputProps={{
                        type: 'text',
                        autoComplete: 'off',
                        style: { maxWidth: '700px' }
                     }}
                  />

                  <TextInput
                     inputTitle='Особые требования'
                     placeholder='Введите, чтобы вы хотели дополнительно'
                     errorMessage={typeof errors.requirements?.message === 'string' ? errors.requirements.message : ''}
                     fieldName='requirements'
                     register={register}
                     inputProps={{
                        type: 'text',
                        autoComplete: 'off',
                        style: { height: '120px', maxWidth: '700px' }
                     }}
                  />

                  <input {...register('receiptDate')} className={styles['input__date']} type='datetime-local' />

                  <SelectInput
                     inputProps={{
                        style: { maxWidth: '700px' }
                     }}
                     selectTitle='Способ получения заказ'
                     selectOptions={[
                        {
                           'value': 'самовывоз',
                           'label': 'Самовывоз'
                        },
                        {
                           'value': 'доставка',
                           'label': 'Доставка'
                        }
                     ]}
                     fieldName='receiptOption'
                     register={register}
                  />

                  <SelectInput
                     inputProps={{
                        style: { maxWidth: '700px' }
                     }}
                     selectTitle='Способ оплаты при получении'
                     selectOptions={[
                        {
                           'value': 'наличный расчет',
                           'label': 'Наличными'
                        },
                        {
                           'value': 'безналичный расчет',
                           'label': 'Картой'
                        }
                     ]}
                     fieldName='paymentMethod'
                     register={register}
                  />

                  <Button buttonProps={{
                     style: { maxWidth: '700px' }
                  }} text='Оформить заказ' onClick={() => console.log('hello')}></Button>

               </form>

            </div>
         </div>

      </>
   )
}

export default TakeawayOrderPage