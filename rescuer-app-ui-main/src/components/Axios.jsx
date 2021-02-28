import axios from 'axios';
import { BroadcastChannel } from 'broadcast-channel';
import AppStorage from '../service/appStorage';

const instance = axios.create({});
const loginEventsChannel = new BroadcastChannel('login-events-channel');

instance.interceptors.request.use(
  (config) => {
    const token = AppStorage.getItem('access_token');
    if (token) {
      config.headers.Authorization = token;
    }
    if(config.url.includes('download')){
      config['responseType']='blob';
    }
    config.headers['Content-Type'] = 'application/json';
    return config;
  },
  (error) => {
    Promise.reject(error);
  },
);

instance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response.status === 401 || error.response.status === 403) {
      loginEventsChannel.postMessage('login');
    } else {
      return Promise.reject(error);
    }
  },
);

export default instance;