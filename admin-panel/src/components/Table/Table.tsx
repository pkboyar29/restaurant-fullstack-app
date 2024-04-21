import { flexRender, getCoreRowModel, useReactTable } from '@tanstack/react-table'
import { useEffect } from 'react'
import './Table.scss'

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
      <table className="table">
         <thead className="table-header">
            {table.getHeaderGroups().map((headerGroup) => (
               <tr key={headerGroup.id} className="table-header__row">
                  {headerGroup.headers.map((header) => (
                     <th key={header.id} className='table-header__cell'>
                        {flexRender(header.column.columnDef.header, header.getContext())}
                     </th>
                  ))}
               </tr>
            ))}
         </thead>
         <tbody className="table-body">
            {table.getRowModel().rows.map((row) => (
               <tr key={row.id} className="table-body__row">
                  {row.getVisibleCells().map((cell) => (
                     <td key={cell.id} className="table-body__cell">
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