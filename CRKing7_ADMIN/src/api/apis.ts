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
  getProductSale: (id, keyword) => API_URL + `/api/admin/product/productSale?saleId=${id}&keyword=${keyword}`,
  getProductNoSale: (page, keyword) => API_URL + `/api/admin/product/productNoSale?page=${page}&keyword=${keyword}`,
  addProToSale: (id, productIds) => API_URL + `/api/admin/sale/add-product/${id}?productIds=${productIds}`,
  removeProSale: (id, productIds) => API_URL + `/api/admin/sale/remove-product/${id}?productIds=${productIds}`,
  //category
  detailCategory: (id) => API_URL + `/api/category/${id}`,
  getAllCategory: (page, keyword) => API_URL + `/api/admin/category?page=${page}&keyword=${keyword}`,
  getAllCategory2: () => API_URL + `/api/admin/category`,
  hideCategory: (id) => API_URL + `/api/admin/category/hideCategory/${id}`,
  showCategory: (id) => API_URL + `/api/admin/category/showCategory/${id}`,
  deleteCategory: (id) => API_URL + `/api/admin/category/delete/${id}`,
  getParentCategory: () => API_URL + `/api/admin/category/parentCategory`,
  createCategory: () => API_URL + `/api/admin/category/create`,
  updateCategory: (id) => API_URL + `/api/admin/category/update/${id}`,
  //sale
  getSale : (id) => API_URL + `/api/admin/sale/${id}`,
  getAllSale : () => API_URL + `/api/admin/sale`,
  getAllSaleByKeyWord: (page, keyword) => API_URL + `/api/admin/sale?page=${page}&keyword=${keyword}`,
  hideSale: (id) => API_URL + `/api/admin/sale/hide/${id}`,
  showSale: (id) => API_URL + `/api/admin/sale/show/${id}`,
  deleteSale: (id) => API_URL + `/api/admin/sale/delete/${id}`,
  createSale: () => API_URL + `/api/admin/sale/create`,
  updateSale: (id) => API_URL + `/api/admin/sale/update/${id}`,
  //banner
  getAllBanner: (page, keyword) => API_URL + `/api/admin/banner/allBanner?page=${page}&keyword=${keyword}`,
  createBanner : () => API_URL +`/api/admin/banner/create`,
  updateBanner : () => API_URL +`/api/admin/banner/update`,
  hideBanner : (id) => API_URL +`/api/admin/banner/hideBanner/${id}`,
  showBanner : (id) => API_URL +`/api/admin/banner/showBanner/${id}`,
  deleteBanner : (id) => API_URL +`/api/admin/banner/delete/${id}`,
  detailBanner: (id) => API_URL + `/api/admin/banner/${id}`,
}
export default Api;