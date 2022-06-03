import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILista } from '../lista.model';

@Component({
  selector: 'jhi-lista-detail',
  templateUrl: './lista-detail.component.html',
})
export class ListaDetailComponent implements OnInit {
  lista: ILista | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lista }) => {
      this.lista = lista;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
