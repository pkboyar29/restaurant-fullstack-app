import { MenuPosition } from "../types/MenuPosition";

export interface MenuPositionResponseFromServer {
   id: number;
   name: string;
   descr: string;
   availability: boolean;
   dateEnteredInMenu: string;
   price: number;
   portion: string;
   menuSection: {
      id: number;
      name: string;
      descr: string;
   };
}

// Преобразовать данные из формата ответа в формат MenuPosition
function transformMenuPosition(responseData: MenuPositionResponseFromServer[]): MenuPosition[] {
   return responseData.map(item => ({
      id: item.id,
      name: item.name,
      sectionName: item.menuSection.name,
      price: item.price,
      priceText: item.price + '₽',
      portion: item.portion,
      availability: item.availability
   }));
}

export default transformMenuPosition