import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkSpace } from '../work-space.model';
import { WorkSpaceService } from '../service/work-space.service';

const workSpaceResolve = (route: ActivatedRouteSnapshot): Observable<null | IWorkSpace> => {
  const id = route.params.id;
  if (id) {
    return inject(WorkSpaceService)
      .find(id)
      .pipe(
        mergeMap((workSpace: HttpResponse<IWorkSpace>) => {
          if (workSpace.body) {
            return of(workSpace.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default workSpaceResolve;
