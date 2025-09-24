import axios from 'axios';

export const getHomeContents = (contentType, page) => axios.get(`${process.env.REACT_APP_API_URL}/api/home?contentType=${contentType}&page=${page}`, { withCredentials: true });
export const getGenresByContentType = (contentType) => axios.get(`${process.env.REACT_APP_API_URL}/api/home/${contentType}/domain`, { withCredentials: true });