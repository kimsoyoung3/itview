import axios from "axios";

export const getContentDetail = (contentId) => axios.get(`http://localhost:8080/api/content/${contentId}`, {withCredentials: true});
export const getContentCredit = (contentId, page) => axios.get(`http://localhost:8080/api/content/${contentId}/credit?page=${page}`, {withCredentials: true});
export const postContentRating = (contentId, data) => axios.post(`http://localhost:8080/api/content/${contentId}/rating`, data, {withCredentials: true});
export const deleteRating = (contentId) => axios.delete(`http://localhost:8080/api/content/${contentId}/rating`, {withCredentials: true});
export const postContentComment = (contentId, data) => axios.post(`http://localhost:8080/api/content/${contentId}/comment`, data, {withCredentials: true});
export const getContentComment = (contentId) => axios.get(`http://localhost:8080/api/content/${contentId}/comment`, {withCredentials: true});
export const putContentComment = (commentId, data) => axios.put(`http://localhost:8080/api/content/${commentId}/comment`, data, {withCredentials: true});
export const deleteContentComment = (commentId) => axios.delete(`http://localhost:8080/api/content/${commentId}/comment`, {withCredentials: true});
// order: new, old, rating, like, reply
export const getContentCommentsPaged = (contentId, order = 'new', page = 1) => axios.get(`http://localhost:8080/api/content/${contentId}/comments?order=${order}&page=${page}`, {withCredentials: true});