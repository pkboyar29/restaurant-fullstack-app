import { MenuPosition } from '../../ts/types/MenuPosition'
import styles from './MenuPositionModal.module.scss'
import closemodal from '../../assets/closemodal.svg'
import nullimage from '../../assets/nullimage.jpeg'
import Slider from 'react-slick'
import 'slick-carousel/slick/slick.css'
import 'slick-carousel/slick/slick-theme.css'

interface MenuPositionModalProps {
   menuPosition: MenuPosition | null
   handleCloseModal: (result: boolean) => void
   setCartItem: (menuPosition: MenuPosition) => void
}

function MenuPositionModal({ menuPosition, handleCloseModal, setCartItem }: MenuPositionModalProps) {

   var sliderSettings = {
      dots: true,
      infinite: true,
      slidesToShow: 1,
      slidesToScroll: 1
   };

   const onClick = () => {
      if (menuPosition !== null) {
         setCartItem(menuPosition)
      }
      handleCloseModal(true)
   }

   return (
      <div className={styles['menu-modal']}>
         <div className={styles['modal__content']}>
            <div className={styles['modal__header']}>
               <div className={styles['modal__title']}>{menuPosition?.name}</div>
               <img onClick={() => handleCloseModal(false)} className={styles['modal__close']} src={closemodal} />
            </div>
            <div className={styles['modal__slider']}>

               <Slider {...sliderSettings}>
                  {menuPosition?.image1 ? <img src={menuPosition?.image1} alt={menuPosition?.name} /> : <img src={nullimage} alt='null image' />}
                  {menuPosition?.image2 ? <img src={menuPosition?.image2} alt={menuPosition?.name} /> : <img src={nullimage} alt='null image' />}
                  {menuPosition?.image3 ? <img src={menuPosition?.image3} alt={menuPosition?.name} /> : <img src={nullimage} alt='null image' />}
                  {menuPosition?.image4 ? <img src={menuPosition?.image4} alt={menuPosition?.name} /> : <img src={nullimage} alt='null image' />}
               </Slider>

            </div>
            <div className={styles['modal__descr']}>{menuPosition?.descr}</div>
            <div style={{ display: 'flex', gap: '15px' }}>Цена <div className={styles['modal__price']}>{menuPosition?.price} ₽</div></div>
            <div style={{ display: 'flex', gap: '15px' }}>Порция <div className={styles['modal__portion']}>{menuPosition?.portion}</div></div>

            <button onClick={onClick} className={styles['modal__button']} type='button'>Заказать</button>
         </div>
      </div>
   )
}

export default MenuPositionModal