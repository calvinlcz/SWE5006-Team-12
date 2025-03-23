import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'toDoListApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'registered',
    data: { pageTitle: 'toDoListApp.registered.home.title' },
    loadChildren: () => import('./registered/registered.routes'),
  },
  {
    path: 'work-space',
    data: { pageTitle: 'toDoListApp.workSpace.home.title' },
    loadChildren: () => import('./work-space/work-space.routes'),
  },
  {
    path: 'task',
    data: { pageTitle: 'toDoListApp.task.home.title' },
    loadChildren: () => import('./task/task.routes'),
  },
  {
    path: 'notification',
    data: { pageTitle: 'toDoListApp.notification.home.title' },
    loadChildren: () => import('./notification/notification.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
