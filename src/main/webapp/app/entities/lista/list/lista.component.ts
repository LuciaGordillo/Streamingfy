import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILista } from '../lista.model';
import { ListaService } from '../service/lista.service';
import { ListaDeleteDialogComponent } from '../delete/lista-delete-dialog.component';

@Component({
  selector: 'jhi-lista',
  templateUrl: './lista.component.html',
})
export class ListaComponent implements OnInit {
  listas?: ILista[];
  isLoading = false;

  constructor(protected listaService: ListaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.listaService.query().subscribe({
      next: (res: HttpResponse<ILista[]>) => {
        this.isLoading = false;
        this.listas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ILista): number {
    return item.id!;
  }

  delete(lista: ILista): void {
    const modalRef = this.modalService.open(ListaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lista = lista;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
