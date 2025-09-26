import axios from "axios";

export const CollectionCreate = async (data) => axios.post(`${process.env.REACT_APP_API_URL}/api/collection`, data, {withCredentials: true});
export const getCollectionDetail = (id) => axios.get(`${process.env.REACT_APP_API_URL}/api/collection/${id}`, {withCredentials: true});
export const getCollectionItems = (id, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/collection/${id}/items?page=${page}`, {withCredentials: true});
export const getCollectionForm = (id) => axios.get(`${process.env.REACT_APP_API_URL}/api/collection/${id}/edit`, {withCredentials: true});
export const getCollectionToAdd = (contentId) => axios.get(`${process.env.REACT_APP_API_URL}/api/collection/add/${contentId}`, {withCredentials: true});
export const editCollection = (id, data) => axios.put(`${process.env.REACT_APP_API_URL}/api/collection/${id}`, data, {withCredentials: true});
export const deleteCollection = (id) => axios.delete(`${process.env.REACT_APP_API_URL}/api/collection/${id}`, {withCredentials: true});
export const likeCollection = (id) => axios.post(`${process.env.REACT_APP_API_URL}/api/collection/${id}/like`, {}, {withCredentials: true});
export const unlikeCollection = (id) => axios.delete(`${process.env.REACT_APP_API_URL}/api/collection/${id}/like`, {withCredentials: true});
export const getCollectionReplies = (id, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/collection/${id}/reply?page=${page}`, {withCredentials: true});
export const insertReply = (id, data) => axios.post(`${process.env.REACT_APP_API_URL}/api/collection/${id}/reply`, data, {withCredentials: true});