import styles from './Header.module.scss'

interface HeaderProps {
   currentUsername: string | null,
   logOut: () => void
}

function Header({ currentUsername, logOut }: HeaderProps) {

   return (
      <div className={styles['header']}>
         <div className={styles['header__block']}>
            <div className={styles['header__username']}> {currentUsername} </div>
            <button className={styles['header__button']} onClick={logOut}>Выйти</button>
         </div>
      </div>
   )
}

export default Header