import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PeliculaComponent } from '../list/pelicula.component';
import { PeliculaDetailComponent } from '../detail/pelicula-detail.component';
import { PeliculaUpdateComponent } from '../update/pelicula-update.component';
import { PeliculaRoutingResolveService } from './pelicula-routing-resolve.service';
import { PeliculaListadoComponent } from '../registros/listado.component';
import { BuscarComponent } from '../busqueda/busqueda.component';
import { ReproducirComponent } from '../reproducir/reproducir.component';
const peliculaRoute: Routes = [
  {
    path: '',
    component: PeliculaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PeliculaDetailComponent,
    resolve: {
      pelicula: PeliculaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PeliculaUpdateComponent,
    resolve: {
      pelicula: PeliculaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PeliculaUpdateComponent,
    resolve: {
      pelicula: PeliculaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'listado',
    component: PeliculaListadoComponent,
    resolve: {
      pelicula: PeliculaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':txtBuscar/buscar',
    component: BuscarComponent,
    resolve: {
      pelicula: PeliculaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/ver',
    component: ReproducirComponent,
    resolve: {
      pelicula: PeliculaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(peliculaRoute)],
  exports: [RouterModule],
})
export class PeliculaRoutingModule {}
