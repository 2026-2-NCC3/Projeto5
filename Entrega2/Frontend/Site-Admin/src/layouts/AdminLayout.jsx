import { Outlet } from 'react-router-dom';
import { useState } from 'react';

import Sidebar from '../components/Sidebar/Sidebar';
import Header from '../components/Header/Header';

export default function AdminLayout({ onLogout }) {
  const [menuOpen, setMenuOpen] = useState(false);

  function openMenu() {
    setMenuOpen(true);
  }

  function closeMenu() {
    setMenuOpen(false);
  }

  return (
    <div className="app-shell">
      <Sidebar
        isOpen={menuOpen}
        onClose={closeMenu}
        onLogout={onLogout}
      />

      <main className="main-content">
        <Header onOpenMenu={openMenu} />

        <div className="page-container">
          <Outlet />
        </div>
      </main>
    </div>
  );
}