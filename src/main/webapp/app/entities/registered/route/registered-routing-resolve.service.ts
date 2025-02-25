import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRegistered } from '../registered.model';
import { RegisteredService } from '../service/registered.service';

const registeredResolve = (route: ActivatedRouteSnapshot): Observable<null | IRegistered> => {
  const id = route.params.id;
  if (id) {
    return inject(RegisteredService)
      .find(id)
      .pipe(
        mergeMap((registered: HttpResponse<IRegistered>) => {
          if (registered.body) {
            return of(registered.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default registeredResolve;
