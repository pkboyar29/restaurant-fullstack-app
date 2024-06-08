import styles from './MenuSectionForm.module.scss'
import { useState, useEffect } from 'react'
import { useForm } from 'react-hook-form'
import { useLocation, useNavigate } from 'react-router-dom'
import axios from 'axios'
import Modal from '../../Modal/Modal'
import Cookies from 'js-cookie'

type FormFields = {
   name: string,
   descr: string
}

function MenuSectionForm() {

   const navigate = useNavigate()
   const location = useLocation()
   const [currentMenuSection, setCurrentMenuSection] = useState<string | null>('')
   const [currentQueryString, setCurrentQueryString] = useState<string>(location.search)
   const [deleteModal, setDeleteModal] = useState<boolean>(false)
   const [cancelModal, setCancelModal] = useState<boolean>(false)

   const { register, handleSubmit, setValue, formState: { errors, isValid } } = useForm<FormFields>({
      mode: 'onBlur'
   })

   const onSubmit = (data: FormFields) => {
      if (currentQueryString === '') {
         axios.post('http://127.0.0.1:8080/api/menu-sections', data, {
            headers: {
               Authorization: `Bearer ${Cookies.get('token')}`
            }
         })
            .then(response => {
               console.log(response.data)
               navigate('/admin-panel/menu-sections')
            })
            .catch(error => console.log(error))
      }
      else {
         axios.put('http://127.0.0.1:8080/api/menu-sections/' + currentMenuSection, data, {
            headers: {
               Authorization: `Bearer ${Cookies.get('token')}`
            }
         })
            .then(response => {
               console.log(response.data)
               navigate('/admin-panel/menu-sections')
            })
            .catch(error => console.log(error))
      }
   }

   useEffect(() => {
      setCurrentQueryString(location.search)

      if (location.search !== '') {
         const searchParams = new URLSearchParams(location.search)
         const sectionId = searchParams.get('sectionId')
         setCurrentMenuSection(sectionId)

         axios.get('http://127.0.0.1:8080/api/menu-sections/' + sectionId)
            .then(response => {
               console.log(response.data)
               setValue('name', response.data['name'])
               setValue('descr', response.data['descr'])
            })
      }
   }, [location])

   const pressInModalCancel = (): void => {
      setDeleteModal(false)
      setCancelModal(false)
   }

   const pressModalDiscardChanges = (): void => {
      navigate('/admin-panel/menu-sections')
   }

   const pressModalDelete = (): void => {
      axios.delete('http://127.0.0.1:8080/api/menu-sections/' + currentMenuSection, {
         headers: {
            Authorization: `Bearer ${Cookies.get('token')}`
         }
      })
         .then(response => {
            console.log(response.data)
            navigate('/admin-panel/menu-sections')
         })
         .catch(error => console.log(error))
   }

   return (
      <div className={styles['main-area']}>
         {cancelModal && <Modal modalText='Вы точно хотите отменить все изменения?' buttonConfirmText='Да' confirmHandler={pressModalDiscardChanges} cancelHandler={pressInModalCancel} />}
         {deleteModal && <Modal modalText='Вы точно хотите удалить этот раздел меню?' buttonConfirmText='Удалить' confirmHandler={pressModalDelete} cancelHandler={pressInModalCancel} />}
         <div className={styles['title']}>
            {currentQueryString === "" ? <>Добавить новый раздел меню</> : <>Редактировать раздел меню</>}
         </div>

         <form onSubmit={handleSubmit(onSubmit)} className={styles["form"]}>

            <div className={styles['element']}>
               <div className={styles['element__title']}>Название раздела меню</div>
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
               <div className={styles['element__title']}>Описание раздела меню</div>
               <textarea className={`${styles['element__input']} ${styles['element__descr']}`} placeholder='Введите описание здесь'
                  {...register('descr', {
                     maxLength: {
                        value: 400,
                        message: "Максимальное количество символов: 400"
                     }
                  })} />
               <div className={'element__error'}>{errors?.descr && <p>{errors?.descr?.message || 'Error!'}</p>}</div>
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

export default MenuSectionForm