import axios from "axios";

export const updateReply = (replyId, data) => axios.put(`http://localhost:8080/api/reply/${replyId}`, data, {withCredentials: true});
export const deleteReply = (replyId) => axios.delete(`http://localhost:8080/api/reply/${replyId}`, {withCredentials: true});
export const likeReply = (replyId) => axios.post(`http://localhost:8080/api/reply/${replyId}/like`, {}, {withCredentials: true});
export const unlikeReply = (replyId) => axios.delete(`http://localhost:8080/api/reply/${replyId}/like`, {withCredentials: true});