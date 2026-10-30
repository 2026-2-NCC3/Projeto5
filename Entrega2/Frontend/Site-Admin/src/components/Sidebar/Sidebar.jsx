import { NavLink } from 'react-router-dom';

import Icon from '../Icon';
import Avatar from '../Avatar';

import './Sidebar.css';

const navItems = [
  {
    to: '/dashboard',
    label: 'Dashboard',
    icon: 'grid',
  },
  {
    to: '/alunos',
    label: 'Alunos',
    icon: 'users',
  },
  {
    to: '/cursos',
    label: 'Cursos',
    icon: 'grid',
  },
  {
    to: '/atividades',
    label: 'Atividades',
    icon: 'grid',
  },
  {
    to: '/inscricoes',
    label: 'Inscrições',
    icon: 'users',
  },
  {
    to: '/presencas',
    label: 'Presenças',
    icon: 'target',
  },
  {
    to: '/certificados',
    label: 'Certificados',
    icon: 'trend',
  },
  {
    to: '/cards',
    label: 'Cards dos alunos',
    icon: 'grid',
  },
  {
    to: '/testes',
    label: 'Testes de perfil',
    icon: 'target',
  },
  {
    to: '/mensagens',
    label: 'Mensagens',
    icon: 'bell',
  },
  {
    to: '/notificacoes',
    label: 'Notificações',
    icon: 'bell',
  },
  {
    to: '/relatorios',
    label: 'Relatórios',
    icon: 'trend',
  },
  {
    to: '/configuracoes',
    label: 'Configurações',
    icon: 'settings',
  },
];

export default function Sidebar({
  isOpen,
  onClose,
  onLogout,
}) {
  function handleSupport() {
    window.alert(
      'A central de suporte será aberta em breve.'
    );
  }

  return (
    <>
      <aside
        className={`sidebar ${
          isOpen ? 'is-open' : ''
        }`}
      >
        <div className="brand">
          <img
            src="./src/Logo Next Step Solutions.png"
            alt="Next Step Solutions"
            className="brand-logo"
          />
          <button
            type="button"
            className="mobile-close icon-button"
            onClick={onClose}
            aria-label="Fechar menu"
          >
            <Icon name="close" />
          </button>
        </div>

        <nav aria-label="Navegação principal">
          {navItems.map((item) => (
            <NavLink
              to={item.to}
              key={item.to}
              onClick={onClose}
            >
              <Icon name={item.icon} />
              <span>{item.label}</span>
            </NavLink>
          ))}
        </nav>

        <div className="sidebar-footer">
          <div className="help-card">
            <strong>Precisa de ajuda?</strong>

            <span>
              Acesse nossa central de suporte.
            </span>

            <button
              type="button"
              onClick={handleSupport}
            >
              Ver suporte
            </button>
          </div>

          <div className="account">
            <Avatar
              initials="JS"
              color="purple"
              size="small"
            />

            <div>
              <strong>
                Equipe Próxima Etapa
              </strong>

              <button
                type="button"
                className="logout-button"
                onClick={onLogout}
              >
                Sair
              </button>
            </div>
          </div>
        </div>
      </aside>

      {isOpen && (
        <button
          type="button"
          className="sidebar-overlay"
          onClick={onClose}
          aria-label="Fechar menu"
        />
      )}
    </>
  );
}