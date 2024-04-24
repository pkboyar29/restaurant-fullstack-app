import styles from './Sidebar.module.scss'

function Sidebar() {
   return (
      <div className={styles['sidebar']}>
         <div className={styles['sidebar__logo']}>Логотип</div>
         <div className={styles['sidebar__menu']}>
            <div className={styles['sidebar__item']}>Главная</div>
            <div className={styles['sidebar__item']}>Меню</div>
            <div className={styles['sidebar__item']}>Заказы на вынос</div>
            <div className={styles['sidebar__item']}>Клиенты</div>
         </div>
      </div>
   )
}

export default Sidebar