import { Routes, Route, Navigate, useNavigate } from 'react-router-dom'
import { Client } from '../ts/types/Client'
import { useState, useEffect } from 'react'
import useCart from '../hooks/useCart'

import SignInPage from '../pages/SignInPage/SignInPage'
import SignUpPage from '../pages/SignUpPage/SignUpPage'
import MenuPage from '../pages/MenuPage/MenuPage'
import Header from '../components/Header/Header'
import TakeawayOrderPage from '../pages/TakeawayOrderPage/TakeawayOrderPage'
import ProfilePage from '../pages/ProfilePage/ProfilePage'

function App() {

  const [currentClient, setCurrentClient] = useState<Client | null>(null)
  const { numberCart, cart, setCartItem, deleteCartItem, changeNumberCartItem, clearCart, initiateCart } = useCart()
  const navigate = useNavigate()

  useEffect(() => {
    initiateCart()
  }, [])

  const signOut = () => {
    setCurrentClient(null)
    navigate('/menu')
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
        <Route path='/profile' element={<ProfilePage setCurrentClient={setCurrentClient} currentClient={currentClient} />} />
        <Route path='/menu' element={<MenuPage setCartItem={setCartItem} />} />
        <Route path='/order' element={<TakeawayOrderPage cart={cart} deleteCartItem={deleteCartItem} changeNumberCartItem={changeNumberCartItem} clearCart={clearCart} currentClient={currentClient} />} />
        <Route path='/' element={<Navigate to='menu' />} />
      </Routes>
    </div>
  )
}

export default App