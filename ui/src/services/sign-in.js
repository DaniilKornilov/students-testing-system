import axios from 'axios';
import store from '../store';
import { userSignIn } from '../actions/authActionCreators';
import API_URL from './api-url';

export default function signIn(username, password) {
  return axios
    .post(`${API_URL}signIn`, {
      username,
      password,
    })
    .then((response) => {
      if (response.data.token) {
        store.dispatch(userSignIn(
          response.data.username,
          response.data.id,
          response.data.token,
        ));
      }
      return response;
    });
}
