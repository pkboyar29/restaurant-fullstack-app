import styles from './Sidebar.module.scss'
import { useNavigate } from 'react-router-dom'

function Sidebar() {

   const navigate = useNavigate()

   return (
      <div className={styles['sidebar']}>
         <div className={styles['sidebar__logo']}>Логотип</div>
         <div className={styles['sidebar__menu']}>
            <div onClick={() => navigate('/admin-panel/menu-positions')} className={styles['sidebar__item']}>Позиции меню</div>
            <div onClick={() => navigate('/admin-panel/menu-sections')} className={styles['sidebar__item']}>Разделы меню</div>
         </div>
      </div>
   )
}

export default Sidebar