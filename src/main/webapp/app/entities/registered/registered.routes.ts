import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import RegisteredResolve from './route/registered-routing-resolve.service';

const registeredRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/registered.component').then(m => m.RegisteredComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/registered-detail.component').then(m => m.RegisteredDetailComponent),
    resolve: {
      registered: RegisteredResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/registered-update.component').then(m => m.RegisteredUpdateComponent),
    resolve: {
      registered: RegisteredResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/registered-update.component').then(m => m.RegisteredUpdateComponent),
    resolve: {
      registered: RegisteredResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default registeredRoute;
