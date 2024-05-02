import { useState } from 'react'
import './Switch.module.scss'

interface SwitchProps {
   initialValue?: boolean
}

function Switch({ initialValue }: SwitchProps) {

   const [isToggled, setIsToggled] = useState(initialValue)

   return (
      <label>
         <input type="checkbox" checked={isToggled} />
      </label>
   )
}

export default Switch