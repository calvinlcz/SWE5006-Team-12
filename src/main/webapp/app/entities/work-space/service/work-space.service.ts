import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkSpace, NewWorkSpace } from '../work-space.model';

export type PartialUpdateWorkSpace = Partial<IWorkSpace> & Pick<IWorkSpace, 'id'>;

type RestOf<T extends IWorkSpace | NewWorkSpace> = Omit<T, 'dateCreated' | 'dateModified'> & {
  dateCreated?: string | null;
  dateModified?: string | null;
};

export type RestWorkSpace = RestOf<IWorkSpace>;

export type NewRestWorkSpace = RestOf<NewWorkSpace>;

export type PartialUpdateRestWorkSpace = RestOf<PartialUpdateWorkSpace>;

export type EntityResponseType = HttpResponse<IWorkSpace>;
export type EntityArrayResponseType = HttpResponse<IWorkSpace[]>;

@Injectable({ providedIn: 'root' })
export class WorkSpaceService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/work-spaces');

  create(workSpace: NewWorkSpace): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workSpace);
    return this.http
      .post<RestWorkSpace>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(workSpace: IWorkSpace): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workSpace);
    return this.http
      .put<RestWorkSpace>(`${this.resourceUrl}/${this.getWorkSpaceIdentifier(workSpace)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(workSpace: PartialUpdateWorkSpace): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workSpace);
    return this.http
      .patch<RestWorkSpace>(`${this.resourceUrl}/${this.getWorkSpaceIdentifier(workSpace)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWorkSpace>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWorkSpace[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWorkSpaceIdentifier(workSpace: Pick<IWorkSpace, 'id'>): number {
    return workSpace.id;
  }

  compareWorkSpace(o1: Pick<IWorkSpace, 'id'> | null, o2: Pick<IWorkSpace, 'id'> | null): boolean {
    return o1 && o2 ? this.getWorkSpaceIdentifier(o1) === this.getWorkSpaceIdentifier(o2) : o1 === o2;
  }

  addWorkSpaceToCollectionIfMissing<Type extends Pick<IWorkSpace, 'id'>>(
    workSpaceCollection: Type[],
    ...workSpacesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const workSpaces: Type[] = workSpacesToCheck.filter(isPresent);
    if (workSpaces.length > 0) {
      const workSpaceCollectionIdentifiers = workSpaceCollection.map(workSpaceItem => this.getWorkSpaceIdentifier(workSpaceItem));
      const workSpacesToAdd = workSpaces.filter(workSpaceItem => {
        const workSpaceIdentifier = this.getWorkSpaceIdentifier(workSpaceItem);
        if (workSpaceCollectionIdentifiers.includes(workSpaceIdentifier)) {
          return false;
        }
        workSpaceCollectionIdentifiers.push(workSpaceIdentifier);
        return true;
      });
      return [...workSpacesToAdd, ...workSpaceCollection];
    }
    return workSpaceCollection;
  }

  protected convertDateFromClient<T extends IWorkSpace | NewWorkSpace | PartialUpdateWorkSpace>(workSpace: T): RestOf<T> {
    return {
      ...workSpace,
      dateCreated: workSpace.dateCreated?.format(DATE_FORMAT) ?? null,
      dateModified: workSpace.dateModified?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restWorkSpace: RestWorkSpace): IWorkSpace {
    return {
      ...restWorkSpace,
      dateCreated: restWorkSpace.dateCreated ? dayjs(restWorkSpace.dateCreated) : undefined,
      dateModified: restWorkSpace.dateModified ? dayjs(restWorkSpace.dateModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWorkSpace>): HttpResponse<IWorkSpace> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWorkSpace[]>): HttpResponse<IWorkSpace[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
