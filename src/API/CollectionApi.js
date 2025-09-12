import axios from "axios";

export const CollectionCreate = async (data) => axios.post(`${process.env.REACT_APP_API_URL}/collection`, data, {withCredentials: true});
export const getCollectionDetail = (id) => axios.get(`${process.env.REACT_APP_API_URL}/collection/${id}`, {withCredentials: true});
export const getCollectionItems = (id, page) => axios.get(`${process.env.REACT_APP_API_URL}/collection/${id}/items?page=${page}`, {withCredentials: true});
export const likeCollection = (id) => axios.post(`${process.env.REACT_APP_API_URL}/collection/${id}/like`, {}, {withCredentials: true});
export const unlikeCollection = (id) => axios.delete(`${process.env.REACT_APP_API_URL}/collection/${id}/like`, {withCredentials: true});