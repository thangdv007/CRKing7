import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ThemeAction from './redux/actions/ThemeAction';
import { RootState } from './redux/reducers';
import Sidebar from './components/sidebar';
import TopNav from './components/topnav';
import Dashboard from './pages/dashboard';
import Product from './pages/products';
import path from './constants/path';
import Account from './pages/account';

const App = () => {
  const themeReducer = useSelector((state: RootState) => state.ThemeReducer);
  const dispatch = useDispatch();

  React.useEffect(() => {
    const themeClass = localStorage.getItem('themeMode') || 'theme-mode-light';
    const colorClass = localStorage.getItem('colorMode') || 'theme-mode-light';

    dispatch(ThemeAction.setMode(themeClass));
    dispatch(ThemeAction.setColor(colorClass));
  }, [dispatch]);

  return (
    <Router>
      <div className={`layout ${themeReducer.mode} ${themeReducer.color}`}>
        <div className="layout__sidebar">
          <Sidebar />
          <div className="layout__content">
            <TopNav />
            <div className="layout__content-main">
              <Routes>
                <Route path={path.home} element={<Dashboard />} />
                <Route path={path.products} element={<Product />} />
                <Route path={path.accounts} element={<Account />} />
              </Routes>
            </div>
          </div>
        </div>
      </div>
    </Router>
  );
};

export default App;
