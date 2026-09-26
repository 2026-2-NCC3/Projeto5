import axios from "axios";

const api = axios.create({
  baseURL: 'https://projeto5-7yqt.onrender.com/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para injetar o token de autenticação antes de cada requisição
api.interceptors.request.use(
  (config) => {
    // Busca o token de onde você o salvou após o login (exemplo: localStorage)
    const token = localStorage.getItem('token');
    
    // Se o token existir, adiciona no cabeçalho de Autorização
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default api;