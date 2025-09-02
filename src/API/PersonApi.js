import axios from "axios";

export const getPersonInfo = (id) => axios.get(`http://localhost:8080/api/person/${id}`, {withCredentials: true});
export const getPersonWorkDomains = (id) => axios.get(`http://localhost:8080/api/person/${id}/work-domains`, {withCredentials: true});
export const getPersonWorks = (id, type, department, page) => axios.get(`http://localhost:8080/api/person/${id}/work?type=${type}&department=${department}&page=${page}`, {withCredentials: true});
export const likePerson = (id) => axios.post(`http://localhost:8080/api/person/${id}/like`, {}, {withCredentials: true});
export const unlikePerson = (id) => axios.delete(`http://localhost:8080/api/person/${id}/like`, {withCredentials: true});