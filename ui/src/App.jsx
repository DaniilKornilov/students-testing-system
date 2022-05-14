import {
  Route, Switch,
} from 'react-router-dom';
import React from 'react';
import Header from './components/Header';
import Login from './routes/Login';
import Students from './routes/Students';
import Tests from './routes/Tests';
import Courses from './routes/Courses';

function App() {
  return (
    <div className="App">
      <Header />
      <Switch>
        <Route
          exact
          path="/signIn"
          render={() => <Login />}
        />
        <Route
          exact
          path="/"
          render={() => <Students />}
        />
        <Route
          exact
          path="/tests"
          render={() => <Tests />}
        />
        <Route
          exact
          path="/courses"
          render={() => <Courses />}
        />
      </Switch>
    </div>
  );
}

export default App;
