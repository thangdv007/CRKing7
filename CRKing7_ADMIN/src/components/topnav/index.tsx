import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './styles.css';
import Dropdown from '../dropdown';
import ThemeMenu from '../thememenu';
import notifications from '../../assets/JsonData/notification.json';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '~/redux/reducers';
import Images from '~/assets';
import { toast } from 'react-toastify';
import authApi from '~/api/auth.apis';
import { REQUEST_API } from '~/constants/method';
import Types from '~/redux/types';

interface NotificationItem {
  icon: string;
  content: string;
}

interface UserMenu {
  icon: string;
  content: string;
  route: string;
}

const renderNotificationItem = (item: NotificationItem, index: number) => (
  <div className="notification-item cursor-pointer" key={index}>
    <i className={item.icon}></i>
    <span>{item.content}</span>
  </div>
);

const renderUserToggle = (user: { username: string }) =>
  user && (
    <div className="topnav__right-user cursor-pointer flex items-center">
      <div className="topnav__right-user__name">{user.username}</div>
    </div>
  );

const renderUserMenu = (item: UserMenu, index: number, onClick: React.MouseEventHandler<HTMLDivElement>) => (
  <div key={index}>
    <div className="notification-item cursor-pointer" onClick={onClick}>
      <i className={item.icon}></i>
      <span>{item.content}</span>
    </div>
  </div>
);
const TopNav = () => {
  const curr_user = useSelector((state: RootState) => state.ReducerAuth.user);
  const token = useSelector((state: RootState) => state.ReducerAuth.token);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user_menu = [
    {
      icon: 'bx bx-user',
      content: 'Hồ sơ',
      route: '/profile',
    },
    {
      icon: 'bx bx-cog',
      content: 'Cài đặt',
      route: '/setting',
    },
    {
      icon: 'bx bx-log-out-circle bx-rotate-180',
      content: 'Đăng xuất',
      route: '/logout',
    },
  ];
  const handleLogout = async () => {
    const res = await authApi.logout();
    if (res.status) {
      navigate('/login');
      localStorage.setItem('user', null);
      localStorage.setItem('token', '');
      dispatch({ type: Types.LOGOUT });
    } else {
      toast.error('Lỗi', {
        position: 'top-right',
        pauseOnHover: false,
        theme: 'dark',
      });
    }
  };
  const routeClickHandle: { [key: string]: () => void } = {
    '/profile': () => {
      navigate('/profile');
    },
    '/setting': () => {
      navigate('/setting');
    },
    '/logout': () => {
      handleLogout();
    },
  };
  const handleCickUser = (itemRoute: string) => {
    const clickHanle = routeClickHandle[itemRoute];
    if (clickHanle) {
      clickHanle();
    }
  };

  return (
    <div className="topnav">
      <div className="topnav__search"></div>
      <div className="topnav__right">
        <div className="topnav__right-item">
          {/* dropdown here */}
          <Dropdown
            customToggle={() => renderUserToggle(curr_user)}
            contentData={user_menu}
            renderItems={(item, index) => renderUserMenu(item, index, () => handleCickUser(item.route))}
          />
        </div>
        <div className="topnav__right-item">
          <Dropdown
            icon="bx bx-bell"
            badge="12"
            contentData={notifications}
            renderItems={(item, index) => renderNotificationItem(item, index)}
            renderFooter={() => <Link to="/">View All</Link>}
          />
          {/* dropdown here */}
        </div>
        <div className="topnav__right-item">
          <ThemeMenu />
        </div>
      </div>
    </div>
  );
};

export default TopNav;
