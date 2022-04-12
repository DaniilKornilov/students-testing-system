import store from '../store';
import { userSignOut } from '../actions/authActionCreators';

export default function signOut() {
  store.dispatch(userSignOut());
}
