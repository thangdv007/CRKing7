const path = {
  login: "/login",
  logout: "/logout",
  forgotpass: "/forgot-password",
  addEmp : '/add-emp',
  home : "/",
  accounts : "/accounts",
  products : "/products",
  orders : "/orders",
  analytics : "/analytics",
  categories : "/categories",
  sale : "/sale",
  article : '/article',
  chat : '/chat',
  settings : "/settings",
  detailAcc : '/detailAcc',
  profile: '/profile',
  banners: '/banners',
  //product
  addProduct : '/add-product',
  detailProduct: '/detail-product',
  editProduct: '/edit-product',
  //category
  addCategory: '/add-category',
  detailCategory: '/detail-category',
  editCategory: '/edit-category',
  //banner
  addBanner: '/add-banner',
  detailBanner: '/detail-banner',
  editBanner: '/edit-banner',
  //sale
  addSale: '/add-sale',
  detailSale: '/deail-sale',
  editSale: '/edit-sale',
  addProductToSale: '/add-product-sale',
  
} as const;


export default path;