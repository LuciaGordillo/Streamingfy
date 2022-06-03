import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILista } from '../lista.model';
import { ListaService } from '../service/lista.service';

@Component({
  templateUrl: './lista-delete-dialog.component.html',
})
export class ListaDeleteDialogComponent {
  lista?: ILista;

  constructor(protected listaService: ListaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.listaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
