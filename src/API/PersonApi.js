import axios from "axios";

export const getPersonInfo = (id) => axios.get(`http://localhost:8080/api/person/${id}`, {withCredentials: true});
export const getPersonWorks = (id, type, department, page) => axios.get(`http://localhost:8080/api/person/${id}/work?type=${type}&department=${department}&page=${page}`, {withCredentials: true});