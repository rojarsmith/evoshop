import log from "loglevel";
import { createBrowserHistory } from 'history';
import { Router, Route, Switch } from 'react-router-dom';
import EcommercePage from "views/EcommercePage";

function App() {
  log.info(`[App]: Rendering App Component`);
  return (
    <Router history={createBrowserHistory()}>
      {
        <Switch>
          <Route path="/" exact component={EcommercePage} />
        </Switch>
      }
    </Router>
  );
}

export default App;
