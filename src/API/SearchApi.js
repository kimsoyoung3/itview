import axios from "axios";

export const searchContents = async (keyword) => axios.get(`${process.env.REACT_APP_API_URL}/api/search/content?keyword=${keyword}`, {withCredentials: true});
export const searchPersons = async (keyword) => axios.get(`${process.env.REACT_APP_API_URL}/api/search/person?keyword=${keyword}`, {withCredentials: true});
export const searchCollections = async (keyword) => axios.get(`${process.env.REACT_APP_API_URL}/api/search/collection?keyword=${keyword}`, {withCredentials: true});
export const searchUsers = async (keyword) => axios.get(`${process.env.REACT_APP_API_URL}/api/search/user?keyword=${keyword}`, {withCredentials: true});