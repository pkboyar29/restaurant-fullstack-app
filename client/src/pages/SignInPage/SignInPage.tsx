import styles from './SignInPage.module.scss'
import { useForm } from 'react-hook-form'
import { Link, useNavigate } from 'react-router-dom'
import axios from 'axios'
import { Client } from '../../ts/types/Client'

import Title from '../../components/Title/Title'
import TextInput from '../../components/TextInput/TextInput'
import Button from '../../components/Button/Button'

type ClientsSignInFields = {
   username: string,
   password: string
}

interface SignInProps {
   setCurrentClient: (currentClient: Client) => void
}

function SignInPage({ setCurrentClient }: SignInProps) {

   const navigate = useNavigate()

   const { handleSubmit, register, setError, formState: { errors } } = useForm<ClientsSignInFields>({
      mode: 'onBlur'
   })

   const onSubmit = (data: ClientsSignInFields) => {
      axios.post(import.meta.env.VITE_BACKEND_URL + '/api/clients/sign-in', data)
         .then(response => {
            const client: Client = response.data
            setCurrentClient(client)
            localStorage.setItem('currentClient', JSON.stringify(client))
            alert('Вы успешно авторизовались!')
            navigate('/menu')
         })
         .catch(error => {
            switch (error.response.status) {
               case 401:
                  setError('password', { type: 'custom', message: 'Пароль неверный' })
                  break
               case 404:
                  setError('username', { type: 'custom', message: 'Пользователя с таким логином не существует' })
                  break
               default:
                  break
            }
         })
   }

   return (
      <>
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