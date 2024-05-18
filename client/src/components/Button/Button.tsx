import styles from './Button.module.scss'

interface ButtonProps {
   text: string,
   onClick: React.MouseEventHandler<HTMLButtonElement>,
   buttonProps?: React.ButtonHTMLAttributes<HTMLButtonElement>
}

function Button({ text, onClick, buttonProps }: ButtonProps) {
   return (
      <button {...buttonProps} className={styles['button']} onClick={onClick}>
         {text}
      </button>
   )
}

export default Button