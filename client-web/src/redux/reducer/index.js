import { combineReducers } from 'redux';
import { reducerModal } from "./reducerModal";

const allReducer = combineReducers({
    reducerModal: reducerModal
});

export default allReducer;