import { combineReducers } from "redux";
import { localizeReducer as localize } from "react-localize-redux";

import { redirect } from "./views/gamma-redirect/GammaRedirect.view.reducer";
import { toast } from "./views/gamma-toast/GammaToast.view.reducer";

import { createAccount } from "../use-cases/create-account/CreateAccount.reducer";

export const rootReducer = combineReducers({
  createAccount,
  redirect,
  toast,
  localize
});
