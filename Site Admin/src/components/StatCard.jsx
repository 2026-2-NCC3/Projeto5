import Icon from './Icon'

export default function StatCard({ metric }) {
  return <article className="stat-card">
    <div className={`metric-icon ${metric.tone}`}><Icon name={metric.icon} size={21} /></div>
    <div><p>{metric.label}</p><strong>{metric.value}</strong></div>
    <span className="positive"><Icon name="arrowUp" size={14} /> {metric.change}</span>
  </article>
}
