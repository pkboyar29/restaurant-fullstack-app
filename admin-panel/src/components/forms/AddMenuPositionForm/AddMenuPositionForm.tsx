import { useNavigate } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import styles from './AddMenuPositionForm.module.scss'
import { useEffect, useState } from 'react'
import axios from 'axios'

type FormFields = {
   name: string,
   descr: string,
   menuSection: number,
   portion: string,
   price: number,
   availability: boolean
}

interface AddMenuPositionFormProps {
   increaseUpdateKey: () => void
}

function AddMenuPositionForm({ increaseUpdateKey }: AddMenuPositionFormProps) {

   const navigate = useNavigate()
   const onCancelButtonClick = () => {
      navigate('/admin-panel/menu-positions')
   }

   const [menuSections, setMenuSections] = useState<any[]>([])

   const {
      register, handleSubmit, setError, formState: { errors, isValid }
   } = useForm<FormFields>({
      mode: "onBlur"
   })

   const onSubmit = (data: FormFields) => {
      console.log(data)

      if (!data.menuSection) {
         setError('menuSection', {
            type: 'manual',
            message: 'Поле обязательно к заполнению'
         })
      }

      axios.post('http://127.0.0.1:8080/api/menu-positions', data)
         .then(response => {
            increaseUpdateKey()
            navigate('/admin-panel/menu-positions')
            console.log(response.data)
         })
         .catch(error => console.log(error))
   }

   const getAllMenuSections = (): void => {
      axios.get('http://127.0.0.1:8080/api/menu-sections')
         .then(response => {
            console.log(response.data)
            setMenuSections(response.data)
         })
         .catch(error => console.log(error))
   }

   useEffect(() => {
      getAllMenuSections()
   }, [])

   return (
      <div className={styles['main-area']}>

         <div className={styles['title']}>
            Добавить новую позицию меню
         </div>

         <form onSubmit={handleSubmit(onSubmit)} className={styles["form"]}>

            <div className={styles['form__columns']}>
               <div className={styles['form__column__first']}>

                  <div className={styles['element']}>
                     <div className={styles['element__title']}>Название позиции меню</div>
                     <input className={styles['element__input']} type="text" placeholder='Введите название здесь'
                        {...register('name', {
                           required: 'Поле обязательно к заполнению',
                           minLength: {
                              value: 4,
                              message: "Минимальное количество символов: 4"
                           },
                           maxLength: {
                              value: 100,
                              message: "Максимальное количество символов: 100"
                           }
                        })} />
                     <div className={'element__error'}>{errors?.name && <p>{errors?.name?.message || 'Error!'}</p>}</div>
                  </div>

                  <div className={styles['element']}>
                     <div className={styles['element__title']}>Описание позиции меню</div>
                     <textarea className={`${styles['element__input']} ${styles['element__descr']}`} placeholder='Введите описание здесь'
                        {...register('descr', {
                           maxLength: {
                              value: 400,
                              message: "Максимальное количество символов: 400"
                           }
                        })} />
                     <div className={'element__error'}>{errors?.descr && <p>{errors?.descr?.message || 'Error!'}</p>}</div>
                  </div>

                  <div className={styles['element']}>
                     <div className={styles['element__title']}>Раздел меню</div>
                     <select className={styles['element__input']} {...register('menuSection', {
                        required: 'Пожалуйста, выберите раздел меню!'
                     })}>
                        <option value=''>Выбрать раздел меню</option>
                        {menuSections.map((section) => (
                           <option key={section.id} value={section.id}>{section.name}</option>
                        ))}
                     </select>
                     <div className={'element__error'}>{errors?.menuSection && <p>{errors?.menuSection?.message}</p>}</div>
                  </div>

                  <div className={styles['element']}>
                     <div className={styles['element__title']}>Порция</div>
                     <input className={styles['element__input']} type='text' placeholder='Введите порцию здесь'
                        {...register('portion', {
                           required: 'Поле обязательно к заполнению',
                           maxLength: {
                              value: 40,
                              message: "Максимальное количество символов: 40"
                           }
                        })} />
                     <div className={'element__error'}>{errors?.portion && <p>{errors?.portion?.message}</p>}</div>
                  </div>

                  <div className={styles['element']}>
                     <div className={styles['element__title']}>Цена (в ₽)</div>
                     <input className={styles['element__input']} type='number' placeholder='Введите цену здесь'
                        {...register('price', {
                           required: 'Поле обязательно к заполнению'
                        })} />
                     <div className={'element__error'}>{errors?.price && <p>{errors?.price?.message}</p>}</div>
                  </div>

                  <div className={styles['element']}>
                     <div className={styles['element__title']}>Доступность</div>
                     <input className={styles['element__checkbox']} type='checkbox'
                        {...register('availability')} />
                  </div>

               </div>
               <div className={styles['form__column']}>Колонка 2</div>
               <div className={styles['form__column']}>Колонка 3</div>
            </div>

            <div className={styles['form__buttons']}>
               <input type='submit' className={styles['form__confirm']} value='Добавить' disabled={!isValid} />
               <button className={styles['form__cancel']} onClick={onCancelButtonClick}>Отмена</button>
            </div>

         </form>
      </div>
   )
}

export default AddMenuPositionForm