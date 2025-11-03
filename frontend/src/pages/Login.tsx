import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Login.css';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', motDePasse: '' });
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage('');

    try {
      await login(form.email, form.motDePasse);
      navigate('/profile');
    } catch (err: any) {
      if (err?.response?.data?.message) {
        setMessage(err.response.data.message);
      } else if (err?.message) {
        setMessage(err.message);
      } else {
        setMessage('❌ Erreur de connexion');
      }
    }
  };

  return (
    <div className="login-container">
      <div className="login-header">
        <h2>Bienvenue</h2>
        <p>Saisir les identifiants de votre compte</p>
      </div>

      <form className="login-form" onSubmit={handleSubmit}>
        <label htmlFor="email">Email</label>
        <input
          type="email"
          id="email"
          placeholder="test@example.com"
          value={form.email}
          onChange={(e) => setForm({ ...form, email: e.target.value })}
          required
        />

        <label htmlFor="password">Mot de passe</label>
        <input
          type="password"
          id="password"
          placeholder="••••••••"
          value={form.motDePasse}
          onChange={(e) => setForm({ ...form, motDePasse: e.target.value })}
          required
        />
 
        {/*<div className="forgot-password">
          <a href="#">Forgot password?</a>
        </div>*/}

        <button type="submit" className="login-btn">
          Se connecter
        </button>
      </form>

      {message && (
        <p className="mt-3 text-center text-red-500 font-medium">{message}</p>
      )}

      <div className="register-link">
        Vous n'avez pas de compte? <a href="/register">S'inscrire</a>
      </div>
    </div>
  );
}
