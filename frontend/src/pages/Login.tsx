import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', motDePasse: '' });
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(''); // reset message avant chaque submit

    try {
      await login(form.email, form.motDePasse);
      navigate('/profile');
    } catch (err: any) {
      // err peut être un objet Response ou un objet custom
      if (err?.response?.data?.message) {
        setMessage(err.response.data.message);
      } else if (err?.message) {
        setMessage(err.message);
      } else {
        setMessage("❌ Erreur de connexion");
      }
    }
  };

  return (
    <div className="p-4 max-w-md mx-auto">
      <h1 className="text-xl font-bold mb-4">Connexion</h1>
      <form onSubmit={handleSubmit} className="flex flex-col gap-2">
        <input
          type="email"
          placeholder="Email"
          value={form.email}
          onChange={e => setForm({ ...form, email: e.target.value })}
          className="border p-2 rounded"
          required
        />
        <input
          type="password"
          placeholder="Mot de passe"
          value={form.motDePasse}
          onChange={e => setForm({ ...form, motDePasse: e.target.value })}
          className="border p-2 rounded"
          required
        />
        <button type="submit" className="bg-green-600 text-white py-2 rounded">
          Se connecter
        </button>
      </form>

      {message && (
        <p className="mt-3 text-center text-red-500 font-medium">{message}</p>
      )}
    </div>
  );
}
