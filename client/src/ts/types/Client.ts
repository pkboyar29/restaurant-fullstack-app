import { OrderDiscount } from './OrderDiscount'

export type Client = {
   username: string,
   phone: string,
   email: string,
   firstName: string,
   lastName: string,
   patronymic: string,
   orderDiscount: OrderDiscount,
   numberOrders: number
}