import styles from './Header.module.scss'
import { Link } from 'react-router-dom'

import logo from '../../assets/logo.svg'
import cart from '../../assets/cart.svg'

function Header() {
   return (
      <header className={styles['header']}>
         <div className={`${styles['container']} ${styles['header__container']}`}>
            <img className={styles['header__img']} src={logo} alt='logo' />

            <nav className={styles['header__nav']}>
               <Link to='/menu' className={styles['header__item']}>Меню</Link>
               <Link to='/' className={styles['header__item']}>Новости</Link>
               <Link to='/' className={styles['header__item']}>О нас</Link>
            </nav>

            <div className={styles['header__right']}>
               <div className={styles['header__cart']}>
                  <img src={cart} alt='cart' />
                  <div className={styles['header__cart_count']}>0</div>
               </div>

               <Link to='/sign-in' className={styles['header__signin']}>Войти</Link>
            </div>

         </div>
      </header>
   )
}

export default Header