import styles from './SignUpPage.module.scss'
import { useForm } from 'react-hook-form'
import { Link, useNavigate } from 'react-router-dom'
import validator from 'email-validator'
import axios from 'axios'
import { Client } from '../../ts/types/Client'
import { useState } from 'react'
import Cookies from 'js-cookie'

import Modal from '../../components/Modal/Modal'
import Title from '../../components/Title/Title'
import TextInput from '../../components/TextInput/TextInput'
import SelectInput from '../../components/SelectInput/SelectInput'
import Button from '../../components/Button/Button'

type ClientSignUpFields = {
   username: string,
   firstName: string,
   lastName: string,
   patronymic: string,
   gender: string,
   phone: string,
   email: string,
   password: string,
   repeatPassword: string | null
}

interface SignUpProps {
   setCurrentClient: (currentClient: Client) => void
}

function SignUpPage({ setCurrentClient }: SignUpProps) {

   const navigate = useNavigate()
   const [modal, setModal] = useState<boolean>(false)

   const { register, handleSubmit, getValues, setError, formState: { errors } } = useForm<ClientSignUpFields>({
      mode: 'onBlur'
   })

   const onlyLettersRegex = /^[A-Za-zА-Яа-яЁё]+$/
   const phoneRegex = /^\+?\d{1,3}?[-.\s]?\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{4}$/
   const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$/
   const usernameRegex = /^[a-zA-Z][a-zA-Z0-9]+$/

   const onSubmit = (data: ClientSignUpFields) => {
      axios.post(import.meta.env.VITE_BACKEND_URL + '/api/users/client-sign-up', data)
         .then(response => {
            console.log(response.data)
            Cookies.set('token', response.data.token)

            axios.get(import.meta.env.VITE_BACKEND_URL + '/api/users/get-client-data', {
               headers: {
                  'Authorization': `Bearer ${Cookies.get('token')}`
               }
            })
               .then(response => {
                  const client: Client = response.data
                  setCurrentClient(client)
                  setModal(true)
               })
               .catch(error => console.log(error))
         })
         .catch(error => {
            if (error.response) {
               switch (error.response.data.errorCode) {
                  case 'DUPLICATE_USERNAME':
                     setError('username', { type: 'custom', message: 'Пользователь с таким логином уже существует' })
                     break
                  case 'DUPLICATE_PHONE':
                     setError('phone', { type: 'custom', message: 'Пользователь с таким телефоном уже существует' })
                     break
                  case 'DUPLICATE_EMAIL':
                     setError('email', { type: 'custom', message: 'Пользователь с такой электронной почтой уже существует' })
                     break
               }
            }
         })
   }

   return (
      <>
         {modal && <Modal modalText='Вы успешно зарегестрировались!' buttonConfirmText='Ок' confirmHandler={() => {
            setModal(false)
            navigate('/menu')
         }} />}

         <Title>Регистрация</Title>

         <form autoComplete='off' className={styles['form']} onSubmit={handleSubmit(onSubmit)}>

            <TextInput
               inputTitle='Имя'
               placeholder='Введите имя'
               errorMessage={typeof errors.firstName?.message === 'string' ? errors.firstName.message : ''}
               inputProps={{
                  type: 'text',
                  autoComplete: 'off',
                  maxLength: 30
               }}
               fieldName='firstName'
               register={register}
               validationRules={{
                  required: 'Поле обязательно к заполнению',
                  minLength: {
                     value: 2,
                     message: 'Минимальное количество символов: 2'
                  },
                  maxLength: {
                     value: 30,
                     message: 'Максимальное количество символов: 30'
                  },
                  pattern: {
                     value: onlyLettersRegex,
                     message: 'Допускаются только буквы'
                  }
               }} />

            <TextInput
               inputTitle='Фамилия'
               placeholder='Введите фамилию'
               errorMessage={typeof errors.lastName?.message === 'string' ? errors.lastName.message : ''}
               inputProps={{
                  type: 'text',
                  autoComplete: 'off',
                  maxLength: 30
               }}
               fieldName='lastName'
               register={register}
               validationRules={{
                  required: 'Поле обязательно к заполнению',
                  minLength: {
                     value: 2,
                     message: 'Минимальное количество символов: 2'
                  },
                  maxLength: {
                     value: 30,
                     message: 'Максимальное количество символов: 30'
                  },
                  pattern: {
                     value: onlyLettersRegex,
                     message: 'Допускаются только буквы'
                  }
               }} />

            <TextInput
               inputTitle='Отчество'
               placeholder='Введите отчество'
               errorMessage={typeof errors.patronymic?.message === 'string' ? errors.patronymic.message : ''}
               inputProps={{
                  type: 'text',
                  autoComplete: 'off',
                  maxLength: 30
               }}
               fieldName='patronymic'
               register={register}
               validationRules={{
                  required: 'Поле обязательно к заполнению',
                  minLength: {
                     value: 2,
                     message: 'Минимальное количество символов: 2'
                  },
                  maxLength: {
                     value: 30,
                     message: 'Максимальное количество символов: 30'
                  },
                  pattern: {
                     value: onlyLettersRegex,
                     message: 'Допускаются только буквы'
                  }
               }} />

            <TextInput
               inputTitle='Телефон'
               placeholder='Введите телефон'
               errorMessage={typeof errors.phone?.message === 'string' ? errors.phone.message : ''}
               inputProps={{
                  type: 'text',
                  autoComplete: 'off'
               }}
               fieldName='phone'
               register={register}
               validationRules={{
                  required: 'Поле обязательно к заполнению',
                  pattern: {
                     value: phoneRegex,
                     message: 'Некорректный формат телефона'
                  }
               }} />

            <TextInput
               inputTitle='Электронная почта'
               placeholder='Введите электронную почту'
               errorMessage={typeof errors.email?.message === 'string' ? errors.email.message : ''}
               inputProps={{
                  type: 'text',
                  autoComplete: 'off',
                  maxLength: 60
               }}
               fieldName='email'
               register={register}
               validationRules={{
                  required: 'Поле обязательно к заполнению',
                  maxLength: {
                     value: 60,
                     message: 'Максимальное количество символов: 60'
                  },
                  validate: (value: string) => validator.validate(value) || 'Неверный формат email'
               }} />

            <SelectInput
               selectTitle='Пол'
               selectOptions={[
                  {
                     'value': 'муж',
                     'label': 'мужской'
                  },
                  {
                     'value': 'жен',
                     'label': 'женский'
                  }
               ]}
               fieldName='gender'
               register={register}
            />

            <TextInput
               inputTitle='Логин'
               placeholder='Придумайте логин'
               errorMessage={typeof errors.username?.message === 'string' ? errors.username.message : ''}
               inputProps={{
                  type: 'text',
                  autoComplete: 'off',
                  maxLength: 20
               }}
               fieldName='username'
               register={register}
               validationRules={{
                  required: 'Поле обязательно к заполнению',
                  minLength: {
                     value: 4,
                     message: "Минимальное количество символов: 4"
                  },
                  maxLength: {
                     value: 20,
                     message: "Максимальное количество символов: 20"
                  },
                  pattern: {
                     value: usernameRegex,
                     message: 'Допускаются только латинские буквы и цифры. Логин должен начинаться с латинской буквы'
                  }
               }} />

            <TextInput
               inputTitle='Пароль'
               placeholder='Придумайте пароль'
               errorMessage={typeof errors.password?.message === 'string' ? errors.password.message : ''}
               inputProps={{
                  type: 'password',
                  autoComplete: 'off',
                  maxLength: 20
               }}
               fieldName='password'
               register={register}
               validationRules={{
                  required: 'Поле обязательно к заполнению',
                  minLength: {
                     value: 8,
                     message: 'Минимальная длина пароля: 8 символов'
                  },
                  maxLength: {
                     value: 20,
                     message: 'Максимальная длина пароля: 20 символов'
                  },
                  pattern: {
                     value: passwordRegex,
                     message: 'Пароль должен содержать по крайней мере одну прописную букву, одну строчную букву, одну цифру и один специальный символ (!@#$%^&*)'
                  }
               }} />

            <TextInput
               inputTitle='Подтверждение пароля'
               placeholder='Введите пароль повторно'
               errorMessage={typeof errors.repeatPassword?.message === 'string' ? errors.repeatPassword.message : ''}
               inputProps={{
                  type: 'password',
                  autoComplete: 'off'
               }}
               fieldName='repeatPassword'
               register={register}
               validationRules={{
                  validate: (value: string) => getValues('password') === value || "Пароли не совпадают"
               }} />

            <Link to='/sign-in'>Уже зарегестрированы?</Link>

            <Button text='Зарегестрироваться' onClick={() => console.log('hello')} />

         </form>
      </>
   )
}

export default SignUpPage