import { postRequest } from "../utils/api";
import { ADMIN_USERS_ENDPOINT } from "../utils/endpoints";

/**
 * {
 *      TODO
 * }
 */
export function addUser(userData) {
    return postRequest(ADMIN_USERS_ENDPOINT, userData);
}
