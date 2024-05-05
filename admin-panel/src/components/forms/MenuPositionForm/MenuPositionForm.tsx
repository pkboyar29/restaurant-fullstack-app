import { useNavigate, useLocation } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import styles from './MenuPositionForm.module.scss'
import { useEffect, useState } from 'react'
import axios from 'axios'
import Modal from '../../Modal/Modal'

type FormFields = {
   name: string,
   descr: string,
   menuSection: number,
   portion: string,
   price: number,
   availability: boolean
}

interface MenuPositionFormProps {
   increaseUpdateKey: () => void
}

function MenuPositionForm({ increaseUpdateKey }: MenuPositionFormProps) {

   const navigate = useNavigate()
   const location = useLocation()

   const [menuSections, setMenuSections] = useState<any[]>([])
   const [currentMenuPosition, setCurrentMenuPosition] = useState<string | null>('')
   const [currentQueryString, setCurrentQueryString] = useState<string>(location.search)
   const [deleteModal, setDeleteModal] = useState<boolean>(false)
   const [cancelModal, setCancelModal] = useState<boolean>(false)

   const {
      register, handleSubmit, formState: { errors, isValid }, setValue
   } = useForm<FormFields>({
      mode: "onBlur"
   })

   const onSubmit = (data: FormFields) => {

      // в зависимости от того, есть query string или нет, отправлять разные запросы
      if (currentQueryString === "") {
         axios.post('http://127.0.0.1:8080/api/menu-positions', data)
            .then(response => {
               console.log(response.data)
               increaseUpdateKey()
               navigate('/admin-panel/menu-positions')
            })
            .catch(error => console.log(error))
      }
      else {
         axios.put('http://127.0.0.1:8080/api/menu-positions/' + currentMenuPosition, data, {
            headers: {
               'Content-Type': 'application/json'
            }
         })
            .then(response => {
               console.log(response.data)
               increaseUpdateKey()
               navigate('/admin-panel/menu-positions')
            })
      }
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

   useEffect(() => {
      setCurrentQueryString(location.search)

      if (location.search !== "") {
         const searchParams = new URLSearchParams(location.search)
         const positionId = searchParams.get('positionId')
         setCurrentMenuPosition(positionId)

         axios.get('http://127.0.0.1:8080/api/menu-positions/' + positionId)
            .then(response => {
               setValue('name', response.data['name'])
               setValue('descr', response.data['descr'])
               setValue('menuSection', response.data['menuSection']['id'])
               setValue('portion', response.data['portion'])
               setValue('price', response.data['price'])
               setValue('availability', response.data['availability'])
            })
            .catch(error => console.log(error))
      }

   }, [location])

   const pressModalCancel = (): void => {
      setDeleteModal(false)
      setCancelModal(false)
   }

   const pressModalDiscardChanges = (): void => {
      navigate('/admin-panel/menu-positions')
   }

   const pressModalDelete = (): void => {
      console.log("delete триггер")
      axios.delete('http://127.0.0.1:8080/api/menu-positions/' + currentMenuPosition)
         .then(response => {
            console.log(response.data)
            increaseUpdateKey()
            navigate('/admin-panel/menu-positions')
         })
         .catch(error => console.log(error.response.data))
   }

   return (
      <div className={styles['main-area']}>
         {cancelModal && <Modal modalText='Вы точно хотите отменить все изменения?' buttonConfirmText='Да' confirmHandler={pressModalDiscardChanges} cancelHandler={pressModalCancel} />}
         {deleteModal && <Modal modalText='Вы точно хотите удалить эту позицию меню?' buttonConfirmText='Удалить' confirmHandler={pressModalDelete} cancelHandler={pressModalCancel} />}
         <div className={styles['title']}>
            {currentQueryString === "" ? <>Добавить новую позицию меню</> : <>Редактировать позицию меню</>}
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
                     <select
                        className={styles['element__input']}
                        {...register('menuSection', {
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
               <input type='submit' className={styles['form__confirm']} value={currentQueryString === "" ? 'Добавить' : 'Сохранить изменения'} disabled={!isValid} />
               {currentQueryString !== "" && <button type='button' className={styles['form__cancel']} onClick={() => { setDeleteModal(true) }}>Удалить</button>}
               <button type='button' className={styles['form__cancel']} onClick={() => { setCancelModal(true) }}>Отмена</button>
            </div>

         </form>
      </div>
   )
}

export default MenuPositionForm