import React, { useState } from 'react';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Alert from '@mui/material/Alert';
import Collapse from '@mui/material/Collapse';
import Box from '@mui/material/Box';
import PropTypes from 'prop-types';
import signIn from '../services/sign-in';

function Login(props) {
  const { history } = props;
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorOpen, setErrorOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (username.length >= 18 || username.length <= 2) {
      setErrorOpen(true);
      setErrorMessage('Введите корректное имя пользователя');
      return;
    }
    if (password.length === 0) {
      setErrorOpen(true);
      setErrorMessage('Введите пароль');
      return;
    }
    if (password.length < 4) {
      setErrorOpen(true);
      setErrorMessage('Длина пароля должна быть не менее 4-х символов');
      return;
    }

    signIn(username, password)
      .then(() => {
        history.push('/');
      })
      .catch(() => {
        setErrorOpen(true);
        setErrorMessage('Такого пользователя не существует или пароль не верный');
        setPassword('');
      });
  };

  return (
    <Box
      display="flex"
      flexDirection="column"
      justifyContent="space-between"
      sx={{
        mt: 2,
        width: '25%',
        m: 'auto',
      }}
    >
      <Collapse in={errorOpen}>
        <Alert
          severity="error"
          sx={{ mb: 2 }}
        >
          {errorMessage}
        </Alert>
      </Collapse>
      <TextField
        id="usernameTextField"
        label="Имя пользователя"
        variant="filled"
        value={username}
        onChange={(e) => {
          setUsername(e.target.value);
          setErrorOpen(false);
        }}
        fullWidth
      />
      <TextField
        id="passwordTextField"
        label="Пароль"
        variant="filled"
        type="password"
        value={password}
        onChange={(e) => {
          setPassword(e.target.value);
          setErrorOpen(false);
        }}
        fullWidth
      />
      <Button
        id="loginButton"
        type="submit"
        variant="contained"
        color="primary"
        onClick={handleSubmit}
        fullWidth
      >
        Войти
      </Button>
    </Box>
  );
}

Login.propTypes = {
  history: PropTypes.shape({ push: PropTypes.func }).isRequired,
};

export default Login;
