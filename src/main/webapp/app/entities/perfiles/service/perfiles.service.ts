import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPerfiles, getPerfilesIdentifier } from '../perfiles.model';

export type EntityResponseType = HttpResponse<IPerfiles>;
export type EntityArrayResponseType = HttpResponse<IPerfiles[]>;

@Injectable({ providedIn: 'root' })
export class PerfilesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/perfiles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(perfiles: IPerfiles): Observable<EntityResponseType> {
    return this.http.post<IPerfiles>(this.resourceUrl, perfiles, { observe: 'response' });
  }

  update(perfiles: IPerfiles): Observable<EntityResponseType> {
    return this.http.put<IPerfiles>(`${this.resourceUrl}/${getPerfilesIdentifier(perfiles) as number}`, perfiles, { observe: 'response' });
  }

  partialUpdate(perfiles: IPerfiles): Observable<EntityResponseType> {
    return this.http.patch<IPerfiles>(`${this.resourceUrl}/${getPerfilesIdentifier(perfiles) as number}`, perfiles, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPerfiles>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPerfiles[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPerfilesToCollectionIfMissing(perfilesCollection: IPerfiles[], ...perfilesToCheck: (IPerfiles | null | undefined)[]): IPerfiles[] {
    const perfiles: IPerfiles[] = perfilesToCheck.filter(isPresent);
    if (perfiles.length > 0) {
      const perfilesCollectionIdentifiers = perfilesCollection.map(perfilesItem => getPerfilesIdentifier(perfilesItem)!);
      const perfilesToAdd = perfiles.filter(perfilesItem => {
        const perfilesIdentifier = getPerfilesIdentifier(perfilesItem);
        if (perfilesIdentifier == null || perfilesCollectionIdentifiers.includes(perfilesIdentifier)) {
          return false;
        }
        perfilesCollectionIdentifiers.push(perfilesIdentifier);
        return true;
      });
      return [...perfilesToAdd, ...perfilesCollection];
    }
    return perfilesCollection;
  }
}
