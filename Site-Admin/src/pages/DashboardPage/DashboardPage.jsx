import { useEffect, useState } from 'react';
import { getDashboard } from '../../services/api';
import { activity as mockActivity, metrics as mockMetrics } from '../../services/mockData';
import StatCard from '../../components/StatCard';
import Avatar from '../../components/Avatar';
import './DashboardPage.css';

const months = [
  'Jan',
  'Fev',
  'Mar',
  'Abr',
  'Mai',
  'Jun',
  'Jul',
  'Ago',
  'Set',
  'Out',
  'Nov',
  'Dez',
];

const bars = [40, 53, 43, 65, 56, 79, 69, 94, 78, 88, 72, 98];

export default function DashboardPage() {
  const [dashboard, setDashboard] = useState(null);

  useEffect(() => {
    getDashboard()
      .then(setDashboard)
      .catch(() => setDashboard(null));
  }, []);

  const metrics = mockMetrics.map((metric, index) => {
    const aliases = [
      ['students', 'students_count', 'total_students', 'alunos'],
      ['courses', 'courses_count', 'total_courses', 'cursos'],
      ['enrollments', 'enrollments_count', 'active_enrollments', 'inscricoes'],
      ['attendance_rate', 'attendance', 'presenca'],
    ][index];
    const source = dashboard?.metrics ?? dashboard;
    const value = aliases
      .map((key) => source?.[key])
      .find((candidate) => candidate !== undefined && candidate !== null && candidate !== '');
    const labeledMetric = Array.isArray(source)
      ? source.find((item) => item.label === metric.label || aliases.includes(item.key))
      : null;

    return value !== undefined || labeledMetric
      ? { ...metric, ...(labeledMetric ?? {}), ...(value !== undefined ? { value: String(value) } : {}) }
      : metric;
  });
  const realActivity = dashboard?.activity ?? dashboard?.recent_activity ?? dashboard?.recentActivity;
  const activity = Array.isArray(realActivity) && realActivity.length > 0
    ? realActivity.map((item) => ({
        ...item,
        name: item.name ?? item.user_name ?? item.user ?? 'Usuário',
        action: item.action ?? item.description ?? item.event ?? '',
        time: item.time ?? item.created_at ?? '',
        initials: item.initials ?? (item.name ?? item.user_name ?? item.user ?? 'U').split(/\s+/).map((part) => part[0]).slice(0, 2).join('').toUpperCase(),
        color: item.color ?? 'blue',
      }))
    : mockActivity;
  function handleExport() {
    window.alert(
      'Relatório demonstrativo preparado para exportação.'
    );
  }

  function handleViewAll() {
    window.alert(
      'A lista completa de atividades será exibida aqui.'
    );
  }

  return (
    <>
      <div className="page-heading">
        <div>
          <p className="eyebrow">Próxima Etapa</p>

          <h1>Visão geral</h1>

          <span>
            Acompanhe os principais indicadores da plataforma.
          </span>
        </div>

        <button
          type="button"
          className="primary-button"
          onClick={handleExport}
        >
          Exportar relatório
        </button>
      </div>

      <section className="metrics-grid">
        {metrics.map((metric) => (
          <StatCard
            metric={metric}
            key={metric.label}
          />
        ))}
      </section>

      <section className="dashboard-grid">
        <article className="card chart-card">
          <header>
            <div>
              <h2>Visão de receita</h2>

              <p>
                Desempenho dos últimos 12 meses
              </p>
            </div>

            <select aria-label="Período">
              <option value="year">Este ano</option>
              <option value="six-months">
                Últimos 6 meses
              </option>
            </select>
          </header>

          <div className="chart">
            <div className="chart-axis">
              <span>50k</span>
              <span>40k</span>
              <span>30k</span>
              <span>20k</span>
              <span>10k</span>
              <span>0</span>
            </div>

            <div className="bars">
              {bars.map((bar, index) => (
                <div
                  className="bar-column"
                  key={months[index]}
                >
                  <div
                    className="bar"
                    style={{
                      height: `${bar}%`,
                    }}
                    title={`R$ ${(bar * 500).toLocaleString(
                      'pt-BR'
                    )}`}
                  />

                  <span>{months[index]}</span>
                </div>
              ))}
            </div>
          </div>
        </article>

        <article className="card activity-card">
          <header>
            <div>
              <h2>Atividade recente</h2>

              <p>
                Últimas ações na plataforma
              </p>
            </div>

            <button
              type="button"
              className="text-button"
              onClick={handleViewAll}
            >
              Ver todas
            </button>
          </header>

          <div className="activity-list">
            {activity.map((item) => (
              <div
                className="activity-item"
                key={item.name}
              >
                <Avatar
                  initials={item.initials}
                  color={item.color}
                />

                <div>
                  <strong>{item.name}</strong>

                  <p>{item.action}</p>
                </div>

                <time>{item.time}</time>
              </div>
            ))}
          </div>
        </article>
      </section>
    </>
  );
}
