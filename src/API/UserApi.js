import axios from "axios";

export const registerUser = (data) => axios.post('http://localhost:8080/api/user', data, {withCredentials: true});
export const loginUser = (data) => axios.post('http://localhost:8080/api/user/login', data, {withCredentials: true});
export const getMyInfo = () => axios.get('http://localhost:8080/api/user/me', {withCredentials: true});
export const logoutUser = () => axios.post('http://localhost:8080/api/user/logout', {}, {withCredentials: true});
export const createVerification = (data) => axios.post('http://localhost:8080/api/user/createVerification', data, {withCredentials: true});
export const checkEmail = (data) => axios.post('http://localhost:8080/api/user/email', data, {withCredentials: true});
export const checkVerification = (data) => axios.post('http://localhost:8080/api/user/checkVerification', data, {withCredentials: true});