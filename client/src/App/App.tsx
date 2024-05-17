import { Routes, Route, Navigate } from "react-router-dom"
import { MenuPosition } from "../ts/types/MenuPosition"
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

  return (
    <div className='App'>

      <Header numberCart={numberCart} />

      <Routes>
        <Route path='/sign-in' element={<SignInPage />} />
        <Route path='/sign-up' element={<SignUpPage />} />
        <Route path='/menu' element={<MenuPage setCartItem={setCartItem} />} />
        <Route path='/' element={<Navigate to='menu' />} />
      </Routes>
    </div>
  )
}

export default App
