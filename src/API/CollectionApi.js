import axios from "axios";

export const CollectionCreate = async (data) => axios.post(`${process.env.REACT_APP_API_URL}/collection`, data, {withCredentials: true});