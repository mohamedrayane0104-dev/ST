import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

export default function Profile() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <div className="p-4 text-center">
      <h1 className="text-2xl font-bold mb-4">Profil</h1>
      {user ? (
        <>
          <p>Email : {user.email}</p>
          <button onClick={handleLogout} className="bg-red-500 text-white px-4 py-2 mt-3 rounded">
            Se déconnecter
          </button>
        </>
      ) : (
        <p>Chargement...</p>
      )}
    </div>
  );
}
