export interface Order {
  addressDetail: string;
  codeOrders: string;
  createDate: string;
  district: string;
  fullName: string;
  id: number;
  isCheckout: boolean;
  items: OrderItem;
  modifiedDate: string;
  note: null | string;
  paymentMethod: string;
  phone: string;
  province: string;
  shipDate: null | string;
  shippingFee: number;
  status: number;
  type: number;
  user: number;
  userNameEmp: null | string;
  wards: string;
}
export interface OrderItem {
  id: number;
  productName: string;
  quantity: number;
  sellPrice: number;
  valueColor: string;
  valueSize: string;
}
// const bb ={

// }