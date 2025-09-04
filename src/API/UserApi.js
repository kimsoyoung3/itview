import axios from "axios";

export const registerUser = (data) => axios.post(`${process.env.REACT_APP_API_URL}/user`, data, {withCredentials: true});
export const loginUser = (data) => axios.post(`${process.env.REACT_APP_API_URL}/user/login`, data, {withCredentials: true});
export const getMyInfo = () => axios.get(`${process.env.REACT_APP_API_URL}/user/me`, {withCredentials: true});
export const logoutUser = () => axios.post(`${process.env.REACT_APP_API_URL}/user/logout`, {}, {withCredentials: true});
export const createVerification = (data) => axios.post(`${process.env.REACT_APP_API_URL}/user/createVerification`, data, {withCredentials: true});
export const checkEmail = (data) => axios.post(`${process.env.REACT_APP_API_URL}/user/email`, data, {withCredentials: true});
export const checkVerification = (data) => axios.post(`${process.env.REACT_APP_API_URL}/user/checkVerification`, data, {withCredentials: true});
export const setPassword = (data) => axios.post(`${process.env.REACT_APP_API_URL}/user/setPW`, data, {withCredentials: true});
export const link = (data) => axios.post(`${process.env.REACT_APP_API_URL}/user/link?redirectURL=` + encodeURIComponent(data.redirectURL), {}, {withCredentials: true});
export const getUserDetail = (id) => axios.get(`${process.env.REACT_APP_API_URL}/user/${id}`, {withCredentials: true});
export const updateUserProfile = (data) => axios.put(`${process.env.REACT_APP_API_URL}/user`, data, {withCredentials: true});