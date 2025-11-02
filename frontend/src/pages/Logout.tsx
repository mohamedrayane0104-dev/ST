import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const Logout = () => {
  const { logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const handleLogout = async () => {
      try {
        await logout(); // Appel à la méthode logout du AuthContext
        navigate("/login"); // Redirection après déconnexion
      } catch (error) {
        console.error("Erreur lors de la déconnexion :", error);
      }
    };

    handleLogout();
  }, [logout, navigate]);

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <p className="text-gray-600 text-lg">Déconnexion en cours...</p>
    </div>
  );
};

export default Logout;
