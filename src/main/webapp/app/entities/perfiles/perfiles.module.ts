import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PerfilesComponent } from './list/perfiles.component';
import { PerfilesDetailComponent } from './detail/perfiles-detail.component';
import { PerfilesUpdateComponent } from './update/perfiles-update.component';
import { PerfilesDeleteDialogComponent } from './delete/perfiles-delete-dialog.component';
import { PerfilesRoutingModule } from './route/perfiles-routing.module';

@NgModule({
  imports: [SharedModule, PerfilesRoutingModule],
  declarations: [PerfilesComponent, PerfilesDetailComponent, PerfilesUpdateComponent, PerfilesDeleteDialogComponent],
  entryComponents: [PerfilesDeleteDialogComponent],
})
export class PerfilesModule {}
