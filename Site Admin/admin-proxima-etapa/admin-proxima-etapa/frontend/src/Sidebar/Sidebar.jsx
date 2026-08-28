import { useState } from 'react';
import { NavLink } from 'react-router-dom';
import { GraduationCap, ChevronLeft, ChevronRight, ChevronDown, LogOut } from 'lucide-react';
import { navSections, settingsItem } from '../routes/navConfig';
import { useAuth } from '../contexts/AuthContext';
import './Sidebar.css';

function iniciais(nome) {
  const partes = nome.trim().split(/\s+/);
  const primeiras = partes.slice(0, 2).map((p) => p[0]?.toUpperCase() ?? '');
  return primeiras.join('') || '??';
}

const papelLabel = {
  administrador: 'Administrador',
  gestor: 'Gestor',
  atendente: 'Atendente',
};

export default function Sidebar() {
  const [colapsada, setColapsada] = useState(false);
  const { usuario, logout } = useAuth();

  return (
    <aside className={`sidebar ${colapsada ? 'sidebar--collapsed' : ''}`}>
      {/* Logo */}
      <div className="sidebar__logo">
        <div className="sidebar__logo-icon">
          <GraduationCap size={19} strokeWidth={2.25} />
        </div>
        {!colapsada && (
          <div className="sidebar__logo-text">
            <p className="sidebar__brand">PRÓXIMA ETAPA</p>
            <p className="sidebar__subtitle">Painel Administrativo</p>
          </div>
        )}
      </div>

      <div className="sidebar__divider" />

      {/* Navegação */}
      <nav className="sidebar__nav">
        {navSections.map((section) => (
          <div key={section.title} className="sidebar__section">
            {!colapsada && <p className="sidebar__section-title">{section.title}</p>}
            <ul className="sidebar__list">
              {section.items.map((item) => (
                <li key={item.path}>
                  <NavLink
                    to={item.path}
                    end={item.path === '/'}
                    title={colapsada ? item.label : undefined}
                    className={({ isActive }) =>
                      `sidebar__link ${isActive ? 'sidebar__link--active' : ''} ${
                        colapsada ? 'sidebar__link--collapsed' : ''
                      }`
                    }
                  >
                    <item.icon size={17} strokeWidth={2} className="sidebar__icon" />
                    {!colapsada && <span className="sidebar__label">{item.label}</span>}
                    {item.badge ? (
                      <span className={`sidebar__badge ${colapsada ? 'sidebar__badge--floating' : ''}`}>
                        {item.badge}
                      </span>
                    ) : null}
                  </NavLink>
                </li>
              ))}
            </ul>
          </div>
        ))}
      </nav>

      {/* Botão de colapsar */}
      <button
        onClick={() => setColapsada((v) => !v)}
        className="sidebar__collapse-btn"
        aria-label={colapsada ? 'Expandir menu' : 'Recolher menu'}
      >
        {colapsada ? <ChevronRight size={16} /> : <ChevronLeft size={16} />}
      </button>

      <div className="sidebar__divider" />

      {/* Rodapé */}
      <div className="sidebar__footer">
        <NavLink
          to={settingsItem.path}
          title={colapsada ? settingsItem.label : undefined}
          className={({ isActive }) =>
            `sidebar__link ${isActive ? 'sidebar__link--active' : ''} ${
              colapsada ? 'sidebar__link--collapsed' : ''
            }`
          }
        >
          <settingsItem.icon size={17} strokeWidth={2} />
          {!colapsada && <span>{settingsItem.label}</span>}
        </NavLink>

        {usuario && (
          <button className={`sidebar__user ${colapsada ? 'sidebar__user--collapsed' : ''}`} title="Ver perfil">
            <div className="sidebar__avatar">{iniciais(usuario.nome)}</div>
            {!colapsada && (
              <div className="sidebar__user-info">
                <p className="sidebar__user-name">{usuario.nome}</p>
                <p className="sidebar__user-role">{papelLabel[usuario.papel] ?? usuario.papel}</p>
              </div>
            )}
            {!colapsada && <ChevronDown size={14} className="sidebar__chevron" />}
          </button>
        )}

        <button
          onClick={logout}
          title="Sair"
          className={`sidebar__logout ${colapsada ? 'sidebar__link--collapsed' : ''}`}
        >
          <LogOut size={16} />
          {!colapsada && <span>Sair</span>}
        </button>
      </div>
    </aside>
  );
}