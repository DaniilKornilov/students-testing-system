import { Redirect, useHistory } from 'react-router-dom';
import React from 'react';
import axios from 'axios';
import authHeader from '../services/auth-header';
import getUser from '../services/get-user';
import CourseRecordsTable from '../components/CourseRecordsTable';

const API_URL_COURSE = 'http://localhost:8080/api/course';

function CoursesImpl() {
  const history = useHistory();
  const [courses, setCourses] = React.useState([]);

  const getAllCourses = () => {
    axios.get(API_URL_COURSE, { headers: authHeader() })
      .then((res) => {
        setCourses(res.data);
      });
  };

  React.useEffect(() => {
    getAllCourses();
  }, []);

  return (
    <div>
      <CourseRecordsTable
        courses={courses}
        history={history}
      />
    </div>
  );
}

function Courses() {
  if (!getUser()) {
    return <Redirect to="/signIn" />;
  }
  return <CoursesImpl />;
}

export default Courses;
