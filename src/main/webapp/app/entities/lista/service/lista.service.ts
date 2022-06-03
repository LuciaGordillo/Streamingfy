import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILista, getListaIdentifier } from '../lista.model';

export type EntityResponseType = HttpResponse<ILista>;
export type EntityArrayResponseType = HttpResponse<ILista[]>;

@Injectable({ providedIn: 'root' })
export class ListaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/listas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lista: ILista): Observable<EntityResponseType> {
    return this.http.post<ILista>(this.resourceUrl, lista, { observe: 'response' });
  }

  update(lista: ILista): Observable<EntityResponseType> {
    return this.http.put<ILista>(`${this.resourceUrl}/${getListaIdentifier(lista) as number}`, lista, { observe: 'response' });
  }

  partialUpdate(lista: ILista): Observable<EntityResponseType> {
    return this.http.patch<ILista>(`${this.resourceUrl}/${getListaIdentifier(lista) as number}`, lista, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILista>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILista[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addListaToCollectionIfMissing(listaCollection: ILista[], ...listasToCheck: (ILista | null | undefined)[]): ILista[] {
    const listas: ILista[] = listasToCheck.filter(isPresent);
    if (listas.length > 0) {
      const listaCollectionIdentifiers = listaCollection.map(listaItem => getListaIdentifier(listaItem)!);
      const listasToAdd = listas.filter(listaItem => {
        const listaIdentifier = getListaIdentifier(listaItem);
        if (listaIdentifier == null || listaCollectionIdentifiers.includes(listaIdentifier)) {
          return false;
        }
        listaCollectionIdentifiers.push(listaIdentifier);
        return true;
      });
      return [...listasToAdd, ...listaCollection];
    }
    return listaCollection;
  }
}
