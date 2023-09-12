import Types from "../types";

const ActionAuth = {
  Login : (value : any) => {
    return {
      type : Types.LOGIN,
      value,
    }
  },
  Logout : () => {
    return {
      type : Types.LOGOUT,
    }
  }
}

export default ActionAuth;