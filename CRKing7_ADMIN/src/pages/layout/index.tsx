import React from 'react';
import { Suspense } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import Sidebar from '~/components/sidebar';
import TopNav from '~/components/topnav';
import path from '~/constants/path';
import ThemeAction from '~/redux/actions/ThemeAction';
import { RootState } from '~/redux/reducers';
import Dashboard from '../dashboard';
import Product from '../products';
import Account from '../account';
import SuspenseContent from './suspenseContent';
import AddEmp from '../account/addEmp';
import DetailAcc from '../account/detaillAcc';
import { ScrollRestoration } from '~/scroll-restoration';
import Profile from '../profile';
import EditProduct from '../products/editProduct';
import AddProduct from '../products/addProduct';
import DetailProduct from '../products/detailProduct';
import Category from '../category';
import AddCategory from '../category/addCategory';
import EditCategory from '../category/editCategory';
import DetailCategory from '../category/detailCategory';
import Banners from '../banner';
import EditBanner from '../banner/editBanner';
import CreateBanner from '../banner/createBanner';
import DetailBanner from '../banner/detailBanner';
import Sale from '../sale';
import DetailSale from '../sale/detailSale';
import AddSale from '../sale/addSale';
import EditSale from '../sale/editSale';
import AddProductSale from '../sale/addProductSale';

const Layout = () => {
  const themeReducer = useSelector((state: RootState) => state.ThemeReducer);
  const dispatch = useDispatch();

  React.useEffect(() => {
    const themeClass = localStorage.getItem('themeMode') || 'theme-mode-light';
    const colorClass = localStorage.getItem('colorMode') || 'theme-mode-light';

    dispatch(ThemeAction.setMode(themeClass));
    dispatch(ThemeAction.setColor(colorClass));
  }, [dispatch]);

  const ScrollToTopOnNavigate = () => {
    const { pathname } = useLocation();

    React.useEffect(() => {
      window.scrollTo(0, 0); // Di chuyển trang lên đầu
    }, [pathname]);

    return null; // Không có đầu ra giao diện thị giác
  };

  return (
    <div className={`layout ${themeReducer.mode} ${themeReducer.color}`}>
      <ScrollToTopOnNavigate />
      <div className="layout__sidebar">
        <Sidebar />
        <div className="layout__content">
          <TopNav />
          <div className="layout__content-main">
            <Suspense fallback={<SuspenseContent />}>
              <Routes>
                <Route path={path.home} element={<Dashboard />} />
                <Route path={path.products} element={<Product />} />
                <Route path={path.accounts} element={<Account />} />
                <Route path={path.addEmp} element={<AddEmp />} />
                <Route path={path.detailAcc} element={<DetailAcc />} />
                <Route path={path.profile} element={<Profile />} />
                <Route path={path.editProduct} element={<EditProduct />} />
                <Route path={path.addProduct} element={<AddProduct />} />
                <Route path={path.detailProduct} element={<DetailProduct />} />
                <Route path={path.categories} element={<Category />} />
                <Route path={path.addCategory} element={<AddCategory />} />
                <Route path={path.editCategory} element={<EditCategory />} />
                <Route path={path.detailCategory} element={<DetailCategory />} />
                <Route path={path.banners} element={<Banners />} />
                <Route path={path.editBanner} element={<EditBanner />} />
                <Route path={path.addBanner} element={<CreateBanner />} />
                <Route path={path.detailBanner} element={<DetailBanner />} />
                <Route path={path.sale} element={<Sale />} />
                <Route path={path.detailSale} element={<DetailSale />} />
                <Route path={path.addSale} element={<AddSale />} />
                <Route path={path.editSale} element={<EditSale />} />
                <Route path={path.addProductToSale} element={<AddProductSale />} />
              </Routes>
            </Suspense>
          </div>
        </div>
      </div>
    </div>
  );
};
export default Layout;
