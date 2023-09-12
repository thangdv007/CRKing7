import Types from "../types";

const initialState = {
  cart: localStorage.getItem('cart') ? JSON.parse(localStorage.getItem('cart')) : [],
};

const CartReducer = (state = initialState, action) => {
  switch (action.type) {
    case Types.ADD_TO_CART:
      const updatedCartItems = action.payload;
      // Lưu cart vào localStorage
      localStorage.setItem('cart', JSON.stringify(updatedCartItems));
      return {
        ...state,
        cart: updatedCartItems,
      };

    case Types.REMOVE_FROM_CART:
      const filteredCartItems = state.cart.filter((item) => item.id !== action.payload);
      // Lưu cart vào localStorage
      localStorage.setItem('cart', JSON.stringify(filteredCartItems));
      return {
        ...state,
        cart: filteredCartItems,
      };

    case Types.CLEAR_CART:
      // Xóa toàn bộ giỏ hàng
      localStorage.removeItem('cart');
      return {
        ...state,
        cart: [],
      };

    default:
      return state;
  }
};

export default CartReducer;
