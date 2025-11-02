import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

interface PublicRouteProps {
  children: React.ReactNode;
}

const PublicRoute = ({ children }: PublicRouteProps) => {
  const { user, loading } = useAuth();

  if (loading) return <div>Chargement...</div>;

  if (user) return <Navigate to="/profile" replace />; // redirection si connecté

  return children;
};

export default PublicRoute;
