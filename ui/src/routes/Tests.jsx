import { Redirect, useHistory } from 'react-router-dom';
import React from 'react';
import axios from 'axios';
import { styled } from '@mui/styles';
import Collapse from '@mui/material/Collapse';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import FileUploadIcon from '@mui/icons-material/FileUpload';
import {
  TextField,
} from '@mui/material';
import MenuItem from '@mui/material/MenuItem';
import CancelOutlinedIcon from '@mui/icons-material/CancelOutlined';
import authHeader from '../services/auth-header';
import getUser from '../services/get-user';
import TestRecordsTable from '../components/TestRecordsTable';

const API_URL_TEST = 'http://localhost:8080/api/test';

const API_URL_COURSE = 'http://localhost:8080/api/course';

const API_URL_TEST_BY_COURSE = 'http://localhost:8080/api/test/course/?course=';

function TestsImpl() {
  const history = useHistory();
  const courseName = history.location.name;

  const [courses, setCourses] = React.useState([]);
  const [courseComboBoxValue, setCourseComboBoxValue] = React.useState('');
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

  const getAllCourses = () => {
    axios.get(API_URL_COURSE, { headers: authHeader() })
      .then((res) => {
        setCourses(res.data);
      });
  };

  React.useEffect(() => {
    getAllTestsByCourse(courseName);
  }, []);

  React.useEffect(() => {
    getAllCourses();
  }, []);

  const handleClearFilters = () => {
    setErrorOpen(false);
    setCourseComboBoxValue('');
    getAllTests();
  };

  const handleChange = (e) => {
    e.preventDefault();
    setErrorOpen(false);
    axios.get(
      API_URL_TEST_BY_COURSE + e.target.value,
      { headers: authHeader() },
    )
      .then((res) => {
        setTests(res.data);
      });
    setCourseComboBoxValue(e.target.value);
  };

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
        <Box sx={{
          width: 1 / 4,
          flex: 1,
          marginTop: 3,
        }}
        >
          <TextField
            id="groupTextField"
            select
            label="Курс"
            value={courseComboBoxValue}
            onChange={handleChange}
            helperText="Выберите курс"
          >
            {courses.map((course) => (
              <MenuItem key={course.id} value={course.name}>
                {course.name}
              </MenuItem>
            ))}
          </TextField>
        </Box>
        <Box sx={{
          width: 1 / 4,
          flex: 1,
          marginTop: 3,
        }}
        >
          <Button
            id="clearButton"
            variant="contained"
            onClick={handleClearFilters}
            endIcon={<CancelOutlinedIcon />}
            color="primary"
          >
            Сбросить фильтр
          </Button>
        </Box>
      </Box>
      <TestRecordsTable
        tests={tests}
      />
    </div>
  );
}

function Tests() {
  if (!getUser()) {
    return <Redirect to="/signIn" />;
  }
  return <TestsImpl />;
}

export default Tests;
