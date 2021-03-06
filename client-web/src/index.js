import React from 'react';
import ReactDOM from 'react-dom';
import log from 'loglevel';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { HelmetProvider } from 'react-helmet-async';
//Redux
import { Provider } from 'react-redux'
import { createStore, applyMiddleware } from 'redux';
import { createLogger } from 'redux-logger';
import thunk from 'redux-thunk';
import allReducer from "redux/reducer"

// import 'swiper/swiper-bundle.css'
import 'swiper/css/swiper.min.css'
// import 'swiper/components/pagination/pagination.min.css'
// import 'swiper/components/thumbs/thumbs.min.css'
// import 'assets/styles/library/swiper/swiper.css'
// import 'assets/styles/library/swiper/swiper.min.css'


console.info("NODE_ENV=" + process.env.NODE_ENV);
console.info("REACT_APP_ENVIRONMENT=" + process.env.REACT_APP_ENVIRONMENT);

//Redux
const logger = createLogger();

let middleware = [thunk];

if (process.env.NODE_ENV === 'development') {
  middleware = [...middleware, logger];
}

const store = createStore(allReducer, applyMiddleware(...middleware));

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
    <Provider store={store}>
      <HelmetProvider>
        <App />
      </HelmetProvider>
    </Provider>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
