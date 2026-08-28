import {
    LayoutDashboard,
    Users,
    BookOpen,
    Calendar,
    ClipboardCheck,
    ListChecks,
    Award,
    CreditCard,
    Compass,
    MessageSquare,
    Bell,
    BarChart3,
    Settings,
  } from 'lucide-react';
  
  export const navSections = [
    {
      title: 'Principal',
      items: [{ label: 'Dashboard', path: '/', icon: LayoutDashboard }],
    },
    {
      title: 'Gestão',
      items: [
        { label: 'Alunos', path: '/alunos', icon: Users },
        { label: 'Cursos', path: '/cursos', icon: BookOpen },
        { label: 'Agenda', path: '/agenda', icon: Calendar },
        { label: 'Presenças', path: '/presencas', icon: ClipboardCheck },
        { label: 'Inscrições', path: '/inscricoes', icon: ListChecks },
      ],
    },
    {
      title: 'Documentos',
      items: [
        { label: 'Certificados', path: '/certificados', icon: Award },
        { label: 'Cards dos Alunos', path: '/cards', icon: CreditCard },
      ],
    },
    {
      title: 'Orientação',
      items: [{ label: 'Testes de Perfil', path: '/testes-perfil', icon: Compass }],
    },
    {
      title: 'Comunicação',
      items: [
        { label: 'Mensagens', path: '/mensagens', icon: MessageSquare, badge: 2 },
        { label: 'Notificações', path: '/notificacoes', icon: Bell },
      ],
    },
    {
      title: 'Análises',
      items: [{ label: 'Indicadores', path: '/indicadores', icon: BarChart3 }],
    },
  ];
  
  export const settingsItem = { label: 'Configurações', path: '/configuracoes', icon: Settings };