import React, { useEffect } from 'react';
import Box from '@mui/material/Box';
import { TextField } from '@mui/material';
import Button from '@mui/material/Button';
import CancelOutlinedIcon from '@mui/icons-material/CancelOutlined';
import FileUploadIcon from '@mui/icons-material/FileUpload';
import axios from 'axios';
import { Redirect } from 'react-router-dom';
import MenuItem from '@mui/material/MenuItem';
import { styled } from '@mui/styles';
import Alert from '@mui/material/Alert';
import Collapse from '@mui/material/Collapse';
import authHeader from '../services/auth-header';
import getUser from '../services/get-user';
import StudentRecordsTable from '../components/StudentRecordsTable';

const API_URL_STUDENT = 'http://localhost:8080/api/student';

const API_URL_GROUP = 'http://localhost:8080/api/group';

const API_URL_STUDENT_BY_GROUP = 'http://localhost:8080/api/student/group/?group=';

const API_URL_STUDENT_IMPORT = 'http://localhost:8080/api/student';

function HomeImpl() {
  const [groups, setGroups] = React.useState([]);
  const [groupComboBoxValue, setGroupComboBoxValue] = React.useState('');
  const [students, setStudents] = React.useState([]);
  const [errorOpen, setErrorOpen] = React.useState(false);
  const [errorMessage, setErrorMessage] = React.useState('');

  const getAllStudents = () => {
    axios.get(API_URL_STUDENT, { headers: authHeader() })
      .then((res) => {
        setStudents(res.data);
      });
  };

  const getAllGroups = () => {
    axios.get(API_URL_GROUP, { headers: authHeader() })
      .then((res) => {
        setGroups(res.data);
      });
  };

  useEffect(() => {
    getAllStudents();
  }, []);

  useEffect(() => {
    getAllGroups();
  }, []);

  const handleClearFilters = () => {
    setErrorOpen(false);
    setGroupComboBoxValue('');
    getAllStudents();
  };

  const handleChange = (e) => {
    e.preventDefault();
    setErrorOpen(false);
    axios.get(
      API_URL_STUDENT_BY_GROUP + e.target.value,
      { headers: authHeader() },
    )
      .then((res) => {
        setStudents(res.data);
      });
    setGroupComboBoxValue(e.target.value);
  };

  const Input = styled('input')({
    display: 'none',
  });

  const handleUpload = (e) => {
    e.preventDefault();
    setErrorOpen(false);
    const formData = new FormData();
    formData.append('file', e.target.files[0]);
    formData.append('teacherUsername', getUser().subject);
    const config = {
      headers: {
        'content-type': 'multipart/form-data',
        ...authHeader(),
      },
    };
    axios.post(API_URL_STUDENT_IMPORT, formData, config)
      .then((res) => {
        setStudents(res.data);
        getAllGroups();
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
            label="Группа"
            value={groupComboBoxValue}
            onChange={handleChange}
            helperText="Выберите группу"
          >
            {groups.map((group) => (
              <MenuItem key={group.id} value={group.name}>
                {group.name}
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
      <StudentRecordsTable
        students={students}
      />
    </div>
  );
}

function Home() {
  if (!getUser()) {
    return <Redirect to="/signIn" />;
  }

  return <HomeImpl />;
}

export default Home;
