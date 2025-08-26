import axios from "axios";

export const getContentDetail = (id) => axios.get(`http://localhost:8080/api/content/${id}`, {withCredentials: true});
export const getContentCredit = (id, page) => axios.get(`http://localhost:8080/api/content/${id}/credit?page=${page}`, {withCredentials: true});
export const postContentRating = (id, data) => axios.post(`http://localhost:8080/api/content/${id}/rating`, data, {withCredentials: true});
export const deleteRating = (id) => axios.delete(`http://localhost:8080/api/content/${id}/rating`, {withCredentials: true});
export const postContentComment = (id, data) => axios.post(`http://localhost:8080/api/content/${id}/comment`, data, {withCredentials: true});
export const putContentComment = (id, data) => axios.put(`http://localhost:8080/api/content/${id}/comment`, data, {withCredentials: true});
export const deleteContentComment = (id) => axios.delete(`http://localhost:8080/api/content/${id}/comment`, {withCredentials: true});