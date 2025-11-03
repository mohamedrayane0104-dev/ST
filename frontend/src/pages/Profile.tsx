import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import './Profile.css';

export default function Profile() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <div className="profile-container">
      {user ? (
        <div className="profile-card">
          <h1 className="profile-title">Mon Profil</h1>
          <div className="profile-info">
            <div className="profile-field">
              <span className="label">Nom :</span>
              <span className="value">{user.nom}</span>
            </div>
            <div className="profile-field">
              <span className="label">Prénom :</span>
              <span className="value">{user.prenom}</span>
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
          <button onClick={handleLogout} className="logout-btn">
            Se déconnecter
          </button>
        </div>
      ) : (
        <p className="loading-text">Chargement du profil...</p>
      )}
    </div>
  );
}
