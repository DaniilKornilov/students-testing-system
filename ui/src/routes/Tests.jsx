import { useLocation } from 'react-router-dom';
import React from 'react';
import axios from 'axios';
import { styled } from '@mui/styles';
import Collapse from '@mui/material/Collapse';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import FileUploadIcon from '@mui/icons-material/FileUpload';
import authHeader from '../services/auth-header';
import TestRecordsTable from '../components/TestRecordsTable';

const API_URL_TEST = 'http://localhost:8080/api/test';

const API_URL_TEST_BY_COURSE = 'http://localhost:8080/api/test/course/?course=';

function Tests() {
  const { state } = useLocation();
  const courseName = state.name;

  const [tests, setTests] = React.useState([]);
  const [errorOpen, setErrorOpen] = React.useState(false);
  const [errorMessage, setErrorMessage] = React.useState('');

  const getAllTests = () => {
    axios.get(API_URL_TEST, { headers: authHeader() })
      .then((res) => {
        setTests(res.data);
      });
  };

  const getAllTestsByCourse = (name) => {
    axios.get(API_URL_TEST_BY_COURSE + name, { headers: authHeader() })
      .then((res) => {
        setTests(res.data);
      });
  };

  React.useEffect(() => {
    getAllTestsByCourse(courseName);
  }, []);

  const Input = styled('input')({
    display: 'none',
  });

  const handleUpload = (e) => {
    e.preventDefault();
    setErrorOpen(false);
    const formData = new FormData();
    formData.append('file', e.target.files[0]);
    formData.append('courseName', courseName);
    const config = {
      headers: {
        'content-type': 'multipart/form-data',
        ...authHeader(),
      },
    };
    axios.post(API_URL_TEST, formData, config)
      .then(() => {
        getAllTests();
      })
      .catch(({ response }) => {
        setErrorOpen(true);
        setErrorMessage(response.data.message);
      });
  };

  return (
    <div>
      <Collapse in={errorOpen}>
        <Alert
          severity="error"
          sx={{ mb: 2 }}
        >
          {errorMessage}
        </Alert>
      </Collapse>
      <Box sx={{ display: 'flex' }}>
        <Box sx={{
          width: 1 / 4,
          flex: 1,
          marginTop: 3,
        }}
        >
          {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
          <label htmlFor="contained-button-file">
            <Input
              accept=".xlsx"
              id="contained-button-file"
              multiple
              type="file"
              onChange={handleUpload}
            />
            <Button
              component="span"
              variant="contained"
              endIcon={<FileUploadIcon />}
              color="primary"
            >
              Импорт
            </Button>
          </label>
        </Box>
      </Box>
      <TestRecordsTable
        tests={tests}
      />
    </div>
  );
}

export default Tests;
