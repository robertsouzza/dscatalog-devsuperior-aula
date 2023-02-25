import axios, { AxiosRequestConfig } from 'axios';
import qs from 'qs';


type LoginResponse ={
    access_token: string;
    token_type: string;
    expires_in: number;
    scope: string;
    UserId: number;
    UserFirstName: string
}


export const BASE_URL = process.env.REACT_APP_BACKEND_URL ?? 'http://localhost:8080';

const CLIENT_ID = process.env.REACT_APP_CLIENT_ID ?? 'dscatalog';
const CLIENT_SECRET = process.env.REACT_APP_CLIENT_SECRET ?? 'dscatalog123';

const tokenKey = 'authData';

type LoginData ={
    username: string;
    password: string;
}

export const requestBackendLogin = (LoginData : LoginData) => {

    const headers={
        'Content-Type' : 'application/x-www-form-urlencoded',
        Authorization: 'Basic ' + window.btoa(CLIENT_ID + ':' + CLIENT_SECRET)
    };

    const data = qs.stringify({
        ...LoginData,
        grant_type : 'password'
    });

    return axios({method: 'POST', baseURL: BASE_URL, url:'/oauth/token', data, headers});

}


export const requestBackend = (config: AxiosRequestConfig) => {
    const headers = config.withCredentials 
    ? {
        ...config.headers,
       Authorization: 'Bearer ' + getAuthData().access_token,
    } 
    //: config.headers;   
    :{}
     return axios({...config, baseURL: BASE_URL, headers });
};


export const saveAuthData = (obj : LoginResponse) => {
      localStorage.setItem(tokenKey, JSON.stringify(obj));
}

export const getAuthData =() =>{
    const str = localStorage.getItem(tokenKey) ?? "{}";
    const obj = JSON.parse(str);
    return obj as LoginResponse;
}