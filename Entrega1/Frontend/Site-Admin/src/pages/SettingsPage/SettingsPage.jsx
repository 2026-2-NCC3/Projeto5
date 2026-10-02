import { useState } from 'react';
import './SettingsPage.css';

const initialSettings = {
  weekly: true,
  updates: false,
  publicProfile: true,
};

const notificationOptions = [
  {
    key: 'weekly',
    title: 'Resumo semanal',
    text: 'Receba um resumo com as métricas da semana.',
  },
  {
    key: 'updates',
    title: 'Atualizações do produto',
    text: 'Saiba primeiro sobre novos recursos.',
  },
  {
    key: 'publicProfile',
    title: 'Perfil visível à equipe',
    text: 'Permita que colegas vejam seu perfil.',
  },
];

export default function SettingsPage() {
  const [settings, setSettings] = useState(initialSettings);
  const [saved, setSaved] = useState(false);

  function toggle(name) {
    setSettings((currentSettings) => ({
      ...currentSettings,
      [name]: !currentSettings[name],
    }));
  }

  function save(event) {
    event.preventDefault();

    setSaved(true);

    setTimeout(() => {
      setSaved(false);
    }, 2500);
  }

  return (
    <>
      <div className="page-heading">
        <div>
          <p className="eyebrow">Preferências</p>

          <h1>Configurações</h1>

          <span>
            Personalize a experiência do seu ambiente.
          </span>
        </div>
      </div>

      <form
        className="settings-layout"
        onSubmit={save}
      >
        <article className="card settings-card">
          <header>
            <h2>Informações da organização</h2>

            <p>
              Dados exibidos para sua equipe.
            </p>
          </header>

          <div className="form-grid">
            <label>
              Nome da organização

              <input
                type="text"
                defaultValue="Próxima Etapa"
              />
            </label>

            <label>
              E-mail de contato

              <input
                type="email"
                defaultValue="contato@proximaetapa.com.br"
              />
            </label>

            <label className="full">
              Fuso horário

              <select defaultValue="America/Sao_Paulo">
                <option value="America/Sao_Paulo">
                  Brasília (GMT-3)
                </option>

                <option value="Europe/Lisbon">
                  Lisboa (GMT+1)
                </option>
              </select>
            </label>
          </div>
        </article>

        <article className="card settings-card">
          <header>
            <h2>Notificações e privacidade</h2>

            <p>
              Defina como deseja receber novidades.
            </p>
          </header>

          <div className="switch-list">
            {notificationOptions.map((item) => (
              <div
                className="switch-row"
                key={item.key}
              >
                <div>
                  <strong>{item.title}</strong>

                  <p>{item.text}</p>
                </div>

                <button
                  type="button"
                  className={`switch ${
                    settings[item.key] ? 'checked' : ''
                  }`}
                  role="switch"
                  aria-checked={settings[item.key]}
                  onClick={() => toggle(item.key)}
                >
                  <span />
                </button>
              </div>
            ))}
          </div>
        </article>

        <div className="save-row">
          <span
            className="save-message"
            aria-live="polite"
          >
            {saved && 'Alterações salvas com sucesso.'}
          </span>

          <button
            type="submit"
            className="primary-button"
          >
            Salvar alterações
          </button>
        </div>
      </form>
    </>
  );
}