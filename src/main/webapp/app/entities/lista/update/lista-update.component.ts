import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILista, Lista } from '../lista.model';
import { ListaService } from '../service/lista.service';
import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { PeliculaService } from 'app/entities/pelicula/service/pelicula.service';
import { IPerfiles } from 'app/entities/perfiles/perfiles.model';
import { PerfilesService } from 'app/entities/perfiles/service/perfiles.service';

@Component({
  selector: 'jhi-lista-update',
  templateUrl: './lista-update.component.html',
})
export class ListaUpdateComponent implements OnInit {
  isSaving = false;

  peliculasSharedCollection: IPelicula[] = [];
  perfilesSharedCollection: IPerfiles[] = [];

  editForm = this.fb.group({
    id: [],
    countryName: [],
    pelicula: [],
    perfiles: [],
  });

  constructor(
    protected listaService: ListaService,
    protected peliculaService: PeliculaService,
    protected perfilesService: PerfilesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lista }) => {
      this.updateForm(lista);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lista = this.createFromForm();
    if (lista.id !== undefined) {
      this.subscribeToSaveResponse(this.listaService.update(lista));
    } else {
      this.subscribeToSaveResponse(this.listaService.create(lista));
    }
  }

  trackPeliculaById(_index: number, item: IPelicula): number {
    return item.id!;
  }

  trackPerfilesById(_index: number, item: IPerfiles): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILista>>): void {
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

  protected updateForm(lista: ILista): void {
    this.editForm.patchValue({
      id: lista.id,
      countryName: lista.countryName,
      pelicula: lista.pelicula,
      perfiles: lista.perfiles,
    });

    this.peliculasSharedCollection = this.peliculaService.addPeliculaToCollectionIfMissing(this.peliculasSharedCollection, lista.pelicula);
    this.perfilesSharedCollection = this.perfilesService.addPerfilesToCollectionIfMissing(this.perfilesSharedCollection, lista.perfiles);
  }

  protected loadRelationshipsOptions(): void {
    this.peliculaService
      .query()
      .pipe(map((res: HttpResponse<IPelicula[]>) => res.body ?? []))
      .pipe(
        map((peliculas: IPelicula[]) =>
          this.peliculaService.addPeliculaToCollectionIfMissing(peliculas, this.editForm.get('pelicula')!.value)
        )
      )
      .subscribe((peliculas: IPelicula[]) => (this.peliculasSharedCollection = peliculas));

    this.perfilesService
      .query()
      .pipe(map((res: HttpResponse<IPerfiles[]>) => res.body ?? []))
      .pipe(
        map((perfiles: IPerfiles[]) =>
          this.perfilesService.addPerfilesToCollectionIfMissing(perfiles, this.editForm.get('perfiles')!.value)
        )
      )
      .subscribe((perfiles: IPerfiles[]) => (this.perfilesSharedCollection = perfiles));
  }

  protected createFromForm(): ILista {
    return {
      ...new Lista(),
      id: this.editForm.get(['id'])!.value,
      countryName: this.editForm.get(['countryName'])!.value,
      pelicula: this.editForm.get(['pelicula'])!.value,
      perfiles: this.editForm.get(['perfiles'])!.value,
    };
  }
}
