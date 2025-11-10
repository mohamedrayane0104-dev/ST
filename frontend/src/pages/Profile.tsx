import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
import './Profile.css';

export default function Profile() {
  const { user, token, logout, setUser } = useAuth();
  const navigate = useNavigate();

  const [editMode, setEditMode] = useState(false);
  const [formData, setFormData] = useState({
    nom: '',
    prenom: ''
  });
  const [loading, setLoading] = useState(false);

  // Initialiser formData avec les données utilisateur
  useEffect(() => {
    if (user) {
      setFormData({ nom: user.nom, prenom: user.prenom });
    }
  }, [user]);

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSave = async () => {
    if (!token) return;
    setLoading(true);
    try {
      await axios.put(
        'http://localhost:8080/user/profile',
        formData,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      alert('Profil mis à jour avec succès !');
      setEditMode(false);
      setUser({ ...user!, ...formData });
    } catch (err) {
      console.error(err);
      alert('Erreur lors de la mise à jour du profil');
    } finally {
      setLoading(false);
    }
  };

  // Nouvelle fonction pour annuler la modification
  const handleCancel = () => {
    if (user) {
      setFormData({ nom: user.nom, prenom: user.prenom });
    }
    setEditMode(false);
  };

  const handleEdit = () => setEditMode(true);

  const handleDeleteRequest = async () => {
    if (!token) return;
    if (!window.confirm("Voulez-vous vraiment supprimer votre compte ?")) return;

    try {
      const res = await axios.post(
        'http://localhost:8080/user/delete-request',
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );
      alert(res.data.message || "Un email de confirmation a été envoyé !");
    } catch (err) {
      console.error(err);
      alert("Erreur lors de la demande de suppression du compte");
    }
  };

  if (!user) return <p className="loading-text">Chargement du profil...</p>;

  return (
    <div className="profile-container">
      <div className="profile-card">
        <h1 className="profile-title">Mon Profil</h1>
        <div className="profile-info">
          <div className={`profile-field ${editMode ? 'edit-mode' : ''}`}>
            <span className="label">Nom :</span>
            {editMode ? (
              <input name="nom" value={formData.nom} onChange={handleChange} />
            ) : (
              <span className="value">{user.nom}</span>
            )}
          </div>

          <div className={`profile-field ${editMode ? 'edit-mode' : ''}`}>
            <span className="label">Prénom :</span>
            {editMode ? (
              <input name="prenom" value={formData.prenom} onChange={handleChange} />
            ) : (
              <span className="value">{user.prenom}</span>
            )}
          </div>

          <div className="profile-field">
            <span className="label">Niveau :</span>
            <span className="value">{user.niveau}</span>
          </div>

          <div className="profile-field">
            <span className="label">Email :</span>
            <span className="value">{user.email}</span>
          </div>

          <div className="profile-field">
            <span className="label">Total Points :</span>
            <span className="value">{user.total_points}</span>
          </div>
        </div>

        {editMode ? (
          <>
            <button onClick={handleSave} disabled={loading}>
              {loading ? 'Enregistrement...' : 'Sauvegarder'}
            </button>
            <button onClick={handleCancel} style={{ marginTop: '10px' }}>
              Annuler
            </button>
          </>
        ) : (
          <button onClick={handleEdit}>Modifier</button>
        )}

        <button onClick={handleDeleteRequest} className="delete-btn">
          Supprimer mon compte
        </button>

        <button onClick={handleLogout} className="logout-btn">
          Se déconnecter
        </button>
      </div>
    </div>
  );
}
