import './Sidebar.scss'

function Sidebar() {
   return (
      <div className="sidebar">
         <div className="sidebar__logo">Логотип</div>
         <div className="sidebar__menu">
            <div className="sidebar__item">Главная</div>
            <div className="sidebar__item">Меню</div>
            <div className="sidebar__item">Заказы на вынос</div>
            <div className="sidebar__item">Клиенты</div>
         </div>
      </div>
   )
}

export default Sidebar