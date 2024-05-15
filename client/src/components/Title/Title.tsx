import { ReactNode } from 'react'
import styles from './Title.module.scss'

interface TitleProps {
   children: ReactNode
}

function Title({ children }: TitleProps) {
   return (
      <div className={styles['title']}>
         {children}
      </div>
   )
}

export default Title