import React from 'react';
import { useSelector } from 'react-redux';
import Api from '~/api/apis';
import { toast } from 'react-toastify';
import { REQUEST_API } from '~/constants/method';
import { RootState } from '~/redux/reducers';
import { useNavigate } from 'react-router-dom';
import path from '~/constants/path';
import { Product, ProductImages } from '~/types/product.type';
import { API_URL_IMAGE, formatPrice } from '~/constants/utils';
import SpinLoading from '~/components/loading/spinLoading';
import Modal from 'react-modal';
import { Banner } from '~/types/banner.type';
import { Category } from '~/types/category.type';

const customStyles = {
  content: {
    top: '50%',
    left: '50%',
    right: 'auto',
    bottom: 'auto',
    marginRight: '-50%',
    transform: 'translate(-50%, -50%)',
  },
};

const Banners = () => {
  const token = useSelector((state: RootState) => state.ReducerAuth.token);
  const navigate = useNavigate();

  const [page, setPage] = React.useState(0);
  const [lastPage, setLastPage] = React.useState(0);
  const [keyword, setKeyword] = React.useState('');
  const [isOpen, setIsOpen] = React.useState(false);
  const [banner, setBanner] = React.useState<Banner[]>([]);
  const [loadding, setLoading] = React.useState(false);
  const [categoriesMapping, setCategoriesMapping] = React.useState({});
  const [bannerId, setBannerId] = React.useState<number>();

  const openModal = (id: number) => {
    setBannerId(id);
    setIsOpen(true);
  };
  const closeModal = () => {
    setIsOpen(false);
  };

  const getAllBanner = async () => {
    if (!!token) {
      try {
        setLoading(true);
        const currentPage = 0;
        setPage(currentPage);
        const url = Api.getAllBanner(currentPage, keyword);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'get',
            token: token,
          }),
        ]);

        if (res.status) {
          setLoading(false);
          const newData = res.data.data.map((item) => {
            return {
              ...item,
            };
          });
          setBanner(newData);
        } else {
          setLoading(true);
          toast.error(`${res.data.data}`, {
            position: 'top-right',
            pauseOnHover: false,
            theme: 'dark',
          });
        }
      } catch (error) {
        setLoading(true);
        console.error(error);
      } finally {
        setLoading(false);
      }
    }
  };
  React.useEffect(() => {
    getAllBanner();
  }, []);
  const getCategory = async (id: number) => {
    if (!!token) {
      try {
        const url = Api.detailCategory(id);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'get',
            token: token,
          }),
        ]);
        if (res.status) {
          const category = res.data;
          setCategoriesMapping((prevMapping) => ({
            ...prevMapping,
            [category.id]: category.title,
          }));
        } else {
          toast.error(`Có lỗi xảy ra`, {
            position: 'top-right',
            pauseOnHover: false,
            theme: 'dark',
          });
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
    if (banner.length > 0) {
      banner.forEach(async (item) => {
        if (item.categoryId !== 0) {
          await getCategory(item.categoryId);
        }
      });
    }
  }, [banner]);
  const hideBanner = async (id: number) => {
    if (!!token) {
      try {
        const url = Api.hideBanner(id);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'put',
            token: token,
          }),
        ]);
        if (res.status) {
          getAllBanner();
          toast.success(`${res.data}`, {
            position: 'top-right',
            pauseOnHover: false,
            theme: 'dark',
          });
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
  const showBanner = async (id: number) => {
    if (!!token) {
      try {
        const url = Api.showBanner(id);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'put',
            token: token,
          }),
        ]);
        if (res.status) {
          getAllBanner();
          toast.success(`${res.data}`, {
            position: 'top-right',
            pauseOnHover: false,
            theme: 'dark',
          });
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
  const deleteBanner = async (id: number) => {
    if (!!token) {
      try {
        const url = Api.deleteBanner(id);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'delete',
            token: token,
          }),
        ]);
        console.log(res);

        if (res.status) {
          getAllBanner();
          closeModal();
          toast.success(`${res.data}`, {
            position: 'top-right',
            pauseOnHover: false,
            theme: 'dark',
          });
        } else {
          toast.error(`Xóa sản phẩm không thành công`, {
            position: 'top-right',
            pauseOnHover: false,
            theme: 'dark',
          });
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
        <div className="flex items-center">
          <span className="text-base font-bold">Quản lý sản phẩm</span>
          <div className="flex ml-5 items-center justify-center">
            <input
              className="h-10 border-black rounded-lg pl-3"
              placeholder="Nhập tên sản phẩm ..."
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
            />
            <i
              className="bx bx-search text-2xl text-blue ml-3 cursor-pointer"
              onClick={() => {
                getAllBanner(), setKeyword('');
              }}
            ></i>
          </div>
        </div>
        <div
          className="w-auto px-2 py-1 cursor-pointer flex justify-center items-center bg-blue rounded-md"
          onClick={() => navigate(path.addBanner)}
        >
          <i className="bx bxs-plus-circle text-2xl text-white"></i>
        </div>
      </div>
      <div className="w-full h-[2px] bg-black mt-5"></div>
      <div className="overflow-x-auto w-full">
        <table className="table w-full">
          <thead>
            <tr>
              <th className="w-[5%]">Mã</th>
              <th className="w-[15%] text-center">Tên Banner</th>
              <th className="w-[15%] text-center">Hình ảnh</th>
              <th className="w-[10%] text-center">Danh mục</th>
              <th className="w-[10%] text-center">Trạng thái</th>
              <th className="w-[15%] text-center">Ngày tạo</th>
              <th className="w-[15%] text-center">Ngày sửa</th>
              <th className="w-[15%] text-center">Hành động</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {!!banner &&
              !!banner.length &&
              banner.map((item, i) => {
                return (
                  <tr key={i} className="cursor-pointer">
                    <td>{item.id}</td>
                    <td className="text-center">{item.name}</td>
                    <td className="">
                      <img src={`${API_URL_IMAGE}${item?.src}`} className="w-auto h-auto object-contain" />
                    </td>
                    <td className="text-center">
                      {item.categoryId === 0 ? 'Banner home' : `${categoriesMapping[item.categoryId]}`}
                    </td>
                    {item.status === 1 && (
                      <td className="text-green-500">
                        <div className="flex items-center justify-between">Hoạt động </div>
                      </td>
                    )}
                    {item.status === 0 && (
                      <td className="text-red-500">
                        <div className="flex items-center justify-between">Đã khóa </div>
                      </td>
                    )}
                    <td className="text-center">{item.createdDate}</td>
                    <td className="text-center">{item.modifiedDate}</td>
                    <td className="flex flex-col items-center justify-between ">
                      {item.status === 1 && (
                        <i className="bx bxs-lock text-xl text-red-500" onClick={() => hideBanner(item.id)}></i>
                      )}
                      {item.status === 0 && (
                        <i className="bx bxs-lock-open text-xl text-green-500" onClick={() => showBanner(item.id)}></i>
                      )}
                      <i
                        className="bx bxs-pencil text-2xl font-semibold text-blue pt-2"
                        onClick={() => navigate(path.editBanner, { state: item.id })}
                      ></i>
                    </td>
                    <td className="">
                      <i className="bx bx-trash text-2xl text-red-500" onClick={() => openModal(item.id)}></i>
                    </td>
                  </tr>
                );
              })}
          </tbody>
        </table>
      </div>
      <Modal isOpen={isOpen} onRequestClose={closeModal} style={customStyles}>
        <div className="w-full flex flex-col items-center justify-center">
          <h2 className="text-red-500">Bạn có chắc chắn muốn xóa sản phẩm này</h2>
          <p className="text-sm text-black">
            Nếu bạn xóa sản phẩm này thì tất cả dữ liệu liên quan đến sản phẩm này đều bị xóa!
          </p>
          <div className="flex items-center justify-around w-full mt-5">
            <div
              className="cursor-pointer w-[30%] h-10 rounded-md bg-blue flex items-center justify-center"
              onClick={closeModal}
            >
              <span>Hủy</span>
            </div>
            <div
              className="cursor-pointer w-[30%] h-10 rounded-md bg-red-500 flex items-center justify-center"
              onClick={() => deleteBanner(bannerId)}
            >
              <span>Xóa</span>
            </div>
          </div>
        </div>
      </Modal>
      {loadding && <SpinLoading />}
    </div>
  );
};

export default Banners;
