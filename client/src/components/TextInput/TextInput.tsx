import styles from './TextInput.module.scss'

interface TextInputProps {
   fieldName: string
   inputTitle: string
   placeholder: string
   errorMessage: string
   register: any
   validationRules: Record<string, any>
   inputProps?: React.InputHTMLAttributes<HTMLInputElement>
}

function TextInput({ fieldName, inputTitle, placeholder, errorMessage, register, validationRules, inputProps }: TextInputProps) {
   return (
      <div className={styles['input']}>
         <div className={styles['input__title']}>{inputTitle}</div>
         <input className={styles['input__input']}
            placeholder={placeholder}
            {...register(fieldName, validationRules)} // распространяем register
            {...inputProps} // распространяем inputProps на элемент input 
         />

         {errorMessage && <div className={styles['input__error']}>{errorMessage}</div>}
      </div>

   )

}

export default TextInput