import Header from "../../components/Header/Header"
import Sidebar from "../../components/Sidebar/Sidebar"
import ListMenuPositions from "../../components/ListMenuPositions/ListMenuPositions"

function AdminPanel() {

   return (
      <div className="admin-panel">
         <Sidebar />
         <div className="right-panel">
            <Header />
            <div className="main">
               <ListMenuPositions />
            </div>
         </div>
      </div>
   )
}

export default AdminPanel