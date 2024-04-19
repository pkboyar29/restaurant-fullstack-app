import { Routes, Route, Navigate } from "react-router-dom"
import AdminPanel from "./pages/AdminPanel"
import SignIn from "./pages/SignIn"
import './scss/App.scss'

function App() {

  return (
    <div className="App">
      <Routes>
        <Route path="/admin-panel" element={< AdminPanel />} />
        <Route path="/sign-in" element={< SignIn />} />
        <Route path="/" element={<Navigate to="/admin-panel"/>} />
      </Routes>
    </div>
  )
}

export default App
