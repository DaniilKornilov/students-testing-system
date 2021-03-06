import React from 'react';
import axios from 'axios';
import authHeader from '../services/auth-header';
import CourseRecordsTable from '../components/CourseRecordsTable';

const API_URL_COURSE = 'http://localhost:8080/api/course';

function Courses() {
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
      />
    </div>
  );
}

export default Courses;
