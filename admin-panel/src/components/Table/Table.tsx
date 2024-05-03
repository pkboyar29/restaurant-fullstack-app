import { flexRender, getCoreRowModel, useReactTable } from '@tanstack/react-table'
import { useEffect, useState } from 'react'

import styles from './Table.module.scss'
import editButtonIcon from '../../assets/edit-button.svg'
import deleteButtonIcon from '../../assets/delete-button.svg'
import DeleteModal from '../Modal/Modal'

interface TableProps {
   data: any[],
   columns: any[],
   deleteHandler: (id: number | string) => void
}

function Table({ data, columns, deleteHandler }: TableProps) {

   // useEffect(() => {
   //    console.log(data)
   //    console.log(columns)
   // }, [])

   const table = useReactTable({
      data,
      columns,
      debugTable: true,
      getCoreRowModel: getCoreRowModel()
   })

   const [modal, setModal] = useState<boolean>(false)

   const [selectedCell, setSelectedCell] = useState<number>(0)

   const pressDelete = (): void => {
      deleteHandler(selectedCell)
      setModal(false)
      setSelectedCell(0)
   }

   const pressCancel = (): void => {
      setModal(false)
      setSelectedCell(0)
   }

   return (
      <table className={styles['table']}>
         {modal && <DeleteModal modalText='Вы точно хотите удалить эту позицию меню?' buttonConfirmText='Удалить' confirmHandler={pressDelete} cancelHandler={pressCancel} />}
         <thead className={styles['table-header']}>
            {table.getHeaderGroups().map((headerGroup) => (
               <tr key={headerGroup.id} className={styles['table-header__row']}>
                  {headerGroup.headers.map((header) => (
                     <th key={header.id} className={styles['table-header__cell']}>
                        {flexRender(header.column.columnDef.header, header.getContext())}
                     </th>
                  ))}
               </tr>
            ))}
         </thead>
         <tbody className={styles['table-body']}>
            {table.getRowModel().rows.map((row) => (

               <tr key={row.id} className={styles['table-body__row']}>
                  {row.getVisibleCells().map((cell) => (
                     <td key={cell.id} className={styles['table-body__cell']}>
                        {flexRender(cell.column.columnDef.cell, cell.getContext())}
                     </td>
                  ))}
                  <td className={styles['table-body__icons']}>
                     <button className={styles['table-body__icon']}>
                        <img src={editButtonIcon} alt="Edit button" />
                     </button>
                     <button onClick={() => {
                        setSelectedCell(row.getVisibleCells()[0].getValue() as number)
                        setModal(true)
                     }} className={styles['table-body__icon']}>
                        <img src={deleteButtonIcon} alt="Delete button" />
                     </button>
                  </td>
               </tr>

            ))}
         </tbody>
      </table>
   )
}

export default Table