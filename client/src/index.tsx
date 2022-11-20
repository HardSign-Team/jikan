import React from 'react';
import ReactDOM from 'react-dom/client';
import App from "./App";
import {UserProvider} from './Contexts/UserContext';


const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);
root.render(
    <React.StrictMode>
        <UserProvider userInfo={{isAuth: false, userInfo: null}}>
            <App/>
        </UserProvider>
    </React.StrictMode>
);

