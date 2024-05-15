import styles from './SelectInput.module.scss'

interface SelectOption {
   value: string,
   label: string
}

interface SelectInputProps {
   selectTitle: string
   fieldName: string
   register: any
   selectOptions: SelectOption[]
}

function SelectInput({ selectTitle, selectOptions, fieldName, register }: SelectInputProps) {

   return (
      <div className={styles['select']}>
         <div className={styles['select__title']}>{selectTitle}</div>

         <select {...register(fieldName)} className={styles['select__select']}>

            {selectOptions.map(option => (
               <option className={styles['select__option']} key={option.value} value={option.value}>
                  {option.label}
               </option>
            ))}

         </select>
      </div>
   )
}

export default SelectInput