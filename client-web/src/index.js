import React from 'react';
import ReactDOM from 'react-dom';
import log from 'loglevel';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

console.info("NODE_ENV=" + process.env.NODE_ENV);
console.info("REACT_APP_ENVIRONMENT=" + process.env.REACT_APP_ENVIRONMENT);

if (process.env.NODE_ENV === 'development' || process.env.NODE_ENV === 'test') {
  log.setLevel("info");
  // For Debug.
  // localStorage.removeItem("user");
} else {
  // Disable log at production
  console.log = console.error = console.warn = function () { };
  log.disableAll(true);
}

log.info("[index]: Begin ReactDOM.");

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
