import axios from "axios";

export const getContentDetail = (id) => axios.get(`http://localhost:8080/api/content/${id}`, {withCredentials: true});
export const getContentCredit = (page) => axios.get(`http://localhost:8080/api/content/credit?page=${page}`, {withCredentials: true});