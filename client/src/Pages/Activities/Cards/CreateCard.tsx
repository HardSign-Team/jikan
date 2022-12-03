import React, {useState} from 'react';
import cn from "classnames";
import "./CreateCard.less";
import {createActivity} from "../../../http/activitiesApi";
import 'react-responsive-modal/styles.css';
import Modal from "react-responsive-modal";
import useUserContext from "../../../hooks/useUserContext";
import {ColorRing} from "react-loader-spinner";

interface CreateCardProps {
    onSave: () => void;
}

const CreateCard = ({onSave}: CreateCardProps) => {
    const userInfo = useUserContext();
    const [showModal, setShowModal] = useState(false);
    const [activityName, setActivityName] = useState("");
    const [loading, setLoading] = useState(false);
    const onOpenModal = () => {
        setShowModal(true);
    }

    const onSaveModal = async () => {
        setLoading(true);
        try {
            const data = await createActivity(activityName);
            userInfo?.saveUserInfo({
                name: userInfo?.userInfo?.name,
                login: userInfo?.userInfo?.login,
                id: +(data?.userId ?? 0)
            });
        } finally {
            setLoading(false);
            setShowModal(false);
            onSave();
        }
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
                <ColorRing
                    visible={loading}
                    height="80"
                    width="80"
                    ariaLabel="blocks-loading"
                    wrapperStyle={{}}
                    wrapperClass="blocks-wrapper"
                    colors={['#b8c480', '#B2A3B5', '#F4442E', '#51E5FF', '#429EA6']}
                />
                {
                    !loading && <><h2 className="modal-header">Напишите название для активности</h2>
                        <input onChange={(e) => setActivityName(e.target.value)} maxLength={50} placeholder="Работа"
                               type="text"
                               className={cn("input")}/>
                        <button className="save-button" onClick={onSaveModal}>Создать</button>
                    </>
                }
            </Modal>
            }
        </>
    );
};

export default CreateCard;
