import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILista, Lista } from '../lista.model';
import { ListaService } from '../service/lista.service';

@Injectable({ providedIn: 'root' })
export class ListaRoutingResolveService implements Resolve<ILista> {
  constructor(protected service: ListaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILista> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lista: HttpResponse<Lista>) => {
          if (lista.body) {
            return of(lista.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Lista());
  }
}
