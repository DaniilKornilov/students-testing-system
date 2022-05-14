import React, { useState, useEffect } from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';
import Button from '@mui/material/Button';
import {
  Avatar, Box, Tooltip,
} from '@mui/material';
import PropTypes from 'prop-types';
import Collapse from '@mui/material/Collapse';
import { withRouter } from 'react-router-dom';
import store from '../store';
import getUser from '../services/get-user';
import signOut from '../services/sign-out';

function Header(props) {
  const { history } = props;
  const [anchorElUser, setAnchorElUser] = useState(null);
  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    store.subscribe(() => {
      setIsOpen(getUser() !== null);
    });
  }, [store]);

  const handleButtonClick = (pageURL) => {
    history.push(pageURL);
  };

  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseUserMenu = (pageURL) => {
    setAnchorElUser(null);
    history.push(pageURL);
  };

  const handleSignOut = () => {
    setAnchorElUser(null);
    signOut();
    history.push('/signIn');
  };

  const menuItems = [
    {
      menuTitle: 'Группы',
      pageURL: '/',
    },
    {
      menuTitle: 'Курсы',
      pageURL: '/courses',
    },
  ];

  const settingsItems = [
    {
      settingsTitle: 'Профиль',
      pageURL: '/personal-area',
      handler: handleCloseUserMenu,
    },
    {
      settingsTitle: 'Избранное',
      pageURL: '/favorites',
      handler: handleCloseUserMenu,
    },
    {
      settingsTitle: 'Выйти',
      pageURL: '/signIn',
      handler: handleSignOut,
    },
  ];

  return (
    <AppBar style={{ background: 'primary' }} position="static">
      <Toolbar>
        <Box sx={{
          flexGrow: 1,
          display: 'flex',
          ml: 0,
        }}
        >
          {menuItems.map((menuItem) => {
            const {
              menuTitle,
              pageURL,
            } = menuItem;
            return (
              <Box sx={{ px: 2 }} key={menuTitle}>
                <Button
                  id={menuTitle}
                  style={{
                    color: '#FFFFFF',
                    textTransform: 'none',
                    fontSize: '18px',
                  }}
                  onClick={() => handleButtonClick(pageURL)}
                >
                  {menuTitle}
                </Button>
              </Box>
            );
          })}
        </Box>
        <Box sx={{ flexGrow: 0 }}>
          <Collapse
            id="settingsCollapse"
            in={isOpen}
          >
            <Tooltip title="Открыть настройки">
              <IconButton
                id="settingsIconButton"
                onClick={handleOpenUserMenu}
                sx={{ p: 0 }}
              >
                <Avatar />
              </IconButton>
            </Tooltip>
            <Menu
              sx={{ mt: '45px' }}
              id="menu-appbar3"
              anchorEl={anchorElUser}
              anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              open={Boolean(anchorElUser)}
              onClose={handleCloseUserMenu}
            >
              {settingsItems.map((settingsItem) => {
                const {
                  settingsTitle,
                  pageURL,
                  handler,
                } = settingsItem;
                return (
                  <MenuItem id={settingsTitle} key={settingsTitle} onClick={() => handler(pageURL)}>
                    <Typography>{settingsTitle}</Typography>
                  </MenuItem>
                );
              })}
            </Menu>
          </Collapse>
        </Box>
      </Toolbar>
    </AppBar>
  );
}

Header.propTypes = {
  history: PropTypes.shape({ push: PropTypes.func }).isRequired,
};

export default withRouter(Header);
