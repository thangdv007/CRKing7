import React from 'react';
import { useSelector } from 'react-redux';
import Api from '~/api/apis';
import { toast } from 'react-toastify';
import Images from '~/assets';
import { REQUEST_API } from '~/constants/method';
import { RootState } from '~/redux/reducers';
import { User } from '~/types/user.type';
import { useNavigate } from 'react-router-dom';
import path from '~/constants/path';

const Account = () => {
  const token = useSelector((state: RootState) => state.ReducerAuth.token);
  const user = useSelector((state: RootState) => state.ReducerAuth.user);
  const [page, setPage] = React.useState(0);
  const [lastPage, setLastPage] = React.useState(0);
  const navigate = useNavigate();

  const [listUser, setListUser] = React.useState<User[]>([]);

  const getListUser = async () => {
    if (!!token) {
      try {
        const currentPage = 0;
        setPage(currentPage);
        const url = Api.getListUser(currentPage);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'get',
            token: token,
          }),
        ]);

        if (res.status) {
          const newData = res.data.data.map((item) => {
            return {
              ...item,
            };
          });
          setListUser(newData);
        } else {
        }
      } catch (error) {
        console.error(error);
      }
    } else {
      toast.error(`Vui lòng đăng nhập lại`, {
        position: 'top-right',
        pauseOnHover: false,
        theme: 'dark',
      });
    }
  };
  React.useEffect(() => {
    getListUser();
  }, []);

  const hideUser = async (userId: number) => {
    if (!!token) {
      try {
        const url = Api.hideUser(userId, user.id);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'post',
            token: token,
          }),
        ]);
        if (res.status) {
          getListUser();
          toast.success(`${res.data}`, {
            position: 'top-right',
            pauseOnHover: false,
            theme: 'dark',
          });
        } else {
        }
      } catch (error) {
        console.error(error);
      }
    } else {
      toast.error(`Vui lòng đăng nhập lại`, {
        position: 'top-right',
        pauseOnHover: false,
        theme: 'dark',
      });
    }
  };
  const showUser = async (userId: number) => {
    if (!!token) {
      try {
        const url = Api.showUser(userId, user.id);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'post',
            token: token,
          }),
        ]);
        if (res.status) {
          getListUser();
          toast.success(`${res.data}`, {
            position: 'top-right',
            pauseOnHover: false,
            theme: 'dark',
          });
        } else {
        }
      } catch (error) {
        console.error(error);
      }
    } else {
      toast.error(`Vui lòng đăng nhập lại`, {
        position: 'top-right',
        pauseOnHover: false,
        theme: 'dark',
      });
    }
  };

  return (
    <div className="">
      <div className="flex justify-between items-center">
        <span className="text-base font-bold">Tài khoản</span>
        <div
          className="w-auto px-2 py-1 cursor-pointer flex justify-center items-center bg-blue rounded-md"
          onClick={() => navigate(path.addEmp)}
        >
          <i className="bx bxs-user-plus text-2xl text-white"></i>
        </div>
      </div>
      <div className="w-full h-[2px] bg-black mt-5"></div>
      <div className="overflow-x-auto w-full">
        <table className="table w-full">
          <thead>
            <tr>
              <th>Tài khoản</th>
              <th>Điện thoại</th>
              <th>Ngày tạo</th>
              <th>Trạng thái</th>
              <th>ROLE</th>
              <th className="text-center">Hành động</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {!!listUser &&
              !!listUser.length &&
              listUser.map((item, i) => {
                return (
                  <tr key={i} className="cursor-pointer">
                    <td>
                      <div className="flex items-center space-x-3">
                        <div>
                          <div className="font-bold">
                            {item.lastName} {item.firstName}
                          </div>
                          <div className="text-sm opacity-50">{item.username}</div>
                        </div>
                      </div>
                    </td>
                    <td>{item.phone}</td>
                    <td>{item.createdDate}</td>
                    {item.status === 1 && (
                      <td className="text-green-500">
                        Hoạt động <i className="bx bxs-lock text-xl text-red-500" onClick={() => hideUser(item.id)}></i>
                      </td>
                    )}
                    {item.status === 0 && (
                      <td className="text-red-500">
                        Đã khóa{' '}
                        <i className="bx bxs-lock-open text-xl text-green-500" onClick={() => showUser(item.id)}></i>
                      </td>
                    )}
                    <td>{item.roles[0].name.replace('ROLE_', '')}</td>
                    <td className="flex flex-col items-center justify-between ">
                      <i
                        className="bx bxs-show text-2xl font-semibold text-blue"
                        onClick={() => navigate(path.detailAcc, { state: item.id })}
                      ></i>
                      <i
                        className="bx bxs-pencil text-2xl font-semibold text-blue pt-2"
                        onClick={() => navigate('/', { state: item.id })}
                      ></i>
                    </td>
                  </tr>
                );
              })}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Account;
