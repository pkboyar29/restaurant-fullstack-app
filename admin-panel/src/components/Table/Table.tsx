import { flexRender, getCoreRowModel, useReactTable } from '@tanstack/react-table'
import { useEffect } from 'react'
import styles from './Table.module.scss'

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
               </tr>
            ))}
         </tbody>
      </table>
   )
}

export default Table