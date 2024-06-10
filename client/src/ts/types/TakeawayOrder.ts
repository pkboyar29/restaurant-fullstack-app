import { TakeawayOrderPosition } from './TakeawayOrderPosition'

export type TakeawayOrder = {
   id: number,
   discountedCost: number,
   paymentMethod: string,
   orderDate: string,
   receiptDate: string,
   receiptOption: string,
   takeawayOrderPositions: TakeawayOrderPosition[]
}