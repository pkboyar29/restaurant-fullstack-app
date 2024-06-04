import { OrderPositionResponse } from './OrderPositionResponse'

export type Order = {
   clientName: string,
   clientPhone: string,
   clientId: number | null,
   requirements: string,
   paymentMethod: string,
   receiptDate: Date,
   receiptOption: string,
   takeawayOrderPositionList: OrderPositionResponse[]
}