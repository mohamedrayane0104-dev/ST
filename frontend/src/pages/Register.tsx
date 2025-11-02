import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import axios from 'axios';

export default function Register() {
  const { register } = useAuth();
  const [form, setForm] = useState({ nom: '', prenom: '', email: '', motDePasse: '' });
  const [message, setMessage] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null); // réinitialiser le message à chaque soumission

    // ✅ Validation côté frontend avant envoi
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(form.email)) {
      setMessage("❌ L'adresse e-mail n'est pas valide.");
      return;
    }

    if (form.motDePasse.length < 6) {
      setMessage("❌ Le mot de passe doit contenir au moins 6 caractères.");
      return;
    }

    try {
      await register(form.nom, form.prenom, form.email, form.motDePasse);
      setMessage("✅ Inscription réussie. Vérifiez votre e-mail !");
      setForm({ nom: '', prenom: '', email: '', motDePasse: '' }); // réinitialiser le formulaire
    } catch (err: any) {
      if (axios.isAxiosError(err)) {
        const data = err.response?.data;
        const errorMsg =
          typeof data === "string"
            ? data
            : data?.message || "❌ Erreur lors de l'inscription.";
        setMessage(errorMsg);
      } else {
        setMessage("❌ Erreur inconnue.");
      }
    }
  };

  return (
    <div className="p-4 max-w-md mx-auto">
      <h1 className="text-xl font-bold mb-4 text-center">Inscription</h1>
      <form onSubmit={handleSubmit} className="flex flex-col gap-2">
        <input
          type="text"
          placeholder="Nom"
          value={form.nom}
          onChange={e => setForm({ ...form, nom: e.target.value })}
          className="border p-2 rounded"
          required
        />
        <input
          type="text"
          placeholder="Prénom"
          value={form.prenom}
          onChange={e => setForm({ ...form, prenom: e.target.value })}
          className="border p-2 rounded"
          required
        />
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
          placeholder="Mot de passe (min. 6 caractères)"
          value={form.motDePasse}
          onChange={e => setForm({ ...form, motDePasse: e.target.value })}
          className="border p-2 rounded"
          minLength={6} // 🔹 validation HTML native
          required
        />
        <button
          type="submit"
          className="bg-blue-500 text-white py-2 rounded hover:bg-blue-600 transition"
        >
          S'inscrire
        </button>
      </form>

      {message && (
        <p
          className={`mt-3 text-center font-semibold ${
            message.startsWith("✅") ? "text-green-600" : "text-red-600"
          }`}
        >
          {message}
        </p>
      )}
    </div>
  );
}
