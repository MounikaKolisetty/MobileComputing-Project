import paths from './paths';
import { lazy } from 'react';

const DashboardPage = lazy(() => import('../components/home/'));
const LoginPage = lazy(() => import('../components/login/'))

const routes = [
    {
      path: paths.dashboard,
      component: DashboardPage,
      isSecured: true
    },
    {
        path: paths.login,
        component: LoginPage,
        isSecured: false
    },
    // {
    //   path: paths.tickets,
    //   component: Tacos,
    //   isSecured: true,
    //   routes: [
    //     {
    //       path: paths.ticketDetails,
    //       component: Bus
    //     }
    //   ]
    // }
  ];

export default routes;