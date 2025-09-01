import axios from "axios";

export const getPersonInfo = (personId) => axios.get(`http://localhost:8080/api/person/${personId}`, {withCredentials: true});