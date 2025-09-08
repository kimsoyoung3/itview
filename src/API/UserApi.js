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
export const getUserRatingCount = (userId) => axios.get(`${process.env.REACT_APP_API_URL}/user/${userId}/rating`, {withCredentials: true});
export const getUserContentCount = (userId, contentType) => axios.get(`${process.env.REACT_APP_API_URL}/user/${userId}/content/${contentType}`, {withCredentials: true});
export const getUserContentRating = (userId, contentType, page, order) => axios.get(`${process.env.REACT_APP_API_URL}/user/${userId}/content/${contentType}/rating?page=${page}&order=${order}`, {withCredentials: true});
export const getUserContentRatingScore = (userId, contentType, page, score) => axios.get(`${process.env.REACT_APP_API_URL}/user/${userId}/content/${contentType}/rating/${score}?page=${page}`, {withCredentials: true});
export const getUserWishlist = (userId, contentType, page, order) => axios.get(`${process.env.REACT_APP_API_URL}/user/${userId}/content/${contentType}/wish?order=${order}&page=${page}`, {withCredentials: true});
export const getUserComment = (userId, contentType, page) => axios.get(`${process.env.REACT_APP_API_URL}/user/${userId}/comment/${contentType}?page=${page}`, {withCredentials: true});