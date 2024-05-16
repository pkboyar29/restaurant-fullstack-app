import { MenuPosition } from '../../ts/types/MenuPosition'
import styles from './MenuPositionModal.module.scss'
import { useEffect } from 'react'
import closemodal from '../../assets/closemodal.svg'
import nullimage from '../../assets/nullimage.jpeg'
import Slider from 'react-slick'
import 'slick-carousel/slick/slick.css'
import 'slick-carousel/slick/slick-theme.css'

interface MenuPositionModalProps {
   menuPosition: MenuPosition | null
   handleCloseModal: () => void
}

function MenuPositionModal({ menuPosition, handleCloseModal }: MenuPositionModalProps) {

   useEffect(() => {
      console.log('привет как дела')
      console.log(menuPosition?.name)
   }, [menuPosition])

   var settings = {
      dots: true,
      infinite: true,
      slidesToShow: 1,
      slidesToScroll: 1
   };

   return (
      <div className={styles['menu-modal']}>
         <div className={styles['modal__content']}>
            <div className={styles['modal__header']}>
               <div className={styles['modal__title']}>{menuPosition?.name}</div>
               <img onClick={handleCloseModal} className={styles['modal__close']} src={closemodal} />
            </div>
            <div className={styles['modal__image']}>

               <Slider {...settings}>
                  {menuPosition?.image1 ? <img src={menuPosition?.image1} alt={menuPosition?.name} /> : <img src={nullimage} alt='null image' />}
                  {menuPosition?.image2 ? <img src={menuPosition?.image2} alt={menuPosition?.name} /> : <img src={nullimage} alt='null image' />}
                  {menuPosition?.image3 ? <img src={menuPosition?.image3} alt={menuPosition?.name} /> : <img src={nullimage} alt='null image' />}
                  {menuPosition?.image4 ? <img src={menuPosition?.image4} alt={menuPosition?.name} /> : <img src={nullimage} alt='null image' />}
               </Slider>

            </div>
            <div className={styles['modal__descr']}>{menuPosition?.descr}</div>
            <div style={{ display: 'flex', gap: '15px' }}>Цена <div className={styles['modal__price']}>{menuPosition?.price} ₽</div></div>
            <div style={{ display: 'flex', gap: '15px' }}>Порция <div className={styles['modal__portion']}>{menuPosition?.portion}</div></div>

            <button className={styles['modal__button']} type='button'>Заказать</button>
         </div>
      </div>
   )
}

export default MenuPositionModal