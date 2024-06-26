import styles from './Header.module.scss'
import { Link } from 'react-router-dom'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

import usericon from '../../assets/user.svg'
import logo from '../../assets/logo.svg'
import cart from '../../assets/cart.svg'
import { Client } from '../../ts/types/Client'
import Modal from '../Modal/Modal'

interface HeaderProps {
   numberCart: number
   currentClient: Client | null
   signOut: () => void
}

function Header({ numberCart, currentClient, signOut }: HeaderProps) {
   const [dropDown, setDropDown] = useState<boolean>(false)
   const [modal, setModal] = useState<boolean>(false)
   const navigate = useNavigate()

   return (
      <>
         {modal && <Modal modalText='Вы уверены, что хотите выйти?' buttonConfirmText='Да' confirmHandler={() => {
            setModal(false)
            signOut()
         }} />}
         <header className={styles['header']}>
            <div className={`${styles['container']} ${styles['header__container']}`}>

               <div className={styles['header__left']}>
                  <img className={styles['header__img']} src={logo} alt='logo' />

                  <nav className={styles['header__nav']}>
                     <Link to='/menu' className={styles['header__item']}>Меню</Link>
                     <Link to='/' className={styles['header__item']}>Новости</Link>
                     <Link to='/' className={styles['header__item']}>О нас</Link>
                  </nav>
               </div>


               <div className={styles['header__right']}>
                  <div onClick={() => navigate('/order')} className={styles['header__cart']}>
                     <img src={cart} alt='cart' />
                     <div className={styles['header__cart_count']}>{numberCart}</div>
                  </div>

                  {currentClient
                     ? (
                        <div className={styles['user__info']}>
                           <div onClick={() => setDropDown(!dropDown)} className={styles['header__select']}>
                              <img src={usericon} alt='user icon' /> {currentClient.username}</div>
                           {dropDown && <ul className={styles['dropdown']}>
                              <li className={styles['dropdown__item']}><Link to='/profile'>Просмотр профиля</Link>
                              </li>
                              <li onClick={() => {
                                 setDropDown(false)
                                 setModal(true)
                              }} className={styles['dropdown__item']}><div>Выйти</div></li>
                           </ul>}
                        </div>
                     )
                     : (<Link to='/sign-in' className={styles['header__signin']}>Войти</Link>)}

               </div>

            </div>
         </header>
      </>

   )
}

export default Header