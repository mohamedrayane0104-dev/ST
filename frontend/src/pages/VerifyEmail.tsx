import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { verifyEmail } from '../api/authApi';

export default function VerifyEmail() {
  const [searchParams] = useSearchParams();
  const [message, setMessage] = useState('');

  useEffect(() => {
    const token = searchParams.get('token');
    if (token) {
      verifyEmail(token)
        .then(() => setMessage('✅ Email vérifié avec succès !'))
        .catch(() => setMessage('❌ Lien invalide ou expiré.'));
    }
  }, [searchParams]);

  return (
    <div className="p-4 text-center">
      <h1 className="text-xl font-bold mb-4">Vérification d’e-mail</h1>
      <p>{message || 'Vérification en cours...'}</p>
    </div>
  );
}
