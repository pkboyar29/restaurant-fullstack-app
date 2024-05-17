import { Routes, Route, Navigate } from "react-router-dom"
import { MenuPosition } from "../ts/types/MenuPosition"
import { Client } from '../ts/types/Client'
import { useState, useEffect } from 'react'

import SignInPage from '../pages/SignInPage/SignInPage'
import SignUpPage from '../pages/SignUpPage/SignUpPage'
import MenuPage from '../pages/MenuPage/MenuPage'
import Header from '../components/Header/Header'


type OrderPosition = {
  number: number,
  menuPositon: MenuPosition
}

function App() {

  const [numberCart, setNumberCart] = useState<number>(0)
  const [currentClient, setCurrentClient] = useState<Client | null>(null)

  const setCartItem = (menuPosition: MenuPosition) => {
    const cartLocalStorage = localStorage.getItem('cart')

    if (!cartLocalStorage) {
      const newOrderPositon: OrderPosition = {
        number: 1,
        menuPositon: menuPosition
      }
      const cart = [newOrderPositon]
      localStorage.setItem('cart', JSON.stringify(cart))

      setNumberCart(1)
    } else {
      const cart = JSON.parse(cartLocalStorage)

      if (cart.some((item: OrderPosition) => item.menuPositon.id === menuPosition.id)) {
        cart.forEach((item: OrderPosition) => {
          if (item.menuPositon.id === menuPosition.id) {
            item.number = item.number + 1
          }
        })
      }
      else {
        const newOrderPosition: OrderPosition = {
          number: 1,
          menuPositon: menuPosition
        }
        cart.push(newOrderPosition)
      }
      localStorage.setItem('cart', JSON.stringify(cart))

      setNumberCart(numberCart + 1)
    }
  }

  const signOut = () => {
    console.log('вышел, хорош')
    setCurrentClient(null)
    localStorage.removeItem('currentClient')
  }

  useEffect(() => {
    const cartLocalStorage = localStorage.getItem('cart')
    if (cartLocalStorage) {
      const cart = JSON.parse(cartLocalStorage)
      let totalNumberCart = 0

      cart.forEach((item: OrderPosition) => {
        totalNumberCart += item.number
      })

      setNumberCart(totalNumberCart)
    }
  }, [])

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
        <Route path='/' element={<Navigate to='menu' />} />
      </Routes>
    </div>
  )
}

export default App
