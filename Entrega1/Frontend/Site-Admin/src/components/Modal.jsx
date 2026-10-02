import { useEffect } from 'react'
import Icon from './Icon'

export default function Modal({ title, children, onClose }) {
  useEffect(() => {
    const closeOnEscape = (event) => event.key === 'Escape' && onClose()
    document.addEventListener('keydown', closeOnEscape)
    return () => document.removeEventListener('keydown', closeOnEscape)
  }, [onClose])
  return <div className="modal-backdrop" role="presentation" onMouseDown={onClose}>
    <section className="modal" role="dialog" aria-modal="true" aria-labelledby="modal-title" onMouseDown={(event) => event.stopPropagation()}>
      <header><h2 id="modal-title">{title}</h2><button className="icon-button" onClick={onClose} aria-label="Fechar"><Icon name="close" /></button></header>
      {children}
    </section>
  </div>
}
