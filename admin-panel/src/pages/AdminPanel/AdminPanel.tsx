import { Outlet } from "react-router-dom"
import Header from "../../components/Header/Header"
import Sidebar from "../../components/Sidebar/Sidebar"

interface AdminPanelProps {
   currentUsername: string | null,
   logOut: () => void
}

function AdminPanel({ currentUsername, logOut }: AdminPanelProps) {

   return (
      <div className="admin-panel">
         <Sidebar />
         <div className="right-panel">
            <Header logOut={logOut} currentUsername={currentUsername} />
            <div className="main">
               <Outlet />
            </div>
         </div>
      </div>
   )
}

export default AdminPanel