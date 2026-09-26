import EntityPage from '../../components/EntityPage/EntityPage';
import { moduleCatalog } from '../../data/adminData';
import './ModulesPage.css';

export default function ModulesPage({ module }) {
  return (
    <EntityPage
      config={moduleCatalog[module]}
    />
  );
}