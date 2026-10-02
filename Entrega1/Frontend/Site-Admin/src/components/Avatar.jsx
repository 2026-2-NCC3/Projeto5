export default function Avatar({ initials, color = 'blue', size = 'normal' }) {
  return <span className={`avatar avatar-${color} avatar-${size}`}>{initials}</span>
}
