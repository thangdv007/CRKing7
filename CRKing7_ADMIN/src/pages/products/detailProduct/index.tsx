import React from 'react';
import { useSelector } from 'react-redux';
import { useLocation } from 'react-router-dom';
import { toast } from 'react-toastify';
import Api from '~/api/apis';
import { REQUEST_API } from '~/constants/method';
import { RootState } from '~/redux/reducers';
import { Category } from '~/types/category.type';
import { Color, Product, ProductImages, Size } from '~/types/product.type';
import { Sale } from '~/types/sale.type';
import { User } from '~/types/user.type';

const DetailProduct = () => {
  const location = useLocation();
  const token = useSelector((state: RootState) => state.ReducerAuth.token);
  const idProduct = location.state;
  const [product, setProduct] = React.useState<Product>();
  const [size, setSize] = React.useState<Size[]>([]);
  const [color, setColor] = React.useState<Color[]>([]);
  const [images, setImages] = React.useState<ProductImages[]>([]);
  const [user, setUser] = React.useState<User>();
  const [sale, setSale] = React.useState<Sale>();
  const [category, setCategory] = React.useState<Category>();

  const getProduct = async () => {
    if (!!token) {
      try {
        const url = Api.detailProduct(idProduct);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'get',
            token: token,
          }),
        ]);
        if (res.status) {
          setProduct(res.data);
          const colors = res.data.colors;
          setColor(colors);
          setImages(res.data.images);
          const sizesByColor = colors.map((color) => color.sizes);
          setSize(sizesByColor);
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
  React.useEffect(() => {
    getProduct();
  }, []);
  const getUser = async () => {
    if (!!token) {
      try {
        const url = Api.detailAcc(product?.author);
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
    } else {
      toast.error(`Vui lòng đăng nhập lại`, {
        position: 'top-right',
        pauseOnHover: false,
        theme: 'dark',
      });
    }
  };
  const getSale = async () => {
    if (!!token) {
      try {
        const url = Api.getSale(product?.sale);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'get',
            token: token,
          }),
        ]);
        if (res.status) {
          setSale(res.data);
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
  const getCategory = async () => {
    if (!!token) {
      try {
        const url = Api.detailCategory(product?.category);
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'get',
            token: token,
          }),
        ]);
        if (res.status) {
          setCategory(res.data);
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
  React.useEffect(() => {
    if (product?.author != 0 && product?.author != undefined) {
      getUser();
    }
  }, [product?.author]);
  React.useEffect(() => {
    if (product?.sale != 0 && product?.sale != undefined) {
      getSale();
    }
  }, [product?.sale]);
  React.useEffect(() => {
    if (product?.category != 0 && product?.category != undefined) {
      getCategory();
    }
  }, [product?.category]);
  return (
    <>
      <div className="flex items-center justify-around mt-0">
        <div className="w-[70%] flex flex-col border rounded-md p-5">
          <span className="text-lg font-semibold">Thông tin sản phẩm</span>
          <div className="w-full h-[1px] bg-black"></div>
          <div className="flex items-center pt-5">
            <span className="text-base">Tên sản phẩm: </span>
            <span className="text-base ml-5">{product?.name}</span>
          </div>
          <div className="flex items-center pt-5">
            <span className="text-base">SKU: </span>
            <span className="text-base ml-5">{product?.sku}</span>
          </div>
          <div className="flex items-center pt-5">
            <span className="text-base">Chất liệu: </span>
            <span className="text-base ml-5">{product?.material}</span>
          </div>
          <div className="flex items-center pt-5">
            <span className="text-base">Số lượt xem: </span>
            <span className="text-base ml-5">{product?.visited}</span>
          </div>
          <div className="flex items-center pt-5">
            <span className="text-base">Ngày tạo: </span>
            <span className="text-base ml-5">{product?.createdDate}</span>
          </div>
          <div className="flex items-center pt-5">
            <span className="text-base">Ngày sửa: </span>
            <span className="text-base ml-5">{product?.modifiedDate}</span>
          </div>
          <div className="flex items-center pt-5">
            <span className="text-base">Trạng thái: </span>
            {product?.status === 1 && <span className="text-base ml-5 text-green-500">Hoạt động</span>}
            {product?.status === 0 && <span className="text-base ml-5 text-red-500">Đã khóa</span>}
          </div>
          <div className="flex items-center pt-5">
            <span className="text-base">Người tạo: </span>
            <span className="text-base ml-5">{user?.username}</span>
          </div>
          <div className="flex pt-5">
            <span className="text-base w-[10%]">Mô tả: </span>
            <span className="text-base ml-5 w-[80%]">{product?.description}</span>
          </div>
        </div>
        <div className="w-[25%] flex flex-col border rounded-md p-5 self-start">
          <span className="text-lg font-semibold">Giá</span>
          <div className="w-full h-[1px] bg-black"></div>
          <div className="flex items-center pt-5">
            <span className="text-base">Giá: </span>
            <span className="text-base ml-5">{product?.price}</span>
          </div>
          {product?.sale != 0 && (
            <>
              <div className="flex items-center pt-5">
                <span className="text-base">Giá khuyến mãi: </span>
                <span className="text-base ml-5">{product?.salePrice}</span>
              </div>
              <div className="flex items-center pt-5">
                <span className="text-base">Phần trăm giảm giá: </span>
                <span className="text-base ml-5">{sale?.discount} %</span>
              </div>
              <div className="flex items-center pt-5">
                <span className="text-base">Ngày bắt đầu: </span>
                <span className="text-base ml-5">{sale?.startDate}</span>
              </div>
              <div className="flex items-center pt-5">
                <span className="text-base">Ngày kết thúc: </span>
                <span className="text-base ml-5">{sale?.endDate}</span>
              </div>
              <div className="flex items-center pt-5">
                <span className="text-base">Trạng thái: </span>
                {sale?.isActive === 1 && <span className="text-base ml-5 text-green-500">Hoạt động</span>}
                {sale?.isActive === 0 && <span className="text-base ml-5 text-red-500">Đã khóa</span>}
              </div>
            </>
          )}
          <div className="flex pt-5">
            <span className="text-base w-[50%]">Khuyến mãi: </span>
            {product?.sale === 0 ? (
              <span className="text-base w-[50%]">Không có khuyến mãi</span>
            ) : (
              <span className="text-base w-[50%]">{sale?.name}</span>
            )}
          </div>
        </div>
      </div>
      <div className="flex items-center justify-around mt-5">
        <div className="w-[70%] flex flex-col border rounded-md p-5">
          <span className="text-lg font-semibold">Màu sắc và Size</span>
          <div className="w-full h-[1px] bg-black"></div>
          <div className="grid grid-cols-4 items-center">
            <span>Màu sắc</span>
            <span className="ml-2">Size</span>
            <span className="">Số lượng tồn</span>
            <span>Số lượng bán</span>
            {!!color &&
              !!color.length &&
              color.map((colorItem, colorIndex) => (
                <React.Fragment key={colorIndex}>
                  <span className="w-[50%] items-center">{colorItem.value}</span>
                  <div className="flex flex-col col-span-3">
                    {colorItem.sizes.map((sizeItem, sizeIndex) => (
                      <div key={sizeIndex} className="flex justify-between">
                        <div className="text-base">{sizeItem.value}</div>
                        <div className="">{sizeItem.total}</div>
                        <div className="w-[25%]">{sizeItem.sold}</div>
                      </div>
                    ))}
                  </div>
                </React.Fragment>
              ))}
          </div>
        </div>
        <div className="w-[25%] flex flex-col border rounded-md p-5 self-start">
          <span className="text-lg font-semibold">Danh mục</span>
          <div className="w-full h-[1px] bg-black"></div>
          <div className="flex items-center pt-5">
            <span className="text-base">Danh mục: </span>
            <span className="text-base ml-5">{category?.title}</span>
          </div>
        </div>
      </div>
      <div className="flex items-center justify-around mt-5">
        <div className="w-[70%] flex flex-col border rounded-md p-5">
          <span className="text-lg font-semibold">Hình ảnh</span>
          <div className="w-full h-[1px] bg-black"></div>
          <div className="flex items-center justify-around pt-5">
            <img src="" alt="ảnh 1" className="w-40 h-40 object-contain rounded" />
            <img src="" alt="ảnh 2" className="w-40 h-40 object-contain rounded" />
            <img src="" alt="ảnh 3" className="w-40 h-40 object-contain rounded" />
          </div>
        </div>
        <div className="w-[25%] flex flex-col p-5 self-start"></div>
      </div>
    </>
  );
};

export default DetailProduct;
