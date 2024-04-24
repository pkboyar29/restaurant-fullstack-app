import styles from './ListMenuPositions.module.scss'
import axios from 'axios'
import { useEffect, useState } from 'react'
import { MenuPosition } from '../../../ts/types/MenuPosition'
import { createColumnHelper } from '@tanstack/react-table'
import Table from '../../../components/Table/Table'
import Switch from '../../../components/Switch/Switch'

function ListMenuPositions() {

   const [menuPositions, setMenuPositions] = useState<MenuPosition[]>([])

   const columnHelper = createColumnHelper<MenuPosition>()

   const columns = [
      columnHelper.accessor('id', {
         header: () => 'ID',
         cell: (info) => info.getValue()
      }),
      columnHelper.accessor('name', {
         header: () => 'Название позиции меню',
         cell: (info) => info.getValue()
      }),
      columnHelper.accessor('sectionName', {
         header: () => 'Название секции меню',
         cell: (info) => info.getValue()
      }),
      columnHelper.accessor('portion', {
         header: () => 'Порция',
         cell: (info) => info.getValue()
      }),
      columnHelper.accessor('price', {
         header: () => 'Цена',
         cell: (info) => info.getValue()
      }),
      columnHelper.accessor('availability', {
         header: () => 'Доступность',
         cell: props => (
            <span>{<Switch initialValue={props.getValue()} />}</span>
         )
      })
   ]

   useEffect(() => {
      // получить результат запроса сразу в переменную (через присовение переменной)?
      axios.get('http://127.0.0.1:8080/api/menu-positions/')
         .then(response => setMenuPositions(response.data))
         .catch(error => console.log(error))
   }, [])

   return (
      <>
         <div className={styles['main-area']}>
            <div className={styles['list-header']}>
               <div className={styles['title']}>
                  Список позиций меню
               </div>

               <div className={styles['list-header__buttons']}>
                  <div className={styles['list-header-button']}>
                     <div className={styles['list-header-button__icon']}>+</div>
                     <div className={styles['list-header-button__text']}>Новый раздел меню</div>
                  </div>
                  <div className={styles['list-header-button']}>
                     <div className={styles['list-header-button__icon']}>+</div>
                     <div className={styles['list-header-button__text']}>Новая позиция меню</div>
                  </div>
               </div>
            </div>
            {menuPositions.length > 0 ? (<Table data={menuPositions} columns={columns} />) : (<div>Loading...</div>)}
         </div>
      </>
   )
}

export default ListMenuPositions