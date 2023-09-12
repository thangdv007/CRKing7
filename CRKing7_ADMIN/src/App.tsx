import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import './App.css';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/user/login';
import Layout from './pages/layout';
import 'react-toastify/dist/ReactToastify.css';
import Types from './redux/types';
import { ToastContainer } from 'react-toastify';
import { RootState } from './redux/reducers';
import path from './constants/path';
import ForgotPassword from './pages/user/forgotpass';

const App = () => {
  const dispatch = useDispatch();
  const user = useSelector((state: RootState) => state.ReducerAuth.user);
  const token = useSelector((state: RootState) => state.ReducerAuth.token);

  const checkData = () => {
    const userLocal = localStorage.getItem('user');
    const tokenLocal = localStorage.getItem('token');
    const parseUser = userLocal ? JSON.parse(userLocal) : null;
    if (tokenLocal && parseUser && !token) {
      dispatch({
        type: Types.LOGIN,
        value: { user: parseUser, token: tokenLocal },
      });
    }
  };
  React.useEffect(() => {
    if (!user) {
      checkData();
    }
  }, [user]);

  return (
    <Router>
      <ToastContainer />
      <Routes>
        <Route path={path.login} element={<Login />} />
        <Route path={path.forgotpass} element={<ForgotPassword />} />
        <Route path="*" element={!token ? <Navigate to={path.login} replace /> : <Layout />} />
      </Routes>
    </Router>
  );
};

export default App;
