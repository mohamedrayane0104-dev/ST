import axios from 'axios';

const API_URL = 'http://localhost:8080/auth';

export const register = (nom: string, prenom: string, email: string, motDePasse: string) => {
  return axios.post(`${API_URL}/register`, { nom, prenom, email, motDePasse });
};

export const login = (email: string, motDePasse: string) => {
  return axios.post(`${API_URL}/login`, { email, motDePasse });
};

export const verifyEmail = (token: string) => {
  return axios.get(`${API_URL}/verify?token=${token}`);
};
