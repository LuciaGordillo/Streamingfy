import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ListaComponent } from './list/lista.component';
import { ListaDetailComponent } from './detail/lista-detail.component';
import { ListaUpdateComponent } from './update/lista-update.component';
import { ListaDeleteDialogComponent } from './delete/lista-delete-dialog.component';
import { ListaRoutingModule } from './route/lista-routing.module';

@NgModule({
  imports: [SharedModule, ListaRoutingModule],
  declarations: [ListaComponent, ListaDetailComponent, ListaUpdateComponent, ListaDeleteDialogComponent],
  entryComponents: [ListaDeleteDialogComponent],
})
export class ListaModule {}
