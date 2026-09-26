import Icon from '../Icon'
import Avatar from '../Avatar'
import './Header.css'

export default function Header({ onOpenMenu }) {
  return <header className="topbar">
    <button className="mobile-menu icon-button" onClick={onOpenMenu} 
    aria-label="Abrir menu"><Icon name="menu" />
    </button>

    <div className="search">
      <Icon name="search" size={18} />
    <input aria-label="Buscar" 
    placeholder="Buscar..." />
    </div>
    <div className="topbar-actions">
      <button className="icon-button notification" 
      aria-label="Notificações"><Icon name="bell" />
      <i />
      </button>
      <Avatar initials="JS" 
      color="purple" 
      size="small" />
      </div>
      
  </header>
}
