import Header from "../../components/Header/Header"
import Sidebar from "../../components/Sidebar/Sidebar"

function AdminPanel() {

   return (
      <div className="admin-panel">
         <Sidebar />
         <div className="right-panel">
            <Header />
            <div className="main">
               Привет
            </div>
         </div>
      </div>
   )
}

export default AdminPanel