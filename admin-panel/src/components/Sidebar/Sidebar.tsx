import styles from './Sidebar.module.scss'

function Sidebar() {
   return (
      <div className={styles['sidebar']}>
         <div className={styles['sidebar__logo']}>Логотип</div>
         <div className={styles['sidebar__menu']}>
            <div className={styles['sidebar__item']}>Позиции меню</div>
            <div className={styles['sidebar__item']}>Разделы меню</div>
            <div className={styles['sidebar__item']}>Заказы на вынос</div>
         </div>
      </div>
   )
}

export default Sidebar