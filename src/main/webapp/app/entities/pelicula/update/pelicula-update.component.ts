import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPelicula, Pelicula } from '../pelicula.model';
import { PeliculaService } from '../service/pelicula.service';

@Component({
  selector: 'jhi-pelicula-update',
  templateUrl: './pelicula-update.component.html',
})
export class PeliculaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    titulo: [],
    genero: [],
    descripcion: [],
    calificacion: [],
    estreno: [],
    imagen: [],
    url: [],
  });

  constructor(protected peliculaService: PeliculaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pelicula }) => {
      if (pelicula.id === undefined) {
        const today = dayjs().startOf('day');
        pelicula.estreno = today;
      }

      this.updateForm(pelicula);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pelicula = this.createFromForm();
    if (pelicula.id !== undefined) {
      this.subscribeToSaveResponse(this.peliculaService.update(pelicula));
    } else {
      this.subscribeToSaveResponse(this.peliculaService.create(pelicula));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPelicula>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(pelicula: IPelicula): void {
    this.editForm.patchValue({
      id: pelicula.id,
      titulo: pelicula.titulo,
      genero: pelicula.genero,
      descripcion: pelicula.descripcion,
      calificacion: pelicula.calificacion,
      estreno: pelicula.estreno ? pelicula.estreno.format(DATE_TIME_FORMAT) : null,
      imagen: pelicula.imagen,
      url: pelicula.url,
    });
  }

  protected createFromForm(): IPelicula {
    return {
      ...new Pelicula(),
      id: this.editForm.get(['id'])!.value,
      titulo: this.editForm.get(['titulo'])!.value,
      genero: this.editForm.get(['genero'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      calificacion: this.editForm.get(['calificacion'])!.value,
      estreno: this.editForm.get(['estreno'])!.value ? dayjs(this.editForm.get(['estreno'])!.value, DATE_TIME_FORMAT) : undefined,
      imagen: this.editForm.get(['imagen'])!.value,
      url: this.editForm.get(['url'])!.value,
    };
  }
}
