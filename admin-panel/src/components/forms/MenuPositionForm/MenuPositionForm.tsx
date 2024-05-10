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
   image1: FileList
   image2: FileList
   image3: FileList
   image4: FileList
}

type FormImageFile = {
   fileName: string
   fileURL: string
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
   const [image1, setImage1] = useState<FormImageFile | null>(null)
   const [image2, setImage2] = useState<FormImageFile | null>(null)
   const [image3, setImage3] = useState<FormImageFile | null>(null)
   const [image4, setImage4] = useState<FormImageFile | null>(null)

   const {
      register, handleSubmit, formState: { errors, isValid }, setValue
   } = useForm<FormFields>({
      mode: "onBlur"
   })

   const onSubmit = (data: FormFields) => {

      const formData = new FormData()
      formData.append('name', data.name)
      formData.append('descr', data.descr)
      formData.append('menuSection', data.menuSection.toString())
      formData.append('portion', data.portion)
      formData.append('price', data.price.toString())
      formData.append('availability', data.availability.toString())
      if (data.image1.length > 0) {
         formData.append('image1', data.image1[0])
      }
      if (data.image2.length > 0) {
         formData.append('image2', data.image2[0])
      }
      if (data.image3.length > 0) {
         formData.append('image3', data.image3[0])
      }
      if (data.image4.length > 0) {
         formData.append('image4', data.image4[0])
      }

      formData.forEach((value, key) => {
         console.log(key, value)
      })

      if (currentQueryString === "") {
         axios.post('http://127.0.0.1:8080/api/menu-positions', formData, {
            headers: {
               'Content-Type': 'multipart/form-data'
            }
         })
            .then(response => {
               console.log(response.data)
               increaseUpdateKey()
               navigate('/admin-panel/menu-positions')
            })
            .catch(error => console.log(error))
      }
      else {
         axios.put('http://127.0.0.1:8080/api/menu-positions/' + currentMenuPosition, formData)
            .then(response => {
               console.log(response.data)
               increaseUpdateKey()
               navigate('/admin-panel/menu-positions')
            })
            .catch(error => console.log(error))
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

   const loadImageAsFile = (imageURL: string): Promise<File> => {
      return new Promise((resolve, reject) => {
         axios.get(imageURL, { responseType: 'blob' })
            .then(
               response => {
                  const parts = imageURL.split('/')
                  const fileName = parts[parts.length - 1]

                  const file = new File([response.data], fileName)
                  resolve(file)
               }
            )
            .catch(error => {
               reject(error)
            })
      })
   }

   const deleteImageHandler = (orderNumber: number) => {
      switch (orderNumber) {
         case 1:
            setImage1(null)
            setValue('image1', [] as unknown as FileList)
            break
         case 2:
            setImage2(null)
            setValue('image2', [] as unknown as FileList)
            break
         case 3:
            setImage3(null)
            setValue('image3', [] as unknown as FileList)
            break
         case 4:
            setImage4(null)
            setValue('image4', [] as unknown as FileList)
            break
      }
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
               console.log(response.data)
               setValue('name', response.data['name'])
               setValue('descr', response.data['descr'])
               setValue('menuSection', response.data['menuSection']['id'])
               setValue('portion', response.data['portion'])
               setValue('price', response.data['price'])
               setValue('availability', response.data['availability'])

               // устанавливаем изображения
               if (response.data['image1'] !== null) {
                  loadImageAsFile(response.data['image1'])
                     .then(file => {
                        setValue('image1', [file] as unknown as FileList)

                        const image1: FormImageFile = {
                           fileURL: response.data['image1'],
                           fileName: response.data['image1'].split('/')[response.data['image1'].split('/').length - 1]
                        };
                        setImage1(image1)
                     })
                     .catch(error => console.log(error))
               }
               if (response.data['image2'] !== null) {
                  loadImageAsFile(response.data['image2'])
                     .then(file => {
                        setValue('image2', [file] as unknown as FileList)

                        const image2: FormImageFile = {
                           fileURL: response.data['image2'],
                           fileName: response.data['image2'].split('/')[response.data['image2'].split('/').length - 1]
                        };
                        setImage2(image2)
                     })
                     .catch(error => console.log(error))
               }
               if (response.data['image3'] !== null) {
                  loadImageAsFile(response.data['image3'])
                     .then(file => {
                        setValue('image3', [file] as unknown as FileList)

                        const image3: FormImageFile = {
                           fileURL: response.data['image3'],
                           fileName: response.data['image3'].split('/')[response.data['image3'].split('/').length - 1]
                        };
                        setImage3(image3)
                     })
                     .catch(error => console.log(error))
               }
               if (response.data['image4'] !== null) {
                  loadImageAsFile(response.data['image4'])
                     .then(file => {
                        setValue('image4', [file] as unknown as FileList)

                        const image4: FormImageFile = {
                           fileURL: response.data['image4'],
                           fileName: response.data['image4'].split('/')[response.data['image4'].split('/').length - 1]
                        };
                        setImage4(image4)
                     })
                     .catch(error => console.log(error))
               }
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
                     <input className={styles['element__input']} type='number' placeholder='Введите цену здесь' min='0' max='1000000'
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
               <div className={styles['form__column']}>
                  <div className={styles['element__title']}>Галерея позиции меню</div>

                  <div className={styles['images']}>
                     {image1
                        ? <div className={styles['images__filled']}>
                           <img src={image1.fileURL} alt='' className={styles['filled__image']} />
                           <div className={styles['filled__text']}>{image1.fileName}</div>
                           <button onClick={() => deleteImageHandler(1)} type='button'><img src='/img/delete-button.svg' alt='' /></button>
                        </div>
                        : <div className={styles['images__notfilled']}>
                           <input className={styles['notfilled__input']} type='file' accept='image/*' {...register('image1')} />
                        </div>}
                     {image2
                        ? <div className={styles['images__filled']}>
                           <img src={image2.fileURL} alt='' className={styles['filled__image']} />
                           <div className={styles['filled__text']}>{image2.fileName}</div>
                           <button onClick={() => deleteImageHandler(2)} type='button'><img src='/img/delete-button.svg' alt='' /></button>
                        </div>
                        : <div className={styles['images__notfilled']}>
                           <input className={styles['notfilled__input']} type='file' accept='image/*' {...register('image2')} />
                        </div>}
                     {image3
                        ? <div className={styles['images__filled']}>
                           <img src={image3.fileURL} alt='' className={styles['filled__image']} />
                           <div className={styles['filled__text']}>{image3.fileName}</div>
                           <button onClick={() => deleteImageHandler(3)} type='button'><img src='/img/delete-button.svg' alt='' /></button>
                        </div>
                        : <div className={styles['images__notfilled']}>
                           <input className={styles['notfilled__input']} type='file' accept='image/*' {...register('image3')} />
                        </div>}
                     {image4
                        ? <div className={styles['images__filled']}>
                           <img src={image4.fileURL} alt='' className={styles['filled__image']} />
                           <div className={styles['filled__text']}>{image4.fileName}</div>
                           <button onClick={() => deleteImageHandler(4)} type='button'><img src='/img/delete-button.svg' alt='' /></button>
                        </div>
                        : <div className={styles['images__notfilled']}>
                           <input className={styles['notfilled__input']} type='file' accept='image/*' {...register('image4')} />
                        </div>}
                  </div>

               </div>
               <div className={styles['form__column']}>

                  {/* <div onDrop={handleDrop} onDragOver={handleDragOver} className={styles['image']}>
                        <div className={styles['image__before-dragdrop']}>
                           <div className={styles['image__before-dragdrop__img']}>
                              <img src='/img/before__image.svg' alt='' />
                           </div>
                           <div className={styles['image__before-dragdrop__text']}>Drop your imager here, or browse Jpeg, png are allowed</div>
                        </div>
                     </div>

                     <div className={styles['image']}>

                     </div>

                     <div className={styles['image']}>

                     </div>
                     <div className={styles['image']}>

                     </div> */}

               </div>
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