import { OrderPositionResponse } from './OrderPositionResponse'

export type OrderRequest = {
   clientName: string,
   clientPhone: string,
   userId: number | null,
   requirements: string,
   paymentMethod: string,
   receiptDate: Date,
   receiptOption: string,
   takeawayOrderPositionList: OrderPositionResponse[]
}