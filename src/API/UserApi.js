import axios from "axios";

export const registerUser = (data) => axios.post('http://localhost:8080/api/user', data, {withCredentials: true});