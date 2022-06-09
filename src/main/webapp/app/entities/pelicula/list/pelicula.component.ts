import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';

import { IPelicula } from '../pelicula.model';
import { PeliculaService } from '../service/pelicula.service';
import { PeliculaDeleteDialogComponent } from '../delete/pelicula-delete-dialog.component';
import { PeliculaModalComponent } from '../modal/pelicula-modal.component';

@Component({
  selector: 'jhi-pelicula',
  templateUrl: './pelicula.component.html',
})
export class PeliculaComponent implements OnInit {
  peliculas?: IPelicula[];
  isLoading = false;
  images = [`https://i.imgur.com/oOZB19w.jpg`, `https://i.imgur.com/r76kHRX.jpg`, `https://i.imgur.com/HzFeR7t.jpg`];

  constructor(
    protected peliculaService: PeliculaService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.peliculaService.query().subscribe({
      next: (res: HttpResponse<IPelicula[]>) => {
        this.isLoading = false;
        this.peliculas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
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
        this.loadAll();
      }
    });
  }
  delete(pelicula: IPelicula): void {
    const modalRef = this.modalService.open(PeliculaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pelicula = pelicula;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
