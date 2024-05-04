import { Routes, Route, Navigate } from "react-router-dom"
import AdminPanel from "../pages/AdminPanel/AdminPanel"
import SignInPage from "../pages/SignInPage/SignInPage"
import './App.module.scss'
import NotFoundPage from "../pages/NotFoundPage/NotFoundPage"
import ListMenuPositions from "../pages/AdminPanel/ListMenuPositions/ListMenuPositions"
import AddMenuPositionForm from "../components/forms/AddMenuPositionForm/AddMenuPositionForm"
import EditMenuPositionForm from "../components/forms/EditMenuPositionForm/EditMenuPositionForm"
import { useState } from "react"

function App() {

  const [updateKey, setUpdateKey] = useState<number>(0)

  const increaseUpdateKey = () => {
    console.log("метод вызвался")
    setUpdateKey(updateKey + 1)
  }

  return (
    <div className="App">
      <Routes>
        <Route path="/admin-panel" element={<AdminPanel />} >
          <Route path="menu-positions" element={<ListMenuPositions updateKey={updateKey} />} />
          <Route path="add-menu-position" element={<AddMenuPositionForm increaseUpdateKey={increaseUpdateKey} />} />
          <Route path="edit-menu-position" element={<EditMenuPositionForm />} />
        </Route>
        <Route path="/sign-in" element={< SignInPage />} />
        <Route path="/" element={<Navigate to="/admin-panel" />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </div>
  )
}

export default App
