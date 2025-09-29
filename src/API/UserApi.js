import axios from "axios";

export const registerUser = (data) => axios.post(`${process.env.REACT_APP_API_URL}/api/user`, data, {withCredentials: true});
export const deleteUser = () => axios.delete(`${process.env.REACT_APP_API_URL}/api/user/delete`, {withCredentials: true});
export const loginUser = (data) => axios.post(`${process.env.REACT_APP_API_URL}/api/user/login`, data, {withCredentials: true});
export const getMyInfo = () => axios.get(`${process.env.REACT_APP_API_URL}/api/user/me`, {withCredentials: true});
export const emitterSubscribe = () => axios.get(`${process.env.REACT_APP_API_URL}/api/user/subscribe`, {withCredentials: true});
export const logoutUser = () => axios.post(`${process.env.REACT_APP_API_URL}/api/user/logout`, {}, {withCredentials: true});
export const createVerification = (data) => axios.post(`${process.env.REACT_APP_API_URL}/api/user/createVerification`, data, {withCredentials: true});
export const checkEmail = (data) => axios.post(`${process.env.REACT_APP_API_URL}/api/user/email`, data, {withCredentials: true});
export const checkVerification = (data) => axios.post(`${process.env.REACT_APP_API_URL}/api/user/checkVerification`, data, {withCredentials: true});
export const setPassword = (data) => axios.post(`${process.env.REACT_APP_API_URL}/api/user/setPW`, data, {withCredentials: true});
export const link = (data) => axios.post(`${process.env.REACT_APP_API_URL}/api/user/link?redirectURL=` + encodeURIComponent(data.redirectURL), {}, {withCredentials: true});
export const unlink = (data) => axios.post(`${process.env.REACT_APP_API_URL}/api/user/unlink?provider=` + encodeURIComponent(data.provider), {}, {withCredentials: true});

export const getNotification = (page) => axios.get(`${process.env.REACT_APP_API_URL}/api/user/notification`, {withCredentials: true});
export const getUserDetail = (id) => axios.get(`${process.env.REACT_APP_API_URL}/api/user/${id}`, {withCredentials: true});
export const updateUserProfile = (data) => axios.put(`${process.env.REACT_APP_API_URL}/api/user`, data, {withCredentials: true});
export const getUserRatingCount = (userId) => axios.get(`${process.env.REACT_APP_API_URL}/api/user/${userId}/rating`, {withCredentials: true});
export const getUserContentCount = (userId, contentType) => axios.get(`${process.env.REACT_APP_API_URL}/api/user/${userId}/content/${contentType}`, {withCredentials: true});
export const getUserContentRating = (userId, contentType, page, order) => axios.get(`${process.env.REACT_APP_API_URL}/api/user/${userId}/content/${contentType}/rating?page=${page}&order=${order}`, {withCredentials: true});
export const getUserContentRatingScore = (userId, contentType, page, score) => axios.get(`${process.env.REACT_APP_API_URL}/api/user/${userId}/content/${contentType}/rating/${score}?page=${page}`, {withCredentials: true});
export const getUserWishlist = (userId, contentType, page, order) => axios.get(`${process.env.REACT_APP_API_URL}/api/user/${userId}/content/${contentType}/wish?order=${order}&page=${page}`, {withCredentials: true});
export const getUserComment = (userId, contentType, page, order) => axios.get(`${process.env.REACT_APP_API_URL}/api/user/${userId}/comment/${contentType}?page=${page}&order=${order}`, {withCredentials: true});
export const getUserCollection = (userId, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/user/${userId}/collection?page=${page}`, {withCredentials: true});
export const getUserLikePerson = (userId, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/user/${userId}/like/person?page=${page}`, {withCredentials: true});
export const getUserLikeCollection = (userId, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/user/${userId}/like/collection?page=${page}`, {withCredentials: true});
export const getUserLikeComment = (userId, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/user/${userId}/like/comment?page=${page}`, {withCredentials: true});