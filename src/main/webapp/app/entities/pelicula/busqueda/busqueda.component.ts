import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';

import { IPelicula, Pelicula } from '../pelicula.model';
import { PeliculaService } from '../service/pelicula.service';
import { PeliculaModalComponent } from '../modal/pelicula-modal.component';

@Component({
  selector: 'jhi-busqueda',
  templateUrl: './busqueda.component.html',
  styleUrls: ['./pelicula.component.css'],
})
export class BuscarComponent implements OnInit {
  peliculas?: IPelicula[] = [];
  isLoading = false;
  public filtro = '';
  /*   public filtrados: Pelicula[] = [];
   */
  constructor(
    protected peliculaService: PeliculaService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    /*    this.isLoading = true;*/

    this.activatedRoute.params.subscribe(params => {
      this.filtro = params.txtBuscar.trim();

      this.peliculas = [];
      this.peliculaService.buscar(this.filtro).subscribe({
        next: (res: HttpResponse<IPelicula[]>) => {
          this.isLoading = false;
          this.peliculas = res.body ?? [];
        },
        error: () => {
          this.isLoading = false;
        },
      });
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPelicula): number {
    return item.id!;
  }

  open(pelicula: IPelicula): void {
    const modalRef = this.modalService.open(PeliculaModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pelicula = pelicula;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.ngOnInit();
      }
    });
  }
}
