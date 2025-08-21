import axios from "axios";

export const getContentDetail = (id) => axios.get(`http://localhost:8080/api/content/${id}`, {withCredentials: true});
export const getContentCredit = (id, page) => axios.get(`http://localhost:8080/api/content/${id}/credit?page=${page}`, {withCredentials: true});
export const postContentRating = (id, data) => axios.post(`http://localhost:8080/api/content/${id}/rating`, data, {withCredentials: true});
export const deleteRating = (id) => axios.delete(`http://localhost:8080/api/content/${id}/rating`, {withCredentials: true});