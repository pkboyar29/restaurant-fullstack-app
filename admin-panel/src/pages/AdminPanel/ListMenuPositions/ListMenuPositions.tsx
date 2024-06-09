import styles from './ListMenuPositions.module.scss'
import axios from 'axios'
import { useEffect, useState } from 'react'
import { MenuPosition } from '../../../ts/types/MenuPosition'
import { createColumnHelper } from '@tanstack/react-table'
import Table from '../../../components/Table/Table'
import downIcon from '../../../assets/down.svg'
import { useNavigate } from 'react-router-dom'
import transformMenuPosition, { MenuPositionResponseFromServer } from '../../../ts/interfaces/MenuPositionResponseFromServer'
import { MenuSection } from '../../../ts/types/MenuSection'
import Cookies from 'js-cookie'

interface ListMenuPositionsProps {
   updateKey: number
}

function ListMenuPositions({ updateKey }: ListMenuPositionsProps) {
   const navigate = useNavigate()

   const [menuPositions, setMenuPositions] = useState<MenuPosition[]>([])
   const [menuSections, setMenuSections] = useState<MenuSection[]>([])
   const [dropdownVisible, setDropdownVisible] = useState<boolean>(false)

   const columnHelper = createColumnHelper<MenuPosition>()
   const columns = [
      columnHelper.accessor('id', {
         header: () => 'ID',
         cell: (info) => info.getValue()
         // enableHiding: false
      }),
      columnHelper.accessor('name', {
         header: () => 'Название позиции меню',
         cell: (info) => info.getValue()
         // enableHiding: false
      }),
      columnHelper.accessor('sectionName', {
         header: () => 'Название раздела меню',
         cell: (info) => info.getValue()
      }),
      columnHelper.accessor('portion', {
         header: () => 'Порция',
         cell: (info) => info.getValue()
      }),
      columnHelper.accessor('priceText', {
         header: () => 'Цена',
         cell: (info) => info.getValue()
      }),
      columnHelper.accessor('availability', {
         header: () => 'Доступность',
         cell: props => (
            <span>{props.getValue() ? <>Да</> : <>Нет</>}</span>
         )
      })
   ]

   useEffect(() => {
      getAllMenuPositions()
      getAllMenuSections()
   }, [updateKey])

   const deleteHandler = (id: number | string): void => {
      axios.delete('http://127.0.0.1:8080/api/menu-positions/' + id, {
         headers: {
            Authorization: `Bearer ${Cookies.get('adminToken')}`
         }
      })
         .then(response => {
            console.log(response.data)
            getAllMenuPositions()
         })
         .catch(error => console.log(error.response.data))
   }

   const editHandler = (id: number | string): void => {
      navigate('/admin-panel/menu-position?positionId=' + id)
   }

   const getAllMenuPositions = (): void => {
      axios.get('http://127.0.0.1:8080/api/menu-positions')
         .then(response => {
            const menuPositionsFromServer: MenuPositionResponseFromServer[] = response.data
            const menuPositions: MenuPosition[] = transformMenuPosition(menuPositionsFromServer)
            console.log(menuPositions)
            setMenuPositions(menuPositions)
         })
         .catch(error => console.log(error))
   }

   const getMenuPositionsBySectionId = (id: number): void => {
      axios.get('http://127.0.0.1:8080/api/menu-positions?sectionId=' + id)
         .then(response => {
            const menuPositionsFromServer: MenuPositionResponseFromServer[] = response.data
            const menuPositions: MenuPosition[] = transformMenuPosition(menuPositionsFromServer)
            console.log(menuPositions)
            setMenuPositions(menuPositions)
         })
         .catch(error => console.log(error))
   }

   const getAllMenuSections = (): void => {
      axios.get('http://127.0.0.1:8080/api/menu-sections')
         .then(response => {
            console.log(response.data)
            setMenuSections(response.data)
         })
         .catch(error => console.log(error))
   }

   const onSelectSectionHandler = (id: number): void => {
      console.log(id)
      setDropdownVisible(false)

      const sectionNameElement = document.querySelector('#section-name')
      if (sectionNameElement) {

         if (id === 0) {
            sectionNameElement.textContent = 'Все разделы меню'
            getAllMenuPositions()
         }
         else {
            const section = menuSections.find(section => section.id === id)
            if (section) {
               sectionNameElement.textContent = section.name
            }
            getMenuPositionsBySectionId(id)
         }
      }
   }

   return (
      <>
         <div className={styles['main-area']}>

            <div className={styles['list-header']}>
               <div className={styles['title']}>
                  Список позиций меню
               </div>
               <div className={styles['list-header__buttons']}>
                  <button onClick={() => navigate('/admin-panel/menu-position')} className={styles['list-header-button']}>
                     <div className={styles['list-header-button__icon']}>+</div>
                     <div className={styles['list-header-button__text']}>Новая позиция меню</div>
                  </button>
               </div>
            </div>

            <div className={styles['list__filters']}>
               <div className={styles['list__filters__sections']}>
                  <button onClick={() => setDropdownVisible(!dropdownVisible)} className={`${styles['list__filter']} ${styles['list-filter']}`}>
                     <div id='section-name' className={styles['list-filter__name']}>Все разделы меню</div>
                     <div className={`${styles['list-filter__icon']} ${dropdownVisible && styles['active']}`}><img src={downIcon} alt='треугольник вниз' /></div>
                  </button>
                  {dropdownVisible && <div className={`${styles['dropdownMenu']}`}>
                     <div key={0} className={styles['dropdownMenu__item']} onClick={() => onSelectSectionHandler(0)}>Все разделы меню</div>
                     {menuSections.map((section) => (
                        <div key={section.id} className={styles['dropdownMenu__item']} onClick={() => onSelectSectionHandler(section.id)}>{section.name}</div>
                     ))}
                  </div>}
               </div>
            </div>

            {menuPositions.length > 0 ? (<Table deleteHandler={deleteHandler} editHandler={editHandler} data={menuPositions}
               columns={columns} modalDeleteText='Вы точно хотите удалить эту позицию меню?' modalEditText='Перейти на страницу редактирования позиции меню?' />)
               : (<div style={{ marginTop: '15px' }}>Результатов не было найдено</div>)}

         </div>
      </>
   )
}

export default ListMenuPositions