import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import Api from '~/api/apis';
import { REQUEST_API } from '~/constants/method';
import { RootState } from '~/redux/reducers';
import { toast } from 'react-toastify';
import { User } from '~/types/user.type';

const ProfileTap = () => {
  const token = useSelector((state: RootState) => state.ReducerAuth.token);
  const userId = useSelector((state: RootState) => state.ReducerAuth.user);

  const [user, setUser] = useState<User>();

  const viewDetail = async () => {
    if (!!token) {
      try {
        const url = Api.detailAcc(userId.id);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'get',
            token: token,
          }),
        ]);
        if (res.status) {
          setUser(res.data);
        } else {
          toast.error(`${res.data}`, {
            position: 'top-right',
            pauseOnHover: false,
            theme: 'dark',
          });
        }
      } catch (error) {
        console.error(error);
      }
    }
  };
  React.useEffect(() => {
    viewDetail();
  }, []);

  return (
    <div className="max-w-5xl mx-auto shadow-md border rounded-lg">
      <div className="flex items-center justify-center pt-3">
        <span className="font-bold text-xl uppercase">Thông tin tài khoản</span>
      </div>
      <div className="p-5">
        <div className="flex items-center justify-around mt-3">
          <div className="w-[30%]">Tài khoản</div>
          <div className="w-[70%] relative flex items-center">{user?.username}</div>
        </div>
        <div className="flex items-center justify-around mt-3">
          <div className="w-[30%]">Họ và Tên</div>
          <div className="w-[70%] relative flex items-center justify-between">
            {user?.lastName} {user?.firstName}
          </div>
        </div>
        <div className="flex items-center justify-around mt-3">
          <div className="w-[30%]">Email</div>
          <div className="w-[70%] relative flex items-center">{user?.email}</div>
        </div>
        <div className="flex items-center justify-around mt-3">
          <div className="w-[30%]">Số điện thoại</div>
          <div className="w-[70%] relative flex items-center">{user?.phone}</div>
        </div>
        <div className="flex items-center justify-around mt-3">
          <div className="w-[30%]">Địa chỉ</div>
          <div className="w-[70%] relative flex items-center">{user?.addresses}</div>
        </div>
        <div className="flex items-center justify-around mt-3">
          <div className="w-[30%]">Ngày tạo</div>
          <div className="w-[70%] relative flex items-center">{user?.createdDate}</div>
        </div>
        <div className="flex items-center justify-around mt-3">
          <div className="w-[30%]">Ngày sửa</div>
          <div className="w-[70%] relative flex items-center">{user?.modifiedDate}</div>
        </div>
        <div className="flex items-center justify-around mt-3">
          <div className="w-[30%]">Quyền</div>
          <div className="w-[70%] relative flex items-center">{user?.roles[0].name.replace('ROLE_', '')}</div>
        </div>
        <div className="flex items-center justify-around mt-3">
          <div className="w-[30%]">Trạng thái</div>
          {user?.status === 1 && <div className="w-[70%] relative flex items-center text-green-500">Hoạt động</div>}
          {user?.status === 0 && <div className="w-[70%] relative flex items-center text-red-500">Đã khóa</div>}
        </div>
      </div>
    </div>
  );
};

export default ProfileTap;
