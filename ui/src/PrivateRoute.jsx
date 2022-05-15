import React from 'react';
import { Navigate } from 'react-router-dom';
import PropTypes from 'prop-types';
import getUser from './services/get-user';

function PrivateRoute({ children }) {
  if (!getUser()) {
    return <Navigate to="/signIn" replace />;
  }

  return children;
}

PrivateRoute.propTypes = {
  children: PropTypes.element.isRequired,
};

export default PrivateRoute;
