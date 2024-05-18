import styles from './Modal.module.scss'

interface ModalProps {
   modalText: string
   buttonConfirmText: string
   confirmHandler: () => void
}

function Modal({ modalText, buttonConfirmText, confirmHandler }: ModalProps) {

   return (
      <>
         <div className={styles["modal"]}>
            <div className={styles["modal__content"]}>
               <div className={styles["modal__text"]}>
                  {modalText}
               </div>
               <div className={styles["modal__buttons"]}>
                  <button onClick={confirmHandler} className={styles["modal__confirm"]}>{buttonConfirmText}</button>
               </div>
            </div>
         </div>
      </>
   )
}

export default Modal