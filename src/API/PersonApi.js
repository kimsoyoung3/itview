import axios from "axios";

export const getPersonInfo = (id) => axios.get(`${process.env.REACT_APP_API_URL}/api/person/${id}`, {withCredentials: true});
export const getPersonWorkDomains = (id) => axios.get(`${process.env.REACT_APP_API_URL}/api/person/${id}/work-domains`, {withCredentials: true});
export const getPersonWorks = (id, type, department, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/person/${id}/work?type=${type}&department=${department}&page=${page}`, {withCredentials: true});
export const likePerson = (id) => axios.post(`${process.env.REACT_APP_API_URL}/api/person/${id}/like`, {}, {withCredentials: true});
export const unlikePerson = (id) => axios.delete(`${process.env.REACT_APP_API_URL}/api/person/${id}/like`, {withCredentials: true});