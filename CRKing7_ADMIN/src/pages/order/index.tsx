import React from 'react';
import { useSelector } from 'react-redux';
import Api from '~/api/apis';
import { toast } from 'react-toastify';
import { REQUEST_API } from '~/constants/method';
import { RootState } from '~/redux/reducers';
import { useNavigate } from 'react-router-dom';
import path from '~/constants/path';
import { formatPrice } from '~/constants/utils';
import SpinLoading from '~/components/loading/spinLoading';
import { Order } from '~/types/order.type';
import { City, District, Ward } from '~/types/province.type';
import provinceApi from '~/api/province.apis';

const Order = () => {
  const token = useSelector((state: RootState) => state.ReducerAuth.token);
  const user = useSelector((state: RootState) => state.ReducerAuth.user);
  const [page, setPage] = React.useState(0);
  const [lastPage, setLastPage] = React.useState(0);
  const [keyword, setKeyword] = React.useState('');
  const navigate = useNavigate();
  const [loadding, setLoading] = React.useState(false);
  const [orders, setOrders] = React.useState<Order[]>([]);

  const getAllOrders = async () => {
    if (!!token) {
      try {
        setLoading(true);
        const currentPage = 0;
        setPage(currentPage);
        const url = Api.getAllOrders(currentPage, keyword);
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
          setOrders(newData);
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
    getAllOrders();
  }, []);
  const totalCost = (orderId) => {
    const order = orders.find((o) => o.id === orderId);
    if (!order) {
      return 0; // Không tìm thấy đơn hàng, tổng tiền là 0
    }
    const itemTotalCost = order.items.reduce((total, item) => {
      const itemCost = item.sellPrice * item.quantity;
      return total + itemCost;
    }, 0);
    const totalCost = itemTotalCost + order.shippingFee;
    return totalCost;
  };
  const [cityMap, setCityMap] = React.useState({});
  const [wardMap, setWardMap] = React.useState({});
  const [districtMap, setDistrictMap] = React.useState({});

  const getCities = async () => {
    try {
      const res = await provinceApi.cityApi();
      console.log(res);

      if (res.status === 200) {
        const newCity = res.data.map((city) => ({
          code: city.code,
          name: city.name,
        }));
        newCity.forEach((city) => {
          setCityMap((prevMapping) => ({
            ...prevMapping,
            [city.code]: city.name,
          }));
        });
      }
    } catch (error) {
      console.error(error);
    }
  };
  const getDistricts = async (id) => {
    try {
      const res = await provinceApi.districtApi(id);
      if (res.status === 200) {
        const newDistrict = res.data.districts.map((district) => ({
          code: district.code,
          name: district.name,
        }));
        newDistrict.forEach((district) => {
          setDistrictMap((prevMapping) => ({
            ...prevMapping,
            [district.code]: district.name,
          }));
        });
      }
    } catch (error) {
      console.error(error);
    }
  };
  const getWards = async (id) => {
    try {
      const res = await provinceApi.wardApi(id);

      if (res.status === 200) {
        const newWards = res.data.wards.map((ward) => ({
          code: ward.code,
          name: ward.name,
        }));
        newWards.forEach((ward) => {
          setWardMap((prevMapping) => ({
            ...prevMapping,
            [ward.code]: ward.name,
          }));
        });
      }
    } catch (error) {
      console.error(error);
    }
  };
  React.useEffect(() => {
    getCities();
  }, []);
  React.useEffect(() => {
    if (orders.length > 0) {
      orders.forEach(async (item) => {
        if (item.district !== null && item.district !== undefined && !Number.isNaN(item.district)) {
          await getWards(parseInt(item.district));
        }
      });
    }
  }, [orders]);
  React.useEffect(() => {
    if (orders.length > 0) {
      orders.forEach(async (item) => {
        if (item.province !== null && item.province !== undefined && !Number.isNaN(item.province)) {
          await getDistricts(parseInt(item.province));
        }
      });
    }
  }, [orders]);
  return (
    <div className="">
      <div className="flex justify-between items-center">
        <div className="flex items-center">
          <span className="text-base font-bold">Quản lý đơn hàng</span>
          <div className="flex ml-5 items-center justify-center">
            <input
              className="h-10 border-black rounded-lg pl-3"
              placeholder="Nhập mã đơn hàng ..."
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
            />
            <i
              className="bx bx-search text-2xl text-blue ml-3 cursor-pointer"
              onClick={() => {
                getAllOrders(), setKeyword('');
              }}
            ></i>
          </div>
        </div>
        <div className="w-auto px-2 py-1 cursor-pointer flex justify-center items-center bg-blue rounded-md">
          <i className="bx bxs-plus-circle text-2xl text-white"></i>
        </div>
      </div>
      <div className="w-full h-[2px] bg-black mt-5"></div>
      <div className="overflow-x-auto w-full">
        <table className="table w-full">
          <thead>
            <tr>
              <th className="w-[10%] text-center">Mã đơn hàng</th>
              <th className="w-[15%] text-center">Người nhận</th>
              <th className="w-[15%] text-center">Số điện thoại</th>
              <th className="w-[15%] text-center">Địa chỉ</th>
              <th className="w-[10%] text-center">Đơn giá</th>
              <th className="w-[10%] text-center">Thanh toán</th>
              <th className="w-[10%] text-center">Trạng thái</th>
              <th className="w-[10%] text-center">Ngày giao</th>
              <th className="w-[10%] text-center"></th>
            </tr>
          </thead>
          <tbody>
            {!!orders &&
              !!orders.length &&
              orders.map((item, i) => {
                return (
                  <tr key={i} className="cursor-pointer">
                    <td className="text-center">{item.codeOrders}</td>
                    <td className="text-center">{item.fullName}</td>
                    <td className="text-center">{item.phone}</td>
                    <td className="text-center">
                      {item.addressDetail}, {wardMap[item.wards]}, {districtMap[item.district]},{' '}
                      {cityMap[item.province]}
                    </td>
                    <td className="text-center">{formatPrice(totalCost(item.id))}</td>
                    <td className={item.isCheckout ? `text-center text-green-500` : `text-red-500 text-center`}>
                      {item.isCheckout ? `Đã thanh toán` : `Chưa thanh toán`}
                    </td>
                    {item.status === 1 && <td className="text-orange-500 text-center">Chờ xác nhận</td>}
                    {item.status === 2 && <td className="text-green-500 text-center">Đã xác nhận</td>}
                    {item.status === 3 && <td className="text-cyan-500 text-center">Đang vận chuyển</td>}
                    {item.status === 4 && <td className="text-blue text-center">Đã giao</td>}
                    {item.status === 5 && <td className="text-red-500 text-center">Không nhận hàng</td>}
                    {item.status === 0 && <td className="text-red-500 text-center">Đã hủy</td>}
                    <td className="text-center">{item.shipDate ? `${item.shipDate}` : `Chưa xác định`}</td>
                    <td className="flex flex-col items-center justify-between ">
                      <i
                        className="bx bxs-show text-2xl font-semibold text-blue"
                        onClick={() => navigate(path.detailOrder, { state: item.id })}
                      ></i>
                      <i
                        className="bx bxs-pencil text-2xl font-semibold text-blue pt-2"
                        onClick={() => navigate(path.editOrder, { state: item.id })}
                      ></i>
                    </td>
                  </tr>
                );
              })}
          </tbody>
        </table>
      </div>

      {loadding && <SpinLoading />}
    </div>
  );
};

export default Order;
