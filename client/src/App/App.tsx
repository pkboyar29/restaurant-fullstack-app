import { Routes, Route, Navigate } from "react-router-dom"
import SignInPage from "../pages/SignInPage/SignInPage"
import SignUpPage from "../pages/SignUpPage/SignUpPage"

function App() {

  return (
    <div className='App'>
      <Routes>
        <Route path='/sign-in' element={<SignInPage />} />
        <Route path='/sign-up' element={<SignUpPage />} />
        <Route path='/' element={<Navigate to='sign-up' />} />
      </Routes>
    </div>
  )
}

export default App
