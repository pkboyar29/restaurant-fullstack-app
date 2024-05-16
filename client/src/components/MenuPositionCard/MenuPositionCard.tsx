import { useEffect, useState } from 'react'
import styles from './MenuPositionCard.module.scss'
import axios from 'axios'
import { MenuPosition } from '../../ts/types/MenuPosition'
import nullimage from '../../assets/nullimage.jpeg'

interface MenuPositionCardProps {
   menuPositionId: number,
   handleSetModal: (menuPosition: MenuPosition | null) => void
}

function MenuPositionCard({ menuPositionId, handleSetModal }: MenuPositionCardProps) {

   const [menuPosition, setMenuPosition] = useState<MenuPosition | null>(null)

   const getMenuPositionById = (id: number) => {
      axios.get(import.meta.env.VITE_BACKEND_URL + '/api/menu-positions/' + id)
         .then(response => {
            setMenuPosition(response.data)
         })
         .catch(error => console.log(error))
   }

   useEffect(() => {
      getMenuPositionById(menuPositionId)
   }, [])

   return (
      <div onClick={() => handleSetModal(menuPosition)} className={styles['card']}>
         <div className={styles['card__image']}>
            {menuPosition?.image1 ? <img src={menuPosition?.image1} alt={menuPosition?.name} /> : <img src={nullimage} alt={'null image'} />}
         </div>
         <div className={styles['card__price']}>
            {menuPosition?.price} ₽
         </div>
         <div className={styles['card__name']}>{menuPosition?.name}</div>
         <div className={styles['card__portion']}>{menuPosition?.portion}</div>
      </div>
   )
}

export default MenuPositionCard