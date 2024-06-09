import styles from './SignInPage.module.scss'
import { useForm } from 'react-hook-form'
import { Link, useNavigate } from 'react-router-dom'
import axios from 'axios'
import { Client } from '../../ts/types/Client'
import { useState } from 'react'
import Cookies from 'js-cookie'

import Title from '../../components/Title/Title'
import TextInput from '../../components/TextInput/TextInput'
import Button from '../../components/Button/Button'
import Modal from '../../components/Modal/Modal'

type ClientSignInFields = {
   username: string,
   password: string
}

interface SignInProps {
   setCurrentClient: (currentClient: Client) => void
}

function SignInPage({ setCurrentClient }: SignInProps) {

   const navigate = useNavigate()
   const [modal, setModal] = useState<boolean>(false)

   const { handleSubmit, register, setError, formState: { errors } } = useForm<ClientSignInFields>({
      mode: 'onBlur'
   })

   const onSubmit = (data: ClientSignInFields) => {
      axios.post(import.meta.env.VITE_BACKEND_URL + '/api/users/client-sign-in', data)
         .then(response => {
            console.log(response.data)
            Cookies.set('clientToken', response.data.token)

            axios.get(import.meta.env.VITE_BACKEND_URL + '/api/users/get-client-data', {
               headers: {
                  'Authorization': `Bearer ${Cookies.get('clientToken')}`
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
            switch (error.response.status) {
               case 401:
                  setError('username', { type: 'custom', message: 'Клиента с таким логином не существует' })
                  break
               case 409:
                  setError('password', { type: 'custom', message: 'Учетные данные пользователя неверны' })
                  break
               default:
                  break
            }
         })
   }

   return (
      <>

         {modal && <Modal modalText='Вы успешно авторизовались!' buttonConfirmText='Ок' confirmHandler={() => {
            setModal(false)
            navigate('/menu')
         }} />}

         <Title>Авторизация</Title>

         <form autoComplete='off' className={styles['form']} onSubmit={handleSubmit(onSubmit)}>

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

            <Link to='/sign-up'>Еще не зарегестрированы?</Link>

            <Button text='Войти' onClick={() => console.log('')} />

         </form>
      </>
   )
}

export default SignInPage