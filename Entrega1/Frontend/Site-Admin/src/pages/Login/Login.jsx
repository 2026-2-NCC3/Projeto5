import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import logo from '../../Logo Next Step Solutions.png';

import './Login.css';

function Icon({ name, size = 22 }) {
  const paths = {
    shield: (
      <>
        <path d="M12 3 4.5 6v5.4c0 4.8 3.1 8.6 7.5 10.1 4.4-1.5 7.5-5.3 7.5-10.1V6L12 3Z" />
        <rect
          x="9"
          y="11"
          width="6"
          height="5"
          rx="1"
        />
        <path d="M10.5 11V9.5a1.5 1.5 0 0 1 3 0V11" />
      </>
    ),

    user: (
      <>
        <circle
          cx="12"
          cy="8"
          r="3.25"
        />
        <path d="M5 20c.7-3.15 3.1-5 7-5s6.3 1.85 7 5" />
      </>
    ),

    lock: (
      <>
        <rect
          x="5.5"
          y="10.5"
          width="13"
          height="10"
          rx="2"
        />
        <path d="M8.5 10.5V8a3.5 3.5 0 0 1 7 0v2.5M12 14.5v2" />
      </>
    ),

    eye: (
      <>
        <path d="M2.5 12s3.4-5.5 9.5-5.5 9.5 5.5 9.5 5.5-3.4 5.5-9.5 5.5S2.5 12 2.5 12Z" />
        <circle
          cx="12"
          cy="12"
          r="2.2"
        />
      </>
    ),

    eyeOff: (
      <>
        <path d="m3 3 18 18" />
        <path d="M10.6 6.7A10.2 10.2 0 0 1 12 6.5c6.1 0 9.5 5.5 9.5 5.5a16.5 16.5 0 0 1-3 3.5M6.3 6.3A16.2 16.2 0 0 0 2.5 12S5.9 17.5 12 17.5c1.2 0 2.3-.2 3.2-.6" />
        <path d="M9.9 9.9a3 3 0 0 0 4.2 4.2" />
      </>
    ),

    arrow: (
      <>
        <path d="M5 12h14M13 6l6 6-6 6" />
      </>
    ),
  };

  return (
    <svg
      width={size}
      height={size}
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="1.8"
      strokeLinecap="round"
      strokeLinejoin="round"
      aria-hidden="true"
    >
      {paths[name]}
    </svg>
  );
}

export default function Login({ onLogin }) {
  const [email, setEmail] = useState(
    'admin@proximaetapa.org.br'
  );

  const [password, setPassword] = useState('123456');

  const [remember, setRemember] = useState(true);

  const [showPassword, setShowPassword] = useState(false);

  const [error, setError] = useState('');

  const [notice, setNotice] = useState('');

  const navigate = useNavigate();

  function submit(event) {
    event.preventDefault();

    setNotice('');

    if (
      email === 'admin@proximaetapa.org.br' &&
      password === '123456'
    ) {
      setError('');

      onLogin();

      navigate('/dashboard');

      return;
    }

    setError(
      'Use as credenciais de demonstração informadas abaixo.'
    );
  }

  function showNotice(message) {
    setError('');
    setNotice(message);
  }

  return (
    <main className="login-page">
      <section
        className="login-brand-panel"
        aria-label="Apresentação NextStep Solutions"
      >
        <div className="login-dots login-dots-top" />

        <div className="login-arc" />

        <img
          className="login-brand-logo"
          src={logo}
          alt="NextStep Solutions"
        />

        <div className="login-welcome">
          <p className="login-kicker">
            PLATAFORMA ADMINISTRATIVA
          </p>

          <h1>Bem-vindo(a)!</h1>

          <p>
            Faça login para acessar o painel administrativo
            do <strong>Próxima Etapa.</strong>
          </p>
        </div>

        <div
          className="login-illustration"
          aria-hidden="true"
        >
          <div className="illustration-plant">
            <i />
            <i />
            <i />
            <b />
          </div>

          <div className="illustration-desk">
            <div className="illustration-screen">
              <span />
              <span />
              <span />
              <em />
            </div>

            <div className="illustration-keyboard" />

            <div className="illustration-cup" />
          </div>

          <div className="illustration-person">
            <div className="person-head" />
            <div className="person-body" />
          </div>
        </div>

        <div className="login-dots login-dots-bottom" />
      </section>

      <section className="login-form-panel">
        <form
          className="login-card"
          onSubmit={submit}
        >
          <header className="login-card-header">
            <div className="login-security-icon">
              <Icon
                name="shield"
                size={31}
              />
            </div>

            <h2>Acesso Administrativo</h2>

            <p>
              Entre com suas credenciais para continuar
            </p>
          </header>

          <label className="login-field">
            <span>E-mail</span>

            <div className="login-input-wrap">
              <Icon name="user" />

              <input
                type="email"
                value={email}
                onChange={(event) =>
                  setEmail(event.target.value)
                }
                placeholder="seu@email.com"
                autoComplete="email"
                required
              />
            </div>
          </label>

          <label className="login-field">
            <span>Senha</span>

            <div className="login-input-wrap">
              <Icon name="lock" />

              <input
                type={
                  showPassword
                    ? 'text'
                    : 'password'
                }
                value={password}
                onChange={(event) =>
                  setPassword(event.target.value)
                }
                placeholder="••••••••••"
                autoComplete="current-password"
                required
              />

              <button
                type="button"
                className="password-toggle"
                onClick={() =>
                  setShowPassword(
                    (visible) => !visible
                  )
                }
                aria-label={
                  showPassword
                    ? 'Ocultar senha'
                    : 'Mostrar senha'
                }
              >
                <Icon
                  name={
                    showPassword
                      ? 'eyeOff'
                      : 'eye'
                  }
                  size={20}
                />
              </button>
            </div>
          </label>

          <div className="login-options">
            <label className="remember-option">
              <input
                type="checkbox"
                checked={remember}
                onChange={(event) =>
                  setRemember(
                    event.target.checked
                  )
                }
              />

              <span>Lembrar-me</span>
            </label>

            <button
              type="button"
              className="login-link"
              onClick={() =>
                showNotice(
                  'Entre em contato com o administrador para redefinir sua senha.'
                )
              }
            >
              Esqueceu sua senha?
            </button>
          </div>

          {error && (
            <p
              className="login-message login-error"
              role="alert"
            >
              {error}
            </p>
          )}

          {notice && (
            <p
              className="login-message login-notice"
              role="status"
            >
              {notice}
            </p>
          )}

          <button
            type="submit"
            className="login-submit"
          >
            Entrar

            <Icon
              name="arrow"
              size={21}
            />
          </button>

          <div className="login-divider">
            <span />
            ou
            <span />
          </div>

          <button
            type="button"
            className="access-code-button"
            onClick={() =>
              showNotice(
                'O acesso por código será disponibilizado em breve.'
              )
            }
          >
            <Icon
              name="shield"
              size={20}
            />

            Entrar com código de acesso
          </button>

          <p className="login-demo">
            Demonstração:{' '}
            <b>admin@proximaetapa.org.br</b>
            {' / '}
            <b>123456</b>
          </p>
        </form>

        <footer className="login-footer">
          © 2025 Próxima Etapa. Todos os direitos reservados.
        </footer>
      </section>
    </main>
  );
}