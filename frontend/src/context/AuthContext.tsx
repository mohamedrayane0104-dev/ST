import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';
import Loader from '../components/Loader';

interface User {
  nom: string;
  prenom: string;
  niveau: string;
  email: string;
  total_points: number;
}

interface AuthContextType {
  user: User | null;
  token: string | null;
  loading: boolean;
  login: (email: string, password: string) => Promise<void>;
  register: (nom: string, prenom: string, email: string, motDePasse: string) => Promise<void>;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(localStorage.getItem('token'));
  const [loading, setLoading] = useState<boolean>(true);

  // ✅ Vérifie le token et récupère le profil
  useEffect(() => {
    const verifyToken = async () => {
      if (token) {
        try {
          const res = await axios.get('http://localhost:8080/auth/profile', {
            headers: { Authorization: `Bearer ${token}` },
          });
          setUser(res.data);
        } catch (err) {
          console.warn("Token invalide, suppression du token...");
          await logout();
        }
      }
      setLoading(false);
    };
    verifyToken();
  }, [token]);

  // ✅ LOGIN : récupère le token depuis res.data.token
  const login = async (email: string, password: string) => {
    const res = await axios.post('http://localhost:8080/auth/login', { email, motDePasse: password });
    const token = res.data?.token;
    if (!token) throw new Error("Token JWT manquant dans la réponse du serveur");

    localStorage.setItem('token', token);
    setToken(token);

    // Charger le profil immédiatement après login
    const profileRes = await axios.get('http://localhost:8080/auth/profile', {
      headers: { Authorization: `Bearer ${token}` },
    });
    setUser(profileRes.data);
  };

  // ✅ REGISTER
  const register = async (nom: string, prenom: string, email: string, motDePasse: string) => {
    await axios.post('http://localhost:8080/auth/register', { nom, prenom, email, motDePasse });
  };

  // ✅ LOGOUT
  const logout = async () => {
    if (token) {
      try {
        await axios.post(
          'http://localhost:8080/auth/logout',
          {},
          { headers: { Authorization: `Bearer ${token}` } }
        );
      } catch {
        console.warn('Erreur lors du logout');
      }
    }
    localStorage.removeItem('token');
    setUser(null);
    setToken(null);
  };

  if (loading) return <Loader />;

  return (
    <AuthContext.Provider value={{ user, token, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within an AuthProvider');
  return context;
};
