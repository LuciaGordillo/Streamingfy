import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PeliculaComponent } from './list/pelicula.component';
import { PeliculaDetailComponent } from './detail/pelicula-detail.component';
import { PeliculaUpdateComponent } from './update/pelicula-update.component';
import { PeliculaDeleteDialogComponent } from './delete/pelicula-delete-dialog.component';
import { PeliculaRoutingModule } from './route/pelicula-routing.module';
import { PeliculaModalComponent } from './modal/pelicula-modal.component';
import { PeliculaListadoComponent } from './registros/listado.component';
import { ReproducirComponent } from './reproducir/reproducir.component';

import { BuscarComponent } from './busqueda/busqueda.component';
@NgModule({
  imports: [SharedModule, PeliculaRoutingModule],
  declarations: [
    PeliculaComponent,
    PeliculaDetailComponent,
    PeliculaUpdateComponent,
    PeliculaDeleteDialogComponent,
    PeliculaModalComponent,
    PeliculaListadoComponent,
    BuscarComponent,
    ReproducirComponent,
  ],

  entryComponents: [PeliculaDeleteDialogComponent, PeliculaModalComponent],
})
export class PeliculaModule {}
