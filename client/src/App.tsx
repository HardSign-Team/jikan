import React, {useEffect} from 'react';
import "./index.css";
import "./App.less";

import {BrowserRouter as Router, Navigate, Route, Routes} from "react-router-dom";
import BasicLayout from "./Layouts/BasicLayout";
import Registration from "./Pages/Register/Registration";
import useUserContext from "./hooks/useUserContext";
import Activities from "./Pages/Activities/Activities";
import About from "./Pages/About/About";
import Help from "./Pages/Help/Help";

const App = () => {
    const user = useUserContext();

    return (
        <Router>
            <Routes>
                <Route path="/" element={<BasicLayout/>}>
                    <Route index element={!user?.isAuth ? <Navigate to="register"/> : <Navigate to="activities"/>}/>
                    <Route path="activities" element={<Activities/>}/>
                    <Route path="register" element={user?.isAuth ? <Navigate to="/activities"/> : <Registration/>}/>
                    <Route path="about" element={<About/>}/>
                    <Route path="help" element={<Help/>}/>
                </Route>
            </Routes>
        </Router>
    );
}

export default App;
