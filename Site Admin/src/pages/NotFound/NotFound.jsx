import { Link } from 'react-router-dom';
import './NotFound.css';

export default function NotFound() {
  return (
    <main className="not-found">
      <p>Erro 404</p>

      <h1>Página não encontrada</h1>

      <Link
        className="primary-button"
        to="/dashboard"
      >
        Voltar ao painel
      </Link>
    </main>
  );
}