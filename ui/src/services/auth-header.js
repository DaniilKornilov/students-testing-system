import getUser from './get-user';

export default function authHeader() {
  const user = getUser();

  if (user && user.token) {
    return { Authorization: `Bearer ${user.token}` };
  }
  return {};
}
