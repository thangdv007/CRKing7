import React from 'react';
import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import Footer from '~/components/footer';
import Header from '~/components/header';
import Topbar from '~/components/topbar';
import path from '~/constants/path';
import Login from '../user/login';
import './styles.css';
import Register from '../user/register';
import Home from '../home';
import Profile from '../profile';
import Address from '../profile/address';
import ProductView from '../product';
import CollectionProduct from '../collectionProduct';
import Cart from '../cart';
import DetailProduct from '../detailProduct';
import SearchProduct from '../searchProduct';
import DetailArticle from '../detailArticle';
import DetailOrder from '../profile/detailOrder';
import Contact from '../contact';
import Articles from '../articles';

const Layout = () => {
  const ScrollToTopOnNavigate = () => {
    const { pathname } = useLocation();

    React.useEffect(() => {
      const scrollToTop = () => {
        window.scrollTo({
          top: 0,
          behavior: 'smooth',
        });
      };

      document.body.classList.add('scroll-fade-out');
      scrollToTop();

      setTimeout(() => {
        document.body.classList.remove('scroll-fade-out');
      }, 300);
    }, [pathname]);

    return null;
  };
  React.useEffect(() => {
    const handleScroll = () => {
      const scrollToTopBtn = document.getElementById('scrollToTop');
      if (scrollToTopBtn) {
        if (window.scrollY > 300) {
          scrollToTopBtn.classList.add('show');
        } else {
          scrollToTopBtn.classList.remove('show');
        }
      }
    };

    window.addEventListener('scroll', handleScroll);

    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, []);
  return (
    <>
      <ScrollToTopOnNavigate />
      <Topbar />
      <Header />
      <div className="mainWrapper--content">
        <Routes>
          <Route path={path.login} element={<Login />} />
          <Route path={path.register} element={<Register />} />
          <Route path={path.home} element={<Home />} />
          <Route path={path.profile} element={<Profile />} />
          <Route path={path.address} element={<Address />} />
          <Route path={path.product} element={<ProductView />} />
          <Route path={path.collectionsProduct} element={<CollectionProduct />} />
          <Route path={path.cart} element={<Cart />} />
          <Route path={path.detailProduct} element={<DetailProduct />} />
          <Route path={path.searchProduct} element={<SearchProduct />} />
          <Route path={path.detailArticle} element={<DetailArticle />} />
          <Route path={path.detailOrder} element={<DetailOrder />} />
          <Route path={path.contact} element={<Contact />} />
          <Route path={path.article} element={<Articles />} />
        </Routes>
      </div>
      <Footer />
      <div className="back-to-top" id="scrollToTop" onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}>
        <a className="cursor-pointer">
          <div className="btt-back">
            <span className="btt-label-back">Về đầu trang</span>
            <span className="btt-icon-back">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width={16}
                height={16}
                fill="currentColor"
                className="bi bi-arrow-bar-up"
                viewBox="0 0 16 16"
              >
                {' '}
                <path
                  fillRule="evenodd"
                  d="M8 10a.5.5 0 0 0 .5-.5V3.707l2.146 2.147a.5.5 0 0 0 .708-.708l-3-3a.5.5 0 0 0-.708 0l-3 3a.5.5 0 1 0 .708.708L7.5 3.707V9.5a.5.5 0 0 0 .5.5zm-7 2.5a.5.5 0 0 1 .5-.5h13a.5.5 0 0 1 0 1h-13a.5.5 0 0 1-.5-.5z"
                />{' '}
              </svg>
            </span>
          </div>
        </a>
      </div>
    </>
  );
};

export default Layout;
