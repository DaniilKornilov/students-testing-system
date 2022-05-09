import { SIGN_IN, SIGN_OUT } from '../actions/types';

export default function authReducer(state = { user: null }, action = {}) {
  const {
    type,
    payload,
  } = action;

  switch (type) {
    case SIGN_IN:
      return {
        ...state,
        user: {
          subject: payload.subject,
          authorities: payload.authorities,
          token: payload.token,
        },
      };

    case SIGN_OUT:
      return {
        ...state,
        user: null,
      };

    default:
      return state;
  }
}
