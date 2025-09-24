import axios from 'axios';

export const getHomeContents = (contentType, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/home?contentType=${contentType}&page=${page}`, { withCredentials: true });
export const getGenresByContentType = (contentType) => axios.get(`${process.env.REACT_APP_API_URL}/api/home/${contentType}/genre`, { withCredentials: true });
export const getChannelsByContentType = (contentType) => axios.get(`${process.env.REACT_APP_API_URL}/api/home/${contentType}/channel`, { withCredentials: true });
export const getContentsByGenre = (contentType, genre) => axios.get(`${process.env.REACT_APP_API_URL}/api/home/${contentType}/genre/${genre}`, { withCredentials: true });
export const getContentsByChannel = (contentType, channel) => axios.get(`${process.env.REACT_APP_API_URL}/api/home/${contentType}/channel/${channel}`, { withCredentials: true });