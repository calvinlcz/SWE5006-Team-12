import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WorkSpaceResolve from './route/work-space-routing-resolve.service';

const workSpaceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/work-space.component').then(m => m.WorkSpaceComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/work-space-detail.component').then(m => m.WorkSpaceDetailComponent),
    resolve: {
      workSpace: WorkSpaceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/work-space-update.component').then(m => m.WorkSpaceUpdateComponent),
    resolve: {
      workSpace: WorkSpaceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/work-space-update.component').then(m => m.WorkSpaceUpdateComponent),
    resolve: {
      workSpace: WorkSpaceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workSpaceRoute;
