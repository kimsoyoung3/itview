import axios from "axios";

export const getCommentAndContent = (commentId) => axios.get(`${process.env.REACT_APP_API_URL}/api/comment/${commentId}`, {withCredentials: true});
export const updateComment = (commentId, data) => axios.put(`${process.env.REACT_APP_API_URL}/api/comment/${commentId}`, data, {withCredentials: true});
export const deleteComment = (commentId) => axios.delete(`${process.env.REACT_APP_API_URL}/api/comment/${commentId}`, {withCredentials: true});
export const likeComment = (commentId) => axios.post(`${process.env.REACT_APP_API_URL}/api/comment/${commentId}/like`, {}, {withCredentials: true});
export const unlikeComment = (commentId) => axios.delete(`${process.env.REACT_APP_API_URL}/api/comment/${commentId}/like`, {withCredentials: true});
export const postReply = (commentId, data) => axios.post(`${process.env.REACT_APP_API_URL}/api/comment/${commentId}/reply`, data, {withCredentials: true});
export const getCommentRepliesPaged = (commentId, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/comment/${commentId}/reply?page=${page}`, {withCredentials: true});