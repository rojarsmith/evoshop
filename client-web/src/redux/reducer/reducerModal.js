import { actionModals } from "../action";
import log from "loglevel";

const initialState = {
    simpleOpen: false,
};

export function reducerModal(state = initialState, action) {
    let atLeastOneOpened = false;
    for (let [key, value] of Object.entries(state)) {
        if (key && value === true) {
            atLeastOneOpened = true;
            break;
        }
    }

    switch (action.type) {
        case actionModals.OPEN_SIMPLE_MODAL:
            log.debug(`[OPEN_SIMPLE_MODAL]: action.payload = ${JSON.stringify(action.payload)}`)
            if (!atLeastOneOpened) state.simpleOpen = true;
            state.payload = action.payload;
            return state;
        case actionModals.CLOSE_SIMPLE_MODAL:
            state.simpleOpen = false;
            return state;
        default:
            return state;
    }
}
