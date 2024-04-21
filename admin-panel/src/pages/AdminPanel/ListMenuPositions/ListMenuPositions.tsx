import './ListMenuPositions.scss'
import axios from 'axios'
import { useEffect, useState } from 'react'
import Table from '../../../components/Table/Table'
import { MenuPosition } from '../../../ts/MenuPosition'
import { createColumnHelper } from '@tanstack/react-table'

function ListMenuPositions() {

   const [menuPositions, setMenuPositions] = useState<MenuPosition[]>([])

   const columnHelper = createColumnHelper<MenuPosition>()

   const columns = [
      columnHelper.accessor('id', {
         header: () => 'ID',
         cell: (info) => info.getValue()
      }),
      columnHelper.accessor('name', {
         header: () => 'Название',
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
   ]

   useEffect(() => {
      // получить результат запроса сразу в переменную (через присовение переменной)?
      axios.get('http://127.0.0.1:8080/api/menu-positions/')
         .then(response => setMenuPositions(response.data))
         .catch(error => console.log(error))
   }, [])

   return (
      <>
         <div className="main-area">
            <div className="list-header">
               <div className="title">
                  Список позиций меню
               </div>

               <div className="list-header__buttons">
                  <div className="list-header-button">
                     <div className="list-header-button__icon">+</div>
                     <div className="list-header-button__text">Новый раздел меню</div>
                  </div>
                  <div className="list-header-button">
                     <div className="list-header-button__icon">+</div>
                     <div className="list-header-button__text">Новая позиция меню</div>
                  </div>
               </div>
            </div>
            {menuPositions.length > 0 ? (<Table data={menuPositions} columns={columns} />) : (<div>Loading...</div>)}
         </div>
      </>
   )
}

export default ListMenuPositions