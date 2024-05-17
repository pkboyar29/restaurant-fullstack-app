import styles from './TakeawayOrderPage.module.scss'
import { Client } from '../../ts/types/Client'
import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'

import Title from '../../components/Title/Title'
import { OrderPosition } from '../../ts/types/OrderPosition'
import plusIcon from '../../assets/plus.svg'
import minusIcon from '../../assets/minus.svg'
import deleteIcon from '../../assets/delete.svg'
import nullimage from '../../assets/nullimage.jpeg'
import { MenuPosition } from '../../ts/types/MenuPosition'

interface TakeawayOrderPageProps {
   cart: OrderPosition[],
   currentClient: Client | null,
   deleteCartItem: (menuPositionId: number) => void,
   changeNumberCartItem: (MenuPositionId: number, plus: boolean) => void
}

function TakeawayOrderPage({ cart, currentClient, deleteCartItem, changeNumberCartItem }: TakeawayOrderPageProps) {

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
         <Title>Ваш заказ</Title>

         <div className={styles['container']}>
            <div className={styles['cart']}>

               {cart.map((item: OrderPosition, index: number) => (
                  <div key={index} className={styles['cart__item']}>
                     <div className={styles['cart__image']}>{item.menuPositon.image1 ? <img src={item.menuPositon.image1} alt='image' /> : <img src={nullimage} alt='null image' />}</div>
                     <div className={styles['cart__name']}>{item.menuPositon.name}</div>
                     <div className={styles['cart__plusminus']}>
                        <img onClick={() => changeNumberCartItem(item.menuPositon.id, true)} className={styles['cart__icon']} src={plusIcon} alt='plus icon' />
                        <div className={styles['cart__number']}>{item.number}</div>
                        <img onClick={() => changeNumberCartItem(item.menuPositon.id, false)} className={styles['cart__icon']} src={minusIcon} alt='minus icon' />
                     </div>
                     <div className={styles['cart__totalPrice']}>{item.totalPrice} ₽</div>
                     <img onClick={() => deleteCartItem(item.menuPositon.id)} className={styles['cart__delete']} src={deleteIcon} alt='delete icon' />
                  </div>
               ))}

            </div>
         </div>

      </>
   )
}

export default TakeawayOrderPage