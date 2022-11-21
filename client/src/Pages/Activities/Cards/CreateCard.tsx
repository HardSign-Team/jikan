import React from 'react';
import cn from "classnames";
import "./CreateCard.less";
import {createActivity} from "../../../http/activitiesApi";
import 'react-responsive-modal/styles.css';
import Modal from "react-responsive-modal";

const CreateCard = () => {
        const [showModal, setShowModal] = React.useState(false);
        const [activityName, setActivityName] = React.useState("");
        const onOpenModal = () => {
            setShowModal(true);
        }

        const onCloseModal = () => {
            setShowModal(false);
        }

        const onSaveModal = async () => {
            const a = createActivity(activityName);
            setShowModal(false);
        }
        return (
            <>
                <div className={cn("card")} onClick={onOpenModal}>
                    <div className={cn("plus")}/>
                    <div className={cn("card-caption")}>
                        <span>Создать</span>
                        <span>активность</span>
                    </div>
                </div>
                {showModal &&
                <Modal open center classNames={{modal: "modal"}} onClose={() => setShowModal(false)}>
                    <h2 className="modal-header">Напишите название для активности</h2>
                    <input onChange={(e) => setActivityName(e.target.value)} maxLength={50} placeholder="Работа" type="text"
                           className={cn("input")}/>
                    <button className="save-button" onClick={onSaveModal}>Создать</button>
                </Modal>
                }
            </>
        );
    }
;

export default CreateCard;
