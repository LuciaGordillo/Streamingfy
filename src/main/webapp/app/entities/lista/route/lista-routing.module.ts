import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ListaComponent } from '../list/lista.component';
import { ListaDetailComponent } from '../detail/lista-detail.component';
import { ListaUpdateComponent } from '../update/lista-update.component';
import { ListaRoutingResolveService } from './lista-routing-resolve.service';

const listaRoute: Routes = [
  {
    path: '',
    component: ListaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ListaDetailComponent,
    resolve: {
      lista: ListaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ListaUpdateComponent,
    resolve: {
      lista: ListaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ListaUpdateComponent,
    resolve: {
      lista: ListaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(listaRoute)],
  exports: [RouterModule],
})
export class ListaRoutingModule {}
