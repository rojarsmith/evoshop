import log from "loglevel";
import { createBrowserHistory } from 'history';
import { Router, Route, Switch } from 'react-router-dom';

function App() {
  log.info(`[App]: Rendering App Component`);
  return (
    <Router history={createBrowserHistory()}>
      {
        <Switch>
          
        </Switch>
      }
    </Router>
  );
}

export default App;
