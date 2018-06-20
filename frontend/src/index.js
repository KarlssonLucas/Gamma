import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import registerServiceWorker from "./registerServiceWorker";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import App from "./app/App";
import { ThemeProvider } from "styled-components";
import { initGlobals, createTheme, colors } from "styled-mdl";

import { configureStore } from "./redux/config/configStore";

initGlobals();

console.log(colors);

const theme = createTheme({
  colorPrimary: colors.lightBlue[500],
  colorPrimaryDark: colors.lightBlue[700],
  colorAccent: colors.orange[500],
  textColorSecondary: "#808080"
});

const store = configureStore({});

ReactDOM.render(
  <ThemeProvider theme={theme}>
    <Provider store={store}>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </Provider>
  </ThemeProvider>,
  document.getElementById("root")
);
registerServiceWorker();
