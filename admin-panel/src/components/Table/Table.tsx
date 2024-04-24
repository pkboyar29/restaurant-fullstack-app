import { flexRender, getCoreRowModel, useReactTable } from '@tanstack/react-table'
import { useEffect } from 'react'
import styles from './Table.module.scss'
import editButtonIcon from '../../assets/edit-button.svg'
import deleteButtonIcon from '../../assets/delete-button.svg'

interface TableProps {
   data: any[],
   columns: any[]
}

function Table({ data, columns }: TableProps) {

   useEffect(() => {
      console.log(data)
      console.log(columns)
   }, [])

   const table = useReactTable({
      data,
      columns,
      debugTable: true,
      getCoreRowModel: getCoreRowModel()
   })

   return (
      <table className={styles['table']}>
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
                  {/* добавить сюда две иконки */}
                  <td className={styles['table-body__icons']}>
                     <div className={styles['table-body__icon']}>
                        <img src={editButtonIcon} alt="Edit button" />
                     </div>
                     <div className={styles['table-body__icon']}>
                        <img src={deleteButtonIcon} alt="Delete button" />
                     </div>
                  </td>
               </tr>
            ))}
         </tbody>
      </table>
   )
}

export default Table