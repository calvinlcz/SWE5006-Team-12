import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRegistered, NewRegistered } from '../registered.model';

export type PartialUpdateRegistered = Partial<IRegistered> & Pick<IRegistered, 'id'>;

export type EntityResponseType = HttpResponse<IRegistered>;
export type EntityArrayResponseType = HttpResponse<IRegistered[]>;

@Injectable({ providedIn: 'root' })
export class RegisteredService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/registereds');

  create(registered: NewRegistered): Observable<EntityResponseType> {
    return this.http.post<IRegistered>(this.resourceUrl, registered, { observe: 'response' });
  }

  update(registered: IRegistered): Observable<EntityResponseType> {
    return this.http.put<IRegistered>(`${this.resourceUrl}/${this.getRegisteredIdentifier(registered)}`, registered, {
      observe: 'response',
    });
  }

  partialUpdate(registered: PartialUpdateRegistered): Observable<EntityResponseType> {
    return this.http.patch<IRegistered>(`${this.resourceUrl}/${this.getRegisteredIdentifier(registered)}`, registered, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRegistered>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRegistered[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRegisteredIdentifier(registered: Pick<IRegistered, 'id'>): number {
    return registered.id;
  }

  compareRegistered(o1: Pick<IRegistered, 'id'> | null, o2: Pick<IRegistered, 'id'> | null): boolean {
    return o1 && o2 ? this.getRegisteredIdentifier(o1) === this.getRegisteredIdentifier(o2) : o1 === o2;
  }

  addRegisteredToCollectionIfMissing<Type extends Pick<IRegistered, 'id'>>(
    registeredCollection: Type[],
    ...registeredsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const registereds: Type[] = registeredsToCheck.filter(isPresent);
    if (registereds.length > 0) {
      const registeredCollectionIdentifiers = registeredCollection.map(registeredItem => this.getRegisteredIdentifier(registeredItem));
      const registeredsToAdd = registereds.filter(registeredItem => {
        const registeredIdentifier = this.getRegisteredIdentifier(registeredItem);
        if (registeredCollectionIdentifiers.includes(registeredIdentifier)) {
          return false;
        }
        registeredCollectionIdentifiers.push(registeredIdentifier);
        return true;
      });
      return [...registeredsToAdd, ...registeredCollection];
    }
    return registeredCollection;
  }
}
