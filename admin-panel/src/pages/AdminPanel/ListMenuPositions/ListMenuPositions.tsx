import './ListMenuPositions.scss'
import axios from 'axios'
import { useEffect } from 'react'
import Table from '../../../components/Table/Table'

function ListMenuPositions() {

   useEffect(() => {
      // получить результат запроса сразу в переменную (через присовение переменной)?
      axios.get('http://127.0.0.1:8080/api/menu-positions/')
         .then(res => res.data)
         .then(data => console.log(data))
         .catch(error => console.log(error))
   })

   return (
      <>
         <Table></Table>
      </>
   )
}

export default ListMenuPositions