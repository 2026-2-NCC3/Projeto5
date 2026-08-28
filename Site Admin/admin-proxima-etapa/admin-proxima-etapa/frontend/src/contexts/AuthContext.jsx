import { createContext, useContext, useState, useCallback } from 'react';
import axios from 'axios';

const api = axios.create({ baseURL: 'http://localhost:4000/api' });
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('pe_token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

const AuthContext = createContext(undefined);

function lerUsuarioArmazenado() {
  try {
    const raw = localStorage.getItem('pe_user');
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(lerUsuarioArmazenado);
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState(null);

  const login = useCallback(async (email, senha) => {
    setCarregando(true);
    setErro(null);
    try {
      const { data } = await api.post('/auth/login', { email, senha });
      localStorage.setItem('pe_token', data.dados.token);
      localStorage.setItem('pe_user', JSON.stringify(data.dados.usuario));
      setUsuario(data.dados.usuario);
    } catch (err) {
      setErro(err?.response?.data?.erro || 'Não foi possível entrar. Verifique suas credenciais.');
      throw err;
    } finally {
      setCarregando(false);
    }
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('pe_token');
    localStorage.removeItem('pe_user');
    setUsuario(null);
  }, []);

  return (
    <AuthContext.Provider value={{ usuario, carregando, erro, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth deve ser usado dentro de AuthProvider');
  return ctx;
}