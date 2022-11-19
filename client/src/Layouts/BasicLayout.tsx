import React from 'react';
import {Outlet} from "react-router-dom";
import Header from "../Pages/Components/Header/Header";

const BasicLayout = () => {
    return (
        <>
            <Header/>
            <Outlet/>
        </>
    );
};

export default BasicLayout;
