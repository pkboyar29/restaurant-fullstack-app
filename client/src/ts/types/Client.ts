import { OrderDiscount } from './OrderDiscount'

export type Client = {
   id: number,
   username: string,
   phone: string,
   firstName: string,
   orderDiscount: OrderDiscount
}