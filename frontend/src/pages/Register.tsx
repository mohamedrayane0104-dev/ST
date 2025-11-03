import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import axios from 'axios';
import './Register.css';

export default function Register() {
  const { register } = useAuth();
  const [form, setForm] = useState({
    nom: '',
    prenom: '',
    email: '',
    motDePasse: '',
  });
  const [message, setMessage] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(form.email)) {
      setMessage("❌ L'adresse e-mail n'est pas valide.");
      return;
    }

    if (form.motDePasse.length < 6) {
      setMessage('❌ Le mot de passe doit contenir au moins 6 caractères.');
      return;
    }

    try {
      await register(form.nom, form.prenom, form.email, form.motDePasse);
      setMessage('✅ Inscription réussie. Vérifiez votre e-mail !');
      setForm({ nom: '', prenom: '', email: '', motDePasse: '' });
    } catch (err: any) {
      if (axios.isAxiosError(err)) {
        const data = err.response?.data;
        const errorMsg =
          typeof data === 'string'
            ? data
            : data?.message || "❌ Erreur lors de l'inscription.";
        setMessage(errorMsg);
      } else {
        setMessage('❌ Erreur inconnue.');
      }
    }
  };

  return (
    <div className="register-container">
      <div className="register-header">
        <h2>Créer votre compte</h2>
        <p>Rejoindre SkillTrack et commencez votre parcours d'apprentissage !</p>
      </div>

      <form className="register-form" onSubmit={handleSubmit}>
        <label htmlFor="nom">Nom</label>
        <input
          type="text"
          id="nom"
          placeholder="Doe"
          value={form.nom}
          onChange={(e) => setForm({ ...form, nom: e.target.value })}
          required
        />

        <label htmlFor="prenom">Prénom</label>
        <input
          type="text"
          id="prenom"
          placeholder="John"
          value={form.prenom}
          onChange={(e) => setForm({ ...form, prenom: e.target.value })}
          required
        />

        <label htmlFor="email">Email</label>
        <input
          type="email"
          id="email"
          placeholder="john@example.com"
          value={form.email}
          onChange={(e) => setForm({ ...form, email: e.target.value })}
          required
        />

        <label htmlFor="password">Mot de passe</label>
        <input
          type="password"
          id="password"
          placeholder="6 caractères minimum"
          value={form.motDePasse}
          onChange={(e) => setForm({ ...form, motDePasse: e.target.value })}
          minLength={6}
          required
        />

        <button type="submit" className="register-btn">
          S'inscrire
        </button>
      </form>

      {message && (
        <p
          className={`mt-3 text-center font-semibold ${
            message.startsWith('✅') ? 'text-green-600' : 'text-red-600'
          }`}
        >
          {message}
        </p>
      )}

      <div className="login-link">
        Vous avez déjà un compte? <a href="/login">Se connecter</a>
      </div>
    </div>
  );
}
