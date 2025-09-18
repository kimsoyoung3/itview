import axios from "axios";

export const searchContents = async (keyword) => axios.get(`${process.env.REACT_APP_API_URL}/api/search/content?keyword=${keyword}`, {withCredentials: true});
export const searchContentsDetail = async (type, keyword, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/search/content/detail?type=${type}&keyword=${keyword}&page=${page}`, {withCredentials: true});
export const searchPersons = async (keyword, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/search/person?keyword=${keyword}&page=${page}`, {withCredentials: true});
export const searchCollections = async (keyword, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/search/collection?keyword=${keyword}&page=${page}`, {withCredentials: true});
export const searchUsers = async (keyword, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/search/user?keyword=${keyword}&page=${page}`, {withCredentials: true});