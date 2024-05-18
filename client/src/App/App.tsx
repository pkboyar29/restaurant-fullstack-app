import { Routes, Route, Navigate } from "react-router-dom"
import { MenuPosition } from "../ts/types/MenuPosition"
import { Client } from '../ts/types/Client'
import { useState, useEffect } from 'react'
import { OrderPosition } from '../ts/types/OrderPosition'

import SignInPage from '../pages/SignInPage/SignInPage'
import SignUpPage from '../pages/SignUpPage/SignUpPage'
import MenuPage from '../pages/MenuPage/MenuPage'
import Header from '../components/Header/Header'
import TakeawayOrderPage from "../pages/TakeawayOrderPage/TakeawayOrderPage"

function App() {

  const [numberCart, setNumberCart] = useState<number>(0)
  const [currentClient, setCurrentClient] = useState<Client | null>(null)
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

  useEffect(() => {
    const cartLocalStorage = localStorage.getItem('cart')
    if (cartLocalStorage) {
      const cartt: OrderPosition[] = JSON.parse(cartLocalStorage)

      setCart(cartt)
      updateNumberCart(cartt)
    }
  }, [])

  const signOut = () => {
    setCurrentClient(null)
    localStorage.removeItem('currentClient')
  }

  useEffect(() => {
    const currentClientLocalStorage = localStorage.getItem('currentClient')
    if (currentClientLocalStorage) {
      const currentClient: Client = JSON.parse(currentClientLocalStorage)
      setCurrentClient(currentClient)
    }
  }, [])

  return (
    <div className='App'>

      <Header currentClient={currentClient} numberCart={numberCart} signOut={signOut} />

      <Routes>
        <Route path='/sign-in' element={<SignInPage setCurrentClient={setCurrentClient} />} />
        <Route path='/sign-up' element={<SignUpPage setCurrentClient={setCurrentClient} />} />
        <Route path='/menu' element={<MenuPage setCartItem={setCartItem} />} />
        <Route path='/order' element={<TakeawayOrderPage cart={cart} deleteCartItem={deleteCartItem} changeNumberCartItem={changeNumberCartItem} currentClient={currentClient} />} />
        <Route path='/' element={<Navigate to='menu' />} />
      </Routes>
    </div>
  )
}

export default App