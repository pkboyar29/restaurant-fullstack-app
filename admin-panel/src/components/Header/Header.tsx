import styles from './Header.module.scss'
import { useState } from 'react'

import Modal from '../Modal/Modal'

interface HeaderProps {
   currentUsername: string | null,
   logOut: () => void
}

function Header({ currentUsername, logOut }: HeaderProps) {
   const [modal, setModal] = useState<boolean>()

   return (
      <>
         {modal && <Modal modalText='Вы точно хотите выйти?' buttonConfirmText='Да' confirmHandler={() => {
            logOut()
            setModal(false)
         }
         } cancelHandler={() => setModal(false)} />}

         <div className={styles['header']}>
            <div className={styles['header__block']}>
               <div className={styles['header__username']}> {currentUsername} </div>
               <button className={styles['header__button']} onClick={() => setModal(true)}>Выйти</button>
            </div>
         </div>
      </>
   )
}

export default Header