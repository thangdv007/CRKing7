import Types from "../types";

const userLocal: string | null = localStorage.getItem('user');
const initialState = {
  user: userLocal ? JSON.parse(userLocal) : null,
  token: localStorage.getItem('token') || "",
};

const AuthReducer = (state = initialState, action) => {
  const { type, value } = action;
  switch (type) {
    case Types.LOGIN:
      return {
        user: value.user,
        token: value.token,
      };
    case Types.LOGOUT:
      return {
        user: null,
        token: "",
      };

    default: {
      return state;
    }
  }
};

export default AuthReducer;