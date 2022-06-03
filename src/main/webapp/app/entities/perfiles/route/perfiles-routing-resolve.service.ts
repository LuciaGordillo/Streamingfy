import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPerfiles, Perfiles } from '../perfiles.model';
import { PerfilesService } from '../service/perfiles.service';

@Injectable({ providedIn: 'root' })
export class PerfilesRoutingResolveService implements Resolve<IPerfiles> {
  constructor(protected service: PerfilesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPerfiles> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((perfiles: HttpResponse<Perfiles>) => {
          if (perfiles.body) {
            return of(perfiles.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Perfiles());
  }
}
