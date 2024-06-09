import { flexRender, getCoreRowModel, useReactTable } from '@tanstack/react-table'
import { useEffect, useState } from 'react'

import styles from './Table.module.scss'
import editButtonIcon from '../../assets/edit-button.svg'
import deleteButtonIcon from '../../assets/delete-button.svg'
import Modal from '../Modal/Modal'

interface TableProps {
   data: any[],
   columns: any[],
   deleteHandler: (id: number | string) => void,
   editHandler: (id: number | string) => void,
   modalDeleteText: string,
   modalEditText: string
}

function Table({ data, columns, deleteHandler, editHandler, modalDeleteText, modalEditText }: TableProps) {
   const table = useReactTable({
      data,
      columns,
      debugTable: true,
      initialState: {
         columnVisibility: {
            id: false // hide this column by default
         }
      },
      getCoreRowModel: getCoreRowModel()
   })

   const [deleteModal, setDeleteModal] = useState<boolean>(false)
   const [editModal, setEditModal] = useState<boolean>(false)

   const [selectedCell, setSelectedCell] = useState<number>(0)

   const pressModalDelete = (): void => {
      deleteHandler(selectedCell)
      setDeleteModal(false)
      setSelectedCell(0)
   }

   const pressModalEdit = (): void => {
      editHandler(selectedCell)
      setEditModal(false)
      setSelectedCell(0)
   }

   const pressModalCancel = (): void => {
      setDeleteModal(false)
      setEditModal(false)
      setSelectedCell(0)
   }

   return (
      <div className={styles['table__container']}>
         <table className={styles['table']}>
            {deleteModal && <Modal modalText={modalDeleteText} buttonConfirmText='Удалить' confirmHandler={pressModalDelete} cancelHandler={pressModalCancel} />}
            {editModal && <Modal modalText={modalEditText} buttonConfirmText='Да' confirmHandler={pressModalEdit} cancelHandler={pressModalCancel} />}
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
                        <button onClick={() => {
                           setSelectedCell(row.getAllCells()[0].getValue() as number)
                           setEditModal(true)
                        }} className={styles['table-body__icon']}>
                           <img src={editButtonIcon} alt="Edit button" />
                        </button>
                        <button onClick={() => {
                           setSelectedCell(row.getAllCells()[0].getValue() as number)
                           setDeleteModal(true)
                        }} className={styles['table-body__icon']}>
                           <img src={deleteButtonIcon} alt="Delete button" />
                        </button>
                     </td>
                  </tr>

               ))}
            </tbody>
         </table>
      </div>
   )
}

export default Table