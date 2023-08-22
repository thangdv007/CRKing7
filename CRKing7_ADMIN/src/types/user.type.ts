export interface User {
  id: number;
  username: string;
  password: string;
  email: string;
  otp: string;
  firstName: string;
  lastName: string;
  phone: string;
  image: string;
  createdDate: string;
  modifiedDate: string;
  roles: {
    name: string;
  }[];
  status: number;
  addresses: {}[];
}
// const bb = {
//   "createdDate": "15:25:21 10-08-2023",
//                 "modifiedDate": "15:25:21 10-08-2023",
//                 "roles": [
//                     {
//                         "name": "ROLE_USER"
//                     }
//                 ],
//                 "status": 1,
//                 "addresses": []
// }