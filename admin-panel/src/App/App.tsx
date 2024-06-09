import { Routes, Route, Navigate, useNavigate } from 'react-router-dom'
import './App.module.scss'
import { useState, useEffect } from 'react'
import { jwtDecode, JwtPayload } from 'jwt-decode'
import Cookies from 'js-cookie'

import SignInPage from '../pages/SignInPage/SignInPage'
import NotFoundPage from '../pages/NotFoundPage/NotFoundPage'
import AdminPanel from '../pages/AdminPanel/AdminPanel'
import ListMenuPositions from '../pages/AdminPanel/ListMenuPositions/ListMenuPositions'
import ListMenuSections from '../pages/AdminPanel/ListMenuSections/ListMenuSections'
import MenuPositionForm from '../components/forms/MenuPositionForm/MenuPositionForm'
import MenuSectionForm from '../components/forms/MenuSectionForm/MenuSectionForm'

function App() {
  const navigate = useNavigate()
  const [updateKey, setUpdateKey] = useState<number>(0)
  const [currentUsername, setCurrentUsername] = useState<string
    | null>(null)

  useEffect(() => {
    setUsername()
  }, [])

  const setUsername = () => {
    const token: string | undefined = Cookies.get('adminToken')

    if (token) {
      const decoded: JwtPayload = jwtDecode(token)
      if (typeof decoded === 'object' && 'username' in decoded && typeof decoded.username === 'string') {
        setCurrentUsername(decoded.username)
      }
    }
  }

  const logOut = () => {
    setCurrentUsername(null)
    Cookies.remove('adminToken')
    navigate('/sign-in')
  }

  const increaseUpdateKey = () => {
    setUpdateKey(updateKey + 1)
  }

  const checkTokenExpiration = () => {
    const token = Cookies.get('adminToken')

    if (token) {
      try {
        const decoded = jwtDecode(token)
        const currentTime = Date.now() / 1000

        if (decoded.exp && decoded.exp < currentTime) {
          console.log(decoded.exp)
          Cookies.remove('adminToken')
          return false
        }
        return true
      } catch (error) {
        console.error('Failed to decode token:', error)
        return false
      }
    }
    return false
  }

  useEffect(() => {
    const tokenExpired: boolean = checkTokenExpiration()

    if (!tokenExpired) {
      navigate('/sign-in')
    }
  }, [])

  return (
    <div className='App'>
      <Routes>
        <Route path='/admin-panel' element={<AdminPanel logOut={logOut} currentUsername={currentUsername} />} >
          <Route path='menu-positions' element={<ListMenuPositions updateKey={updateKey} />} />
          <Route path='menu-sections' element={<ListMenuSections />} />
          <Route path='menu-position' element={<MenuPositionForm increaseUpdateKey={increaseUpdateKey} />} />
          <Route path='menu-section' element={<MenuSectionForm />} />
        </Route>
        <Route path='/sign-in' element={< SignInPage setUsername={setUsername} />} />
        <Route path='/' element={<Navigate to='sign-in' />} />
        <Route path='*' element={<NotFoundPage />} />
      </Routes>
    </div>
  )
}

export default App
