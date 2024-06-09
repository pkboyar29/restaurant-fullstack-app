import styles from './MenuPage.module.scss'
import axios from 'axios'
import { useEffect, useState } from 'react'

import { MenuPosition } from '../../ts/types/MenuPosition'
import { MenuSection } from '../../ts/types/MenuSection'
import Title from '../../components/Title/Title'
import MenuPositionCard from '../../components/MenuPositionCard/MenuPositionCard'
import MenuPositionModal from '../../components/MenuPositionModal/MenuPositionModal'
import Modal from '../../components/Modal/Modal'

interface MenuPageProps {
   setCartItem: (menuPosition: MenuPosition) => void
}

function MenuPage({ setCartItem }: MenuPageProps) {
   const [menuPositions, setMenuPositions] = useState<MenuPosition[]>([])
   const [menuSections, setMenuSections] = useState<MenuSection[]>([])
   const [selectedSection, setSelectedSection] = useState<number>(0)
   const [menuPositionModal, setMenuPositionModal] = useState<boolean>(false)
   const [resultModal, setResultModal] = useState<boolean>(false)
   const [currentMenuPosition, setCurrentMenuPostion] = useState<MenuPosition | null>(null)

   const getMenuPositions = (selectedSection: number) => {
      if (selectedSection === 0) {
         axios.get(import.meta.env.VITE_BACKEND_URL + '/api/menu-positions?onlyAvailable=true')
            .then(response => {
               setMenuPositions(response.data)
            })
            .catch(error => console.log(error))
      } else {
         axios.get(import.meta.env.VITE_BACKEND_URL + `/api/menu-positions?onlyAvailable=true&sectionId=${selectedSection}`)
            .then(response => {
               setMenuPositions(response.data)
            })
            .catch(error => console.log(error))
      }
   }

   const getMenuSections = () => {
      axios.get(import.meta.env.VITE_BACKEND_URL + '/api/menu-sections')
         .then(response => {
            const menuSections: MenuSection[] = response.data
            setMenuSections(menuSections)
         })
         .catch(error => console.log(error))
   }

   useEffect(() => {
      getMenuPositions(selectedSection)
      getMenuSections()
   }, [])

   const handleSetModal = (menuPosition: MenuPosition | null) => {
      setCurrentMenuPostion(menuPosition)
      setMenuPositionModal(true)
      document.body.classList.add('menu-modal-open')
   }

   const handleCloseModal = (result: boolean) => {
      setCurrentMenuPostion(null)
      setMenuPositionModal(false)
      document.body.classList.remove('menu-modal-open')

      if (result) {
         setResultModal(true)
         document.body.classList.add('menu-modal-open')
      }
   }

   return (
      <div className={`${styles['container']} ${styles['menu__container']}`}>

         {menuPositionModal && <MenuPositionModal setCartItem={setCartItem} handleCloseModal={handleCloseModal} menuPosition={currentMenuPosition} />}
         {resultModal && <Modal modalText='Товар успешно добавлен в корзину!' buttonConfirmText='Ок' confirmHandler={() => {
            document.body.classList.remove('menu-modal-open')
            setResultModal(false)
         }} />}

         <Title>Наше меню</Title>

         {menuSections && (
            <div className={styles['menu__sections']}>
               <div key={0} className={`${styles['menu__section']} ${selectedSection === 0 && styles['active']}`}
                  onClick={() => {
                     setSelectedSection(0)
                     getMenuPositions(0)
                  }}>Все</div>
               {menuSections.map((menuSection: MenuSection) => (
                  <div className={`${styles['menu__section']} ${selectedSection === menuSection.id && styles['active']}`}
                     key={menuSection.id} onClick={() => {
                        setSelectedSection(menuSection.id)
                        getMenuPositions(menuSection.id)
                     }}>{menuSection.name}</div>
               ))}
            </div>
         )}

         <div className={styles['menu__cards']}>
            {menuPositions.length === 0 ? <div> Результатов не найдено </div>
               : menuPositions.map((menuPosition) => (
                  <MenuPositionCard key={menuPosition.id} handleSetModal={handleSetModal} menuPositionId={menuPosition.id} />
               ))
            }
         </div>
      </div>
   )
}

export default MenuPage