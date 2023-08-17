import { combineReducers } from 'redux'
import ThemeReducer from "./ThemeReducer"

const Reducers = combineReducers({
  ThemeReducer
})
export type RootState = ReturnType<typeof Reducers>;
export default Reducers
