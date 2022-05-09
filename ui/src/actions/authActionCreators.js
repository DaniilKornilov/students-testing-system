import { SIGN_IN, SIGN_OUT } from './types';

export const userSignIn = (subject, authorities, token) => ({
  type: SIGN_IN,
  payload: {
    subject,
    authorities,
    token,
  },
});

export const userSignOut = () => ({
  type: SIGN_OUT,
});
