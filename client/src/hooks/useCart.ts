import { useState } from 'react'

import { OrderPosition } from '../ts/types/OrderPosition'
import { MenuPosition } from '../ts/types/MenuPosition'

const useCart = () => {

   const [numberCart, setNumberCart] = useState<number>(0)
   const [cart, setCart] = useState<OrderPosition[]>([])

   const updateNumberCart = (cart: OrderPosition[]) => {
      let totalNumberCart = 0
      cart.map((item: OrderPosition) => {
         totalNumberCart += item.number
      })
      setNumberCart(totalNumberCart)
   }

   const setCartItem = (menuPosition: MenuPosition) => {
      const cartLocalStorage = localStorage.getItem('cart')

      if (!cartLocalStorage) {
         const newOrderPositon: OrderPosition = {
            number: 1,
            totalPrice: menuPosition.price,
            menuPositon: menuPosition
         }
         setCart([newOrderPositon])
         localStorage.setItem('cart', JSON.stringify([newOrderPositon]))

         setNumberCart(1)

      } else {
         const cartt = JSON.parse(cartLocalStorage)

         if (cartt.some((item: OrderPosition) => item.menuPositon.id === menuPosition.id)) {
            cartt.forEach((item: OrderPosition) => {
               if (item.menuPositon.id === menuPosition.id) {
                  item.number += 1
                  item.totalPrice += menuPosition.price
               }
            })
         }
         else {
            const newOrderPosition: OrderPosition = {
               number: 1,
               totalPrice: menuPosition.price,
               menuPositon: menuPosition
            }
            cartt.push(newOrderPosition)
         }

         setCart(cartt)
         localStorage.setItem('cart', JSON.stringify(cartt))

         setNumberCart(numberCart + 1)
      }
   }

   const deleteCartItem = (menuPositionId: number) => {
      const updatedCart = cart.filter((item: OrderPosition) => item.menuPositon.id !== menuPositionId)

      setCart(updatedCart)
      localStorage.setItem('cart', JSON.stringify(updatedCart))
      updateNumberCart(updatedCart)
   }

   const changeNumberCartItem = (menuPositionId: number, plus: boolean) => {
      const updatedCart: OrderPosition[] = cart.map((item: OrderPosition) => {
         if (item.menuPositon.id === menuPositionId) {
            let newNumber = item.number
            let newTotalPrice = item.totalPrice

            if (plus && item.number < 10) {
               newNumber += 1
               newTotalPrice += item.menuPositon.price
            } else if (!plus && item.number > 1) {
               newNumber -= 1
               newTotalPrice -= item.menuPositon.price
            }

            return {
               ...item,
               number: newNumber,
               totalPrice: newTotalPrice
            }
         }
         return item
      })

      setCart(updatedCart)
      localStorage.setItem('cart', JSON.stringify(updatedCart))
      updateNumberCart(updatedCart)
   }

   const clearCart = () => {
      setCart([])
      setNumberCart(0)
      localStorage.removeItem('cart')
   }

   const initiateCart = () => {
      const cartLocalStorage = localStorage.getItem('cart')
      if (cartLocalStorage) {
         const cartt: OrderPosition[] = JSON.parse(cartLocalStorage)

         setCart(cartt)
         updateNumberCart(cartt)
      }
   }

   return { numberCart, cart, updateNumberCart, setCartItem, deleteCartItem, changeNumberCartItem, clearCart, initiateCart }
}

export default useCart