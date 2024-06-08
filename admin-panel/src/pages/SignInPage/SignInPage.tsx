import { useNavigate } from 'react-router-dom'
import { useState } from 'react'
import { useForm } from 'react-hook-form'
import axios from 'axios'
import Cookies from 'js-cookie'

import styles from './SignInPage.module.scss'

import Modal from '../../components/Modal/Modal'
import TextInput from '../../components/TextInput/TextInput'
import Title from '../../components/Title/Title'
import Button from '../../components/Button/Button'

type SignInFields = {
   username: string,
   password: string
}

interface SignInPageProps {
   setUsername: () => void
}

function SignInPage({ setUsername }: SignInPageProps) {
   const navigate = useNavigate()
   const [modal, setModal] = useState<boolean>(false)

   const { handleSubmit, register, setError, formState: { errors } } = useForm<SignInFields>({
      mode: 'onBlur'
   })

   const onSubmit = (data: SignInFields) => {
      console.log(data)

      axios.post('http://127.0.0.1:8080/api/users/employee-sign-in', data)
         .then(response => {
            console.log(response.data)
            Cookies.set('token', response.data.token)
            setModal(true)
            setUsername()
         })
         .catch(error => {
            console.log(error)
         })
   }

   return (
      <>
         {modal && <Modal modalText='Вы успешно авторизовались' buttonConfirmText='Ок'
            confirmHandler={() => {
               setModal(false)
               navigate('/admin-panel/menu-positions')
            }}
            cancelHandler={() => {
               console.log('asd')
            }} />}

         <form className={styles['sign-in__form']} onSubmit={handleSubmit(onSubmit)}>

            <Title>Войдите в систему</Title>

            <TextInput
               inputTitle='Логин'
               placeholder='Введите логин'
               errorMessage={typeof errors.username?.message === 'string' ? errors.username.message : ''}
               inputProps={{
                  type: 'text',
                  autoComplete: 'off',
                  maxLength: 20
               }}
               fieldName='username'
               register={register}
               validationRules={{ required: 'Поле обязательно к заполнению' }} />

            <TextInput
               inputTitle='Пароль'
               placeholder='Введите пароль'
               errorMessage={typeof errors.password?.message === 'string' ? errors.password.message : ''}
               inputProps={{
                  type: 'password',
                  autoComplete: 'off',
                  maxLength: 20
               }}
               fieldName='password'
               register={register}
               validationRules={{ required: 'Поле обязательно к заполнению' }} />

            <Button text='Войти' onClick={() => console.log('')} />

         </form>
      </>
   )
}

export default SignInPage