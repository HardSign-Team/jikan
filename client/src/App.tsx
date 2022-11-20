import React, {useEffect} from 'react';
import "./index.css";
import "./App.less";

import {BrowserRouter as Router, Navigate, Route, Routes} from "react-router-dom";
import BasicLayout from "./Layouts/BasicLayout";
import Registration from "./Pages/Register/Registration";
import useUserContext from "./hooks/useUserContext";
import Activities from "./Pages/Activities";
import {check} from "./http/userApi";
import About from "./Pages/About/About";
import Help from "./Pages/Help/Help";

const App = () => {
    const user = useUserContext();

    useEffect(() => {
        check().then(() => {
            user?.setIsAuth(true);
        })
    }, []);

    return (
        <Router>
            <Routes>
                <Route path="/" element={<BasicLayout/>}>
                    <Route index element={!user?.isAuth ? <Navigate to="register"/> : <Activities/>}/>
                    <Route path="register" element={<Registration/>}/>
                    <Route path="about" element={<About/>}/>
                    <Route path="help" element={<Help/>}/>
                </Route>
            </Routes>
        </Router>
    );
}

export default App;
