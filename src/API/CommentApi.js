import axios from "axios";

export const getCommentAndContent = (commentId) => axios.get(`http://localhost:8080/api/comment/${commentId}`, {withCredentials: true});
export const updateComment = (commentId, data) => axios.put(`http://localhost:8080/api/comment/${commentId}`, data, {withCredentials: true});
export const deleteComment = (commentId) => axios.delete(`http://localhost:8080/api/comment/${commentId}`, {withCredentials: true});
export const likeComment = (commentId) => axios.post(`http://localhost:8080/api/comment/${commentId}/like`, {}, {withCredentials: true});
export const unlikeComment = (commentId) => axios.delete(`http://localhost:8080/api/comment/${commentId}/like`, {withCredentials: true});
export const postReply = (commentId, data) => axios.post(`http://localhost:8080/api/comment/${commentId}/reply`, data, {withCredentials: true});
export const getCommentRepliesPaged = (commentId, page) => axios.get(`http://localhost:8080/api/comment/${commentId}/reply?page=${page}`, {withCredentials: true});