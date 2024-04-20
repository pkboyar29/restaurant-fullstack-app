import './ListMenuPositions.scss'
import axios from 'axios'
import { useEffect } from 'react'

function ListMenuPositions() {
   
   useEffect(() => {
      axios.get('http://127.0.0.1:8080/api/menu-positions/')
      .then(res => res.data)
      .then(data => console.log(data))
   })
   
   return (
      <>
         Список позиций меню
      </>
   )
}

export default ListMenuPositions