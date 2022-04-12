import {
  Route, Switch,
} from 'react-router-dom';
import React from 'react';
import Header from './components/Header';
import Login from './routes/Login';
import Home from './routes/Home';

function App() {
  return (
    <div className="App">
      <Header />
      <Switch>
        <Route
          exact
          path="/signIn"
          /* eslint-disable-next-line react/jsx-props-no-spreading */
          render={(props) => <Login {...props} />}
        />
        <Route
          exact
          path="/"
          render={() => <Home />}
        />
      </Switch>
    </div>
  );
}

export default App;
