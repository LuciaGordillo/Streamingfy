import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PerfilesComponent } from '../list/perfiles.component';
import { PerfilesDetailComponent } from '../detail/perfiles-detail.component';
import { PerfilesUpdateComponent } from '../update/perfiles-update.component';
import { PerfilesRoutingResolveService } from './perfiles-routing-resolve.service';

const perfilesRoute: Routes = [
  {
    path: '',
    component: PerfilesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PerfilesDetailComponent,
    resolve: {
      perfiles: PerfilesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PerfilesUpdateComponent,
    resolve: {
      perfiles: PerfilesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PerfilesUpdateComponent,
    resolve: {
      perfiles: PerfilesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(perfilesRoute)],
  exports: [RouterModule],
})
export class PerfilesRoutingModule {}
