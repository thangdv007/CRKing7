import { API_URL } from "~/constants/utils";

const Api = {
  //user
  getListUser : (page) => API_URL + `/api/admin/user?page=${page}`,
  hideUser: (userId, id) => API_URL + `/api/admin/user/hide?userId=${userId}&id=${id}`,
  showUser: (userId, id) => API_URL + `/api/admin/user/show?userId=${userId}&id=${id}`,
  addEmp: () => API_URL + `/api/admin/user/addEmp`,
  detailAcc: (id) => API_URL + `/api/user/${id}`,
  updateUser: (id) => API_URL +`/api/user/update/${id}`,
  changePassword: (id) => API_URL + `/api/user/changePassword/${id}`,
  //product
  getAllProduct: (page, keyword) => API_URL + `/api/admin/product?page=${page}&keyword=${keyword}`,
  createProduct : () => API_URL +`/api/admin/product/create`,
  updateProduct : () => API_URL +`/api/admin/product/update`,
  hideProduct : (id) => API_URL +`/api/admin/product/hide/${id}`,
  showProduct : (id) => API_URL +`/api/admin/product/show/${id}`,
  deleteProduct : (id) => API_URL +`/api/admin/product/delete/${id}`,
  detailProduct: (id) => API_URL + `/api/admin/product/${id}`,
  //category
  detailCategory: (id) => API_URL + `/api/category/${id}`,
  //sale
  getSale : (id) => API_URL + `/api/admin/sale/${id}`,
}
export default Api;