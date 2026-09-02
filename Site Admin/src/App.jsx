import { useState } from 'react';
import {
  Navigate,
  Route,
  Routes,
} from 'react-router-dom';

import AdminLayout from './layouts/AdminLayout';

import DashboardPage from './pages/DashboardPage/DashboardPage';
import UsersPage from './pages/UsersPage/UsersPage';
import SettingsPage from './pages/SettingsPage/SettingsPage';
import ModulesPage from './pages/ModulesPage/ModulesPage';
import Login from './pages/Login/Login';
import NotFound from './pages/NotFound/NotFound';

export default function App() {
  const [authenticated, setAuthenticated] = useState(() => {
    return (
      localStorage.getItem(
        'proxima-etapa:session'
      ) === 'active'
    );
  });

  function login() {
    localStorage.setItem(
      'proxima-etapa:session',
      'active'
    );

    setAuthenticated(true);
  }

  function logout() {
    localStorage.removeItem(
      'proxima-etapa:session'
    );

    setAuthenticated(false);
  }

  if (!authenticated) {
    return (
      <Routes>
        <Route
          path="/login"
          element={<Login onLogin={login} />}
        />

        <Route
          path="*"
          element={
            <Navigate
              to="/login"
              replace
            />
          }
        />
      </Routes>
    );
  }

  return (
    <Routes>
      <Route
        element={
          <AdminLayout onLogout={logout} />
        }
      >
        <Route
          path="/"
          element={
            <Navigate
              to="/dashboard"
              replace
            />
          }
        />

        <Route
          path="/dashboard"
          element={<DashboardPage />}
        />

        <Route
          path="/alunos"
          element={
            <ModulesPage module="alunos" />
          }
        />

        <Route
          path="/cursos"
          element={
            <ModulesPage module="cursos" />
          }
        />

        <Route
          path="/atividades"
          element={
            <ModulesPage module="atividades" />
          }
        />

        <Route
          path="/inscricoes"
          element={
            <ModulesPage module="inscricoes" />
          }
        />

        <Route
          path="/presencas"
          element={
            <ModulesPage module="presencas" />
          }
        />

        <Route
          path="/certificados"
          element={
            <ModulesPage module="certificados" />
          }
        />

        <Route
          path="/cards"
          element={
            <ModulesPage module="cards" />
          }
        />

        <Route
          path="/testes"
          element={
            <ModulesPage module="testes" />
          }
        />

        <Route
          path="/mensagens"
          element={
            <ModulesPage module="mensagens" />
          }
        />

        <Route
          path="/notificacoes"
          element={
            <ModulesPage module="notificacoes" />
          }
        />

        <Route
          path="/relatorios"
          element={
            <ModulesPage module="relatorios" />
          }
        />

        <Route
          path="/usuarios"
          element={<UsersPage />}
        />

        <Route
          path="/configuracoes"
          element={<SettingsPage />}
        />
      </Route>

      <Route
        path="*"
        element={<NotFound />}
      />
    </Routes>
  );
}