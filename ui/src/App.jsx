import {
  BrowserRouter,
  Route,
  Routes,
} from 'react-router-dom';
import React from 'react';
import Header from './components/Header';
import Login from './routes/Login';
import Students from './routes/Students';
import Tests from './routes/Tests';
import Courses from './routes/Courses';
import PrivateRoute from './PrivateRoute';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Header />
        <Routes>
          <Route
            path="/signIn"
            element={<Login />}
          />
          <Route
            path="/"
            element={(
              <PrivateRoute>
                <Students />
              </PrivateRoute>
            )}
          />
          <Route
            path="/tests"
            element={(
              <PrivateRoute>
                <Tests />
              </PrivateRoute>
            )}
          />
          <Route
            path="/courses"
            element={(
              <PrivateRoute>
                <Courses />
              </PrivateRoute>
            )}
          />
          <Route
            path="*"
            element={
              <p>Not found: 404!</p>
            }
          />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
