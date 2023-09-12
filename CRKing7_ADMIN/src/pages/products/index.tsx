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

const Product = () => {
  const token = useSelector((state: RootState) => state.ReducerAuth.token);
  const user = useSelector((state: RootState) => state.ReducerAuth.user);
  const [page, setPage] = React.useState(0);
  const [lastPage, setLastPage] = React.useState(0);
  const [keyword, setKeyword] = React.useState('');
  const navigate = useNavigate();
  const [categoriesMapping, setCategoriesMapping] = React.useState({});
  const [salesMapping, setSalesMapping] = React.useState({});
  const [loadding, setLoading] = React.useState(false);

  const [product, setProduct] = React.useState<Product[]>([]);
  const [images, setImages] = React.useState<ProductImages>();

  const [isOpen, setIsOpen] = React.useState(false);
  const [productId, setProductId] = React.useState<number>();

  const openModal = (id: number) => {
    setProductId(id);
    setIsOpen(true);
  };
  const closeModal = () => {
    setIsOpen(false);
  };

  const getAllProduct = async () => {
    if (!!token) {
      try {
        setLoading(true);
        const currentPage = 0;
        setPage(currentPage);
        const url = Api.getAllProduct(currentPage, keyword);
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
          setProduct(newData);
        } else {
          setLoading(true);
          toast.error(`${res.data.data}`, {
            position: 'top-right',
            pauseOnHover: false,
            theme: 'dark',
          });
        }
      } catch (error) {
        toast.error(`Vui lòng đăng nhập lại`, {
          position: 'top-right',
          pauseOnHover: false,
          theme: 'dark',
        });
        setLoading(true);
        console.error(error);
      } finally {
        setLoading(false);
      }
    } else {
    }
  };
  React.useEffect(() => {
    getAllProduct();
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
  const getSale = async (id: number) => {
    if (!!token) {
      try {
        const url = Api.getSale(id);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'get',
            token: token,
          }),
        ]);
        if (res.status) {
          const sale = res.data;
          setSalesMapping((prevMapping) => ({
            ...prevMapping,
            [sale.id]: sale.name,
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
    }
  };
  React.useEffect(() => {
    if (product.length > 0) {
      product.forEach(async (item) => {
        if (item.sale != null && item.sale !== 0) {
          await getSale(item.sale);
        }
      });
    }
  }, [product]);
  React.useEffect(() => {
    if (product.length > 0) {
      product.forEach(async (item) => {
        await getCategory(item.category);
      });
    }
  }, [product]);
  const hideProduct = async (id: number) => {
    if (!!token) {
      try {
        const url = Api.hideProduct(id);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'put',
            token: token,
          }),
        ]);
        if (res.status) {
          getAllProduct();
          toast.success(`Sản phẩm đã được ẩn`, {
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
    } else {
      toast.error(`Vui lòng đăng nhập lại`, {
        position: 'top-right',
        pauseOnHover: false,
        theme: 'dark',
      });
    }
  };
  const showProduct = async (id: number) => {
    if (!!token) {
      try {
        const url = Api.showProduct(id);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'put',
            token: token,
          }),
        ]);
        if (res.status) {
          getAllProduct();
          toast.success(`Sản phẩm đã được hiện`, {
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
    } else {
      toast.error(`Vui lòng đăng nhập lại`, {
        position: 'top-right',
        pauseOnHover: false,
        theme: 'dark',
      });
    }
  };
  const deleteProduct = async (id: number) => {
    if (!!token) {
      try {
        const url = Api.deleteProduct(id);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'delete',
            token: token,
          }),
        ]);
        console.log(res);

        if (res.status) {
          getAllProduct();
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
                getAllProduct(), setKeyword('');
              }}
            ></i>
          </div>
        </div>
        <div
          className="w-auto px-2 py-1 cursor-pointer flex justify-center items-center bg-blue rounded-md"
          onClick={() => navigate(path.addProduct)}
        >
          <i className="bx bxs-plus-circle text-2xl text-white"></i>
        </div>
      </div>
      <div className="w-full h-[2px] bg-black mt-5"></div>
      <div className="overflow-x-auto w-full">
        <table className="table w-full">
          <thead>
            <tr>
              <th className="w-[5%] text-center">SKU</th>
              <th className="w-[15%] text-center">Tên sản phẩm</th>
              <th className="w-[10%]">Hình ảnh</th>
              <th className="w-[10%] text-center">Giá bán</th>
              <th className="w-[10%] text-center">Giá KM</th>
              <th className="w-[10%] text-center">Khuyến mãi</th>
              <th className="w-[10%] text-center">Trạng thái</th>
              <th className="w-[10%] text-center">Danh mục</th>
              <th className="w-[10%] text-center">Hành động</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {!!product &&
              !!product.length &&
              product.map((item, i) => {
                return (
                  <tr key={i} className="cursor-pointer">
                    <td className="text-center">{item.sku}</td>
                    <td className="text-center">{item.name}</td>
                    <td className="">
                      <img src={`${API_URL_IMAGE}${item?.images[0]?.url}`} className="w-20 h-20 object-contain" />
                    </td>
                    <td className="text-center">{formatPrice(item.price)}</td>
                    <td className="text-center">{formatPrice(item.salePrice)}</td>
                    <td className="text-center">{item.sale != 0 ? `${salesMapping[item.sale]}` : `Không có`}</td>
                    {item.status === 1 && (
                      <td className="text-green-500">
                        <div className="flex items-center justify-between">
                          Hoạt động{' '}
                          <i className="bx bxs-lock text-xl text-red-500" onClick={() => hideProduct(item.id)}></i>
                        </div>
                      </td>
                    )}
                    {item.status === 0 && (
                      <td className="text-red-500">
                        <div className="flex items-center justify-between">
                          Đã khóa{' '}
                          <i
                            className="bx bxs-lock-open text-xl text-green-500"
                            onClick={() => showProduct(item.id)}
                          ></i>
                        </div>
                      </td>
                    )}
                    <td className="text-center">{categoriesMapping[item.category]}</td>
                    <td className="flex flex-col items-center justify-between ">
                      <i
                        className="bx bxs-show text-2xl font-semibold text-blue"
                        onClick={() => navigate(path.detailProduct, { state: item.id })}
                      ></i>
                      <i
                        className="bx bxs-pencil text-2xl font-semibold text-blue pt-2"
                        onClick={() => navigate(path.editProduct, { state: item.id })}
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
              onClick={() => deleteProduct(productId)}
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

export default Product;
