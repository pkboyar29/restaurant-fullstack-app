import { Routes, Route, Navigate } from 'react-router-dom'
import AdminPanel from '../pages/AdminPanel/AdminPanel'
import SignInPage from '../pages/SignInPage/SignInPage'
import './App.module.scss'
import NotFoundPage from '../pages/NotFoundPage/NotFoundPage'
import ListMenuPositions from '../pages/AdminPanel/ListMenuPositions/ListMenuPositions'
import MenuPositionForm from '../components/forms/MenuPositionForm/MenuPositionForm'
import { useState } from 'react'

function App() {

  const [updateKey, setUpdateKey] = useState<number>(0)

  const increaseUpdateKey = () => {
    setUpdateKey(updateKey + 1)
  }

  return (
    <div className='App'>
      <Routes>
        <Route path='/admin-panel' element={<AdminPanel />} >
          <Route path='menu-positions' element={<ListMenuPositions updateKey={updateKey} />} />
          <Route path='menu-position' element={<MenuPositionForm increaseUpdateKey={increaseUpdateKey} />} />
        </Route>
        <Route path='/sign-in' element={< SignInPage />} />
        <Route path='/' element={<Navigate to='/admin-panel' />} />
        <Route path='*' element={<NotFoundPage />} />
      </Routes>
    </div>
  )
}

export default App
