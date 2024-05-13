import axios from "axios"
import { useState, useEffect } from "react"
import styles from './ListMenuSections.module.scss'
import { MenuSection } from "../../../ts/types/MenuSection"
import { createColumnHelper } from "@tanstack/react-table"
import { useNavigate } from "react-router-dom"
import Table from "../../../components/Table/Table"

function ListMenuSections() {

   const navigate = useNavigate()

   const [menuSections, setMenuSections] = useState<MenuSection[]>([])

   const columnHelper = createColumnHelper<MenuSection>()
   const columns = [
      columnHelper.accessor('id', {
         header: () => 'ID',
         cell: (info) => info.getValue()
      }),
      columnHelper.accessor('name', {
         header: () => 'Название раздела меню',
         cell: (info) => info.getValue()
      }),
      columnHelper.accessor('descr', {
         header: () => 'Описание раздела меню',
         cell: (info) => info.getValue()
      })
   ]

   useEffect(() => {
      getAllMenuSections()
   }, [])

   const deleteHandler = (id: number | string): void => {
      axios.delete('http://127.0.0.1:8080/api/menu-sections/' + id)
         .then(response => {
            console.log(response.data)
            getAllMenuSections()
         })
         .catch(error => console.log(error))
   }

   const editHandler = (id: number | string): void => {
      navigate('/admin-panel/menu-section?sectionId=' + id)
   }

   const getAllMenuSections = (): void => {
      axios.get('http://127.0.0.1:8080/api/menu-sections')
         .then(response => {
            setMenuSections(response.data)
         })
         .catch(error => console.log(error))
   }

   return (
      <>
         <div className={styles['main-area']}>
            <div className={styles['list-header']}>
               <div className={styles['title']}>
                  Список разделов меню
               </div>
               <div className={styles['list-header__buttons']}>
                  <button onClick={() => navigate('/admin-panel/menu-section')} className={styles['list-header-button']}>
                     <div className={styles['list-header-button__icon']}>+</div>
                     <div className={styles['list-header-button__text']}>Новый раздел меню</div>
                  </button>
               </div>
            </div>

            {menuSections.length > 0 ? (<Table deleteHandler={deleteHandler} editHandler={editHandler} data={menuSections}
               columns={columns} modalDeleteText='Вы точно хотите удалить раздел меню' modalEditText='Перейти на страницу редактирования раздела меню?' />)
               : (<div style={{ marginTop: '15px' }}>Результатов не было найдено</div>)}

         </div>
      </>
   )
}

export default ListMenuSections