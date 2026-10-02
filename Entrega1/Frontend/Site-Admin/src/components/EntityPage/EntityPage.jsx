import { useEffect, useMemo, useState } from 'react';
import Icon from '../Icon';
import Modal from '../Modal';
import './EntityPage.css';

function keyFor(title) {
  return `proxima-etapa:${title.toLowerCase().replaceAll(' ', '-')}`;
}

export default function EntityPage({ config }) {
  const storageKey = keyFor(config.title);

  const [records, setRecords] = useState(() => {
    const storedRecords = localStorage.getItem(storageKey);

    return storedRecords
      ? JSON.parse(storedRecords)
      : config.records;
  });

  const [query, setQuery] = useState('');
  const [status, setStatus] = useState('Todos');
  const [modalOpen, setModalOpen] = useState(false);
  const [notice, setNotice] = useState('');
  const [loading, setLoading] = useState(true);

  const blankForm = Object.fromEntries(
    config.fields.map((field) => [field, ''])
  );

  const [form, setForm] = useState(blankForm);

  useEffect(() => {
    const timer = setTimeout(() => {
      setLoading(false);
    }, 250);

    return () => clearTimeout(timer);
  }, []);

  useEffect(() => {
    localStorage.setItem(storageKey, JSON.stringify(records));
  }, [records, storageKey]);

  const visibleRecords = useMemo(() => {
    const normalizedQuery = query.toLowerCase();

    return records.filter((record) => {
      const matchesStatus =
        status === 'Todos' || record.Status === status;

      const matchesSearch = Object.values(record)
        .join(' ')
        .toLowerCase()
        .includes(normalizedQuery);

      return matchesStatus && matchesSearch;
    });
  }, [records, status, query]);

  function openCreateModal() {
    setNotice('');
    setForm(blankForm);
    setModalOpen(true);
  }

  function closeCreateModal() {
    setModalOpen(false);
    setNotice('');
    setForm(blankForm);
  }

  function handleFormChange(field, value) {
    setForm((currentForm) => ({
      ...currentForm,
      [field]: value,
    }));
  }

  function create(event) {
    event.preventDefault();

    const hasEmptyField = Object.values(form).some(
      (value) => !String(value).trim()
    );

    if (hasEmptyField) {
      setNotice('Preencha todos os campos obrigatórios.');
      return;
    }

    const newRecord = {
      id: Date.now(),
      ...form,
      Status: config.statuses[0],
    };

    setRecords((currentRecords) => [
      ...currentRecords,
      newRecord,
    ]);

    setModalOpen(false);
    setForm(blankForm);

    setNotice(
      `${config.singular} cadastrado(a) com sucesso.`
    );
  }

  function remove(record) {
    const confirmed = window.confirm(
      `Excluir ${config.singular} "${record[config.columns[0]]}"?`
    );

    if (!confirmed) {
      return;
    }

    setRecords((currentRecords) =>
      currentRecords.filter(
        (item) => item.id !== record.id
      )
    );

    setNotice(
      `${config.singular} removido(a) com sucesso.`
    );
  }

  function reset() {
    setRecords(config.records);
    localStorage.removeItem(storageKey);
    setNotice('Dados demonstrativos restaurados.');
  }

  return (
    <section className="entity-page">
      <div className="page-heading">
        <div>
          <p className="eyebrow">Próxima Etapa</p>

          <h1>{config.title}</h1>

          <span>{config.description}</span>
        </div>

        <button
          type="button"
          className="primary-button"
          onClick={openCreateModal}
        >
          <Icon name="userPlus" size={18} />
          Novo {config.singular}
        </button>
      </div>

      <article className="card table-card">
        <div className="table-toolbar">
          <div className="inline-search">
            <Icon name="search" size={18} />

            <input
              type="text"
              value={query}
              onChange={(event) =>
                setQuery(event.target.value)
              }
              placeholder={`Buscar ${config.title.toLowerCase()}`}
              aria-label={`Buscar ${config.title}`}
            />
          </div>

          <select
            className="filter-select"
            value={status}
            onChange={(event) =>
              setStatus(event.target.value)
            }
            aria-label="Filtrar por status"
          >
            <option value="Todos">Todos</option>

            {config.statuses.map((item) => (
              <option key={item} value={item}>
                {item}
              </option>
            ))}
          </select>

          <button
            type="button"
            className="text-button"
            onClick={reset}
          >
            Restaurar dados
          </button>
        </div>

        {notice && (
          <p className="entity-notice" role="status">
            {notice}
          </p>
        )}

        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                {config.columns.map((column) => (
                  <th key={column}>{column}</th>
                ))}

                <th aria-label="Ações"></th>
              </tr>
            </thead>

            <tbody>
              {loading ? (
                <tr>
                  <td colSpan={config.columns.length + 1}>
                    <div className="empty-state">
                      Carregando dados...
                    </div>
                  </td>
                </tr>
              ) : visibleRecords.length > 0 ? (
                visibleRecords.map((record) => (
                  <tr key={record.id}>
                    {config.columns.map((column) => (
                      <td key={column}>
                        {column === 'Status' ? (
                          <span
                            className={`status ${String(
                              record[column]
                            )
                              .toLowerCase()
                              .normalize('NFD')
                              .replace(
                                /[\u0300-\u036f]/g,
                                ''
                              )}`}
                          >
                            {record[column]}
                          </span>
                        ) : (
                          record[column]
                        )}
                      </td>
                    ))}

                    <td>
                      <button
                        type="button"
                        className="danger-link"
                        onClick={() => remove(record)}
                      >
                        Excluir
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={config.columns.length + 1}>
                    <div className="empty-state">
                      Nenhum registro corresponde aos filtros
                      selecionados.
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
          title={`Novo ${config.singular}`}
          onClose={closeCreateModal}
        >
          <form
            className="user-form"
            onSubmit={create}
          >
            {config.fields.map((field) => (
              <label key={field}>
                {field}

                <input
                  type="text"
                  value={form[field]}
                  onChange={(event) =>
                    handleFormChange(
                      field,
                      event.target.value
                    )
                  }
                  placeholder={`Informe ${field.toLowerCase()}`}
                />
              </label>
            ))}

            {notice && (
              <p className="form-error" role="alert">
                {notice}
              </p>
            )}

            <footer>
              <button
                type="button"
                className="secondary-button"
                onClick={closeCreateModal}
              >
                Cancelar
              </button>

              <button
                type="submit"
                className="primary-button"
              >
                Salvar
              </button>
            </footer>
          </form>
        </Modal>
      )}
    </section>
  );
}