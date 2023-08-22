import { SuccessResponse } from "./ultis.type";
import { User } from "./user.type";

export type AuthResponse = SuccessResponse<{
  token: string;
  user: User;
}>;