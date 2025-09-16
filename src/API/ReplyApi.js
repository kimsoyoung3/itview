import axios from "axios";

export const updateReply = (replyId, data) => axios.put(`${process.env.REACT_APP_API_URL}/api/reply/${replyId}`, data, {withCredentials: true});
export const deleteReply = (replyId) => axios.delete(`${process.env.REACT_APP_API_URL}/api/reply/${replyId}`, {withCredentials: true});
export const likeReply = (replyId) => axios.post(`${process.env.REACT_APP_API_URL}/api/reply/${replyId}/like`, {}, {withCredentials: true});
export const unlikeReply = (replyId) => axios.delete(`${process.env.REACT_APP_API_URL}/api/reply/${replyId}/like`, {withCredentials: true});