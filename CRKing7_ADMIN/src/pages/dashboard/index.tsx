import React from 'react';
import Chart from 'react-apexcharts';
import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import Badge from '~/components/badge';
import StatusCard from '~/components/statuscard';
import Table from '~/components/table';
import { RootState } from '~/redux/reducers';
import { toast } from 'react-toastify';
import Api from '~/api/apis';
import { REQUEST_API } from '~/constants/method';

const chartOptions = {
  series: [
    {
      name: 'Online Customers',
      data: [40, 70, 20, 90, 36, 80, 30, 91, 60],
    },
    {
      name: 'Store Customers',
      data: [40, 30, 70, 80, 40, 16, 40, 20, 51, 10],
    },
  ],
  options: {
    colors: ['#6ab04c', '#2980b9'],
    chart: {
      background: 'transparent',
    },
    dataLabels: {
      enabled: false,
    },
    stroke: {
      curve: 'smooth',
    },
    xaxis: {
      categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep'],
    },
    legend: {
      position: 'top',
    },
    grid: {
      show: false,
    },
  },
};

const topCustomers = {
  head: ['user', 'total orders', 'total spending'],
  body: [
    {
      username: 'john doe',
      order: '490',
      price: '$15,870',
    },
    {
      username: 'frank iva',
      order: '250',
      price: '$12,251',
    },
    {
      username: 'anthony baker',
      order: '120',
      price: '$10,840',
    },
    {
      username: 'frank iva',
      order: '110',
      price: '$9,251',
    },
    {
      username: 'anthony baker',
      order: '80',
      price: '$8,840',
    },
  ],
};

const renderCustomerHead = (item: string, index: number) => <th key={index}>{item}</th>;

const renderCustomerBody = (item: any, index: number) => (
  <tr key={index}>
    <td>{item.username}</td>
    <td>{item.order}</td>
    <td>{item.price}</td>
  </tr>
);

const latestOrders = {
  header: ['order id', 'user', 'total price', 'date', 'status'],
  body: [
    {
      id: '#OD1711',
      user: 'john doe',
      date: '17 Jun 2021',
      price: '$900',
      status: 'shipping',
    },
    // ... other data
  ],
};

const orderStatus = {
  shipping: 'primary',
  pending: 'warning',
  paid: 'success',
  refund: 'danger',
};

const renderOrderHead = (item: string, index: number) => <th key={index}>{item}</th>;

const renderOrderBody = (item: any, index: number) => (
  <tr key={index}>
    <td>{item.id}</td>
    <td>{item.user}</td>
    <td>{item.price}</td>
    <td>{item.date}</td>
    <td>
      <Badge type={orderStatus[item.status]} content={item.status} />
    </td>
  </tr>
);
const Dashboard = () => {
  const themeReducer = useSelector((state: RootState) => state.ThemeReducer.mode);
  const token = useSelector((state: RootState) => state.ReducerAuth.token);
  const [totalProductSold, setTotalProductSold] = React.useState();
  const getTotalProductSold = async () => {
    if (!!token) {
      try {
        const url = Api.getTotalSoldProduct();
        const [res] = await Promise.all([
          REQUEST_API({
            url: url,
            method: 'get',
            token: token,
          }),
        ]);
        console.log(res);

        if (res.status) {
          setTotalProductSold(res.data);
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
    getTotalProductSold();
  }, []);
  const statusCards = [
    {
      icon: 'bx bx-shopping-bag',
      title: 'Tổng số đơn hàng hoàn thành',
      count: totalProductSold || '',
    },
    {
      icon: 'bx bx-cart',
      count: '2,001',
      title: 'Daily visits',
    },
    {
      icon: 'bx bx-dollar-circle',
      count: '$2,632',
      title: 'Tổng thu nhập',
    },
    {
      icon: 'bx bx-receipt',
      count: '1,711',
      title: 'Total orders',
    },
  ];
  return (
    <div>
      <h2 className="page-header text-xl font-bold">Trang tổng quan</h2>
      <div className="row">
        <div className="col-6">
          <div className="row">
            {statusCards.map((item, index) => (
              <div className="col-6" key={index}>
                <StatusCard icon={item.icon} count={item.count} title={item.title} />
              </div>
            ))}
          </div>
        </div>
        <div className="col-6">
          <div className="card full-height">
            {/* chart */}
            <Chart
              options={
                themeReducer === 'theme-mode-dark'
                  ? {
                      ...chartOptions.options,
                      theme: { mode: 'dark' },
                    }
                  : {
                      ...chartOptions.options,
                      theme: { mode: 'light' },
                    }
              }
              series={chartOptions.series}
              type="line"
              height="100%"
            />
          </div>
        </div>
        <div className="col-4">
          <div className="card">
            <div className="card__header">
              <h3>top customers</h3>
            </div>
            <div className="card__body">
              <Table
                headData={topCustomers.head}
                renderHead={(item, index) => renderCustomerHead(item, index)}
                bodyData={topCustomers.body}
                renderBody={(item, index) => renderCustomerBody(item, index)}
              />
            </div>
            <div className="card__footer">
              <Link to="/">view all</Link>
            </div>
          </div>
        </div>
        <div className="col-8">
          <div className="card">
            <div className="card__header">
              <h3>latest orders</h3>
            </div>
            <div className="card__body">
              <Table
                headData={latestOrders.header}
                renderHead={(item, index) => renderOrderHead(item, index)}
                bodyData={latestOrders.body}
                renderBody={(item, index) => renderOrderBody(item, index)}
              />
            </div>
            <div className="card__footer">
              <Link to="/">view all</Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
