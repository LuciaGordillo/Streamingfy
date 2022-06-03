import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPerfiles } from '../perfiles.model';
import { PerfilesService } from '../service/perfiles.service';

@Component({
  templateUrl: './perfiles-delete-dialog.component.html',
})
export class PerfilesDeleteDialogComponent {
  perfiles?: IPerfiles;

  constructor(protected perfilesService: PerfilesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.perfilesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
