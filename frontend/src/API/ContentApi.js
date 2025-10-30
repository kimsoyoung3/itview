import axios from "axios";

export const getContentTitle = (contentId) => axios.get(`${process.env.REACT_APP_API_URL}/api/content/${contentId}/title`, {withCredentials: true});
export const searchContent = (title, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/content/search?title=${title}&page=${page}`, {withCredentials: true});
export const getContentDetail = (contentId) => axios.get(`${process.env.REACT_APP_API_URL}/api/content/${contentId}`, {withCredentials: true});
export const getContentCredit = (contentId, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/content/${contentId}/credit?page=${page}`, {withCredentials: true});
export const postContentRating = (contentId, data) => axios.post(`${process.env.REACT_APP_API_URL}/api/content/${contentId}/rating`, data, {withCredentials: true});
export const deleteRating = (contentId) => axios.delete(`${process.env.REACT_APP_API_URL}/api/content/${contentId}/rating`, {withCredentials: true});
export const wishContent = (contentId) => axios.post(`${process.env.REACT_APP_API_URL}/api/content/${contentId}/wish`, {}, {withCredentials: true});
export const unwishContent = (contentId) => axios.delete(`${process.env.REACT_APP_API_URL}/api/content/${contentId}/wish`, {withCredentials: true});
export const postContentComment = (contentId, data) => axios.post(`${process.env.REACT_APP_API_URL}/api/content/${contentId}/comment`, data, {withCredentials: true});
export const getContentComment = (contentId) => axios.get(`${process.env.REACT_APP_API_URL}/api/content/${contentId}/comment`, {withCredentials: true});
// order: new, old, rating, like, reply
export const getContentCommentsPaged = (contentId, order = 'new', page = 1) => axios.get(`${process.env.REACT_APP_API_URL}/api/content/${contentId}/comments?order=${order}&page=${page}`, {withCredentials: true});