import { SIGN_IN, SIGN_OUT } from './types';

export const userSignIn = (phoneNumber, userId, token) => ({
  type: SIGN_IN,
  payload: {
    phoneNumber,
    userId,
    token,
  },
});

export const userSignOut = () => ({
  type: SIGN_OUT,
});
