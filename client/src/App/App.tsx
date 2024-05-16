import { Routes, Route, Navigate } from "react-router-dom"

import SignInPage from '../pages/SignInPage/SignInPage'
import SignUpPage from '../pages/SignUpPage/SignUpPage'
import MenuPage from "../pages/MenuPage/MenuPage"
import Header from '../components/Header/Header'

function App() {

  return (
    <div className='App'>

      <Header />

      <Routes>
        <Route path='/sign-in' element={<SignInPage />} />
        <Route path='/sign-up' element={<SignUpPage />} />
        <Route path='/menu' element={<MenuPage />} />
        <Route path='/' element={<Navigate to='menu' />} />
      </Routes>
    </div>
  )
}

export default App
