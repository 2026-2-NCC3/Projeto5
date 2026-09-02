import { useMemo, useState } from 'react';

import { initialUsers } from '../../services/mockData';
import Avatar from '../../components/Avatar';
import Icon from '../../components/Icon';
import Modal from '../../components/Modal';

import './UsersPage.css';

const roles = [
  'Administradora',
  'Editor',
  'Analista',
];

const emptyForm = {
  name: '',
  email: '',
  role: 'Analista',
};

export default function UsersPage() {
  const [users, setUsers] = useState(initialUsers);
  const [query, setQuery] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [form, setForm] = useState(emptyForm);
  const [error, setError] = useState('');

  const filteredUsers = useMemo(() => {
    const normalizedQuery = query.toLowerCase();

    return users.filter((user) =>
      `${user.name} ${user.email}`
        .toLowerCase()
        .includes(normalizedQuery)
    );
  }, [users, query]);

  function openModal() {
    setError('');
    setForm(emptyForm);
    setModalOpen(true);
  }

  function closeModal() {
    setModalOpen(false);
    setError('');
    setForm(emptyForm);
  }

  function handleFormChange(field, value) {
    setForm((currentForm) => ({
      ...currentForm,
      [field]: value,
    }));
  }

  function addUser(event) {
    event.preventDefault();

    const isNameValid = form.name.trim().length > 0;
    const isEmailValid = /^\S+@\S+\.\S+$/.test(
      form.email
    );

    if (!isNameValid || !isEmailValid) {
      setError(
        'Informe um nome e um e-mail válido.'
      );
      return;
    }

    const initials = form.name
      .trim()
      .split(/\s+/)
      .map((part) => part[0])
      .slice(0, 2)
      .join('')
      .toUpperCase();

    const newUser = {
      id: Date.now(),
      ...form,
      status: 'Pendente',
      initials,
      color: 'blue',
    };

    setUsers((currentUsers) => [
      ...currentUsers,
      newUser,
    ]);

    closeModal();
  }

  return (
    <>
      <div className="page-heading">
        <div>
          <p className="eyebrow">
            Gerenciamento
          </p>

          <h1>Usuários</h1>

          <span>
            Gerencie o acesso e as permissões da equipe.
          </span>
        </div>

        <button
          type="button"
          className="primary-button"
          onClick={openModal}
        >
          <Icon
            name="userPlus"
            size={18}
          />

          Adicionar usuário
        </button>
      </div>

      <article className="card table-card">
        <div className="table-toolbar">
          <div className="inline-search">
            <Icon
              name="search"
              size={18}
            />

            <input
              type="text"
              value={query}
              onChange={(event) =>
                setQuery(event.target.value)
              }
              placeholder="Buscar usuário"
              aria-label="Buscar usuário"
            />
          </div>

          <span>
            {filteredUsers.length}{' '}
            {filteredUsers.length === 1
              ? 'usuário'
              : 'usuários'}
          </span>
        </div>

        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Usuário</th>
                <th>Função</th>
                <th>Status</th>
                <th aria-label="Ações"></th>
              </tr>
            </thead>

            <tbody>
              {filteredUsers.length > 0 ? (
                filteredUsers.map((user) => (
                  <tr key={user.id}>
                    <td>
                      <div className="user-cell">
                        <Avatar
                          initials={user.initials}
                          color={user.color}
                        />

                        <div>
                          <strong>
                            {user.name}
                          </strong>

                          <span>
                            {user.email}
                          </span>
                        </div>
                      </div>
                    </td>

                    <td>
                      {user.role}
                    </td>

                    <td>
                      <span
                        className={`status ${user.status
                          .toLowerCase()
                          .normalize('NFD')
                          .replace(
                            /[\u0300-\u036f]/g,
                            ''
                          )}`}
                      >
                        {user.status}
                      </span>
                    </td>

                    <td>
                      <button
                        type="button"
                        className="icon-button"
                        aria-label={`Opções de ${user.name}`}
                        onClick={() =>
                          window.alert(
                            `Opções do usuário ${user.name}`
                          )
                        }
                      >
                        <Icon name="more" />
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={4}>
                    <div className="empty-state">
                      Nenhum usuário encontrado
                      para esta busca.
                    </div>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </article>

      {modalOpen && (
        <Modal
          title="Adicionar usuário"
          onClose={closeModal}
        >
          <form
            className="user-form"
            onSubmit={addUser}
          >
            <label>
              Nome completo

              <input
                type="text"
                autoFocus
                value={form.name}
                onChange={(event) =>
                  handleFormChange(
                    'name',
                    event.target.value
                  )
                }
                placeholder="Ex.: Ana Souza"
              />
            </label>

            <label>
              E-mail

              <input
                type="email"
                value={form.email}
                onChange={(event) =>
                  handleFormChange(
                    'email',
                    event.target.value
                  )
                }
                placeholder="ana@empresa.com"
              />
            </label>

            <label>
              Função

              <select
                value={form.role}
                onChange={(event) =>
                  handleFormChange(
                    'role',
                    event.target.value
                  )
                }
              >
                {roles.map((role) => (
                  <option
                    key={role}
                    value={role}
                  >
                    {role}
                  </option>
                ))}
              </select>
            </label>

            {error && (
              <p
                className="form-error"
                role="alert"
              >
                {error}
              </p>
            )}

            <footer>
              <button
                type="button"
                className="secondary-button"
                onClick={closeModal}
              >
                Cancelar
              </button>

              <button
                type="submit"
                className="primary-button"
              >
                Adicionar
              </button>
            </footer>
          </form>
        </Modal>
      )}
    </>
  );
}