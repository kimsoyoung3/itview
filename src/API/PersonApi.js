import axios from "axios";

export const getPersonInfo = (id) => axios.get(`http://localhost:8080/api/person/${id}`, {withCredentials: true});