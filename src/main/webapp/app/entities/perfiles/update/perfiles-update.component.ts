import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPerfiles, Perfiles } from '../perfiles.model';
import { PerfilesService } from '../service/perfiles.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { PeliculaService } from 'app/entities/pelicula/service/pelicula.service';

@Component({
  selector: 'jhi-perfiles-update',
  templateUrl: './perfiles-update.component.html',
})
export class PerfilesUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  peliculasSharedCollection: IPelicula[] = [];

  editForm = this.fb.group({
    id: [],
    imagen: [],
    nombre: [],
    user: [],
    pelicula: [],
  });

  constructor(
    protected perfilesService: PerfilesService,
    protected userService: UserService,
    protected peliculaService: PeliculaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ perfiles }) => {
      this.updateForm(perfiles);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const perfiles = this.createFromForm();
    if (perfiles.id !== undefined) {
      this.subscribeToSaveResponse(this.perfilesService.update(perfiles));
    } else {
      this.subscribeToSaveResponse(this.perfilesService.create(perfiles));
    }
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  trackPeliculaById(_index: number, item: IPelicula): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerfiles>>): void {
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

  protected updateForm(perfiles: IPerfiles): void {
    this.editForm.patchValue({
      id: perfiles.id,
      imagen: perfiles.imagen,
      nombre: perfiles.nombre,
      user: perfiles.user,
      pelicula: perfiles.pelicula,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, perfiles.user);
    this.peliculasSharedCollection = this.peliculaService.addPeliculaToCollectionIfMissing(
      this.peliculasSharedCollection,
      perfiles.pelicula
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.peliculaService
      .query()
      .pipe(map((res: HttpResponse<IPelicula[]>) => res.body ?? []))
      .pipe(
        map((peliculas: IPelicula[]) =>
          this.peliculaService.addPeliculaToCollectionIfMissing(peliculas, this.editForm.get('pelicula')!.value)
        )
      )
      .subscribe((peliculas: IPelicula[]) => (this.peliculasSharedCollection = peliculas));
  }

  protected createFromForm(): IPerfiles {
    return {
      ...new Perfiles(),
      id: this.editForm.get(['id'])!.value,
      imagen: this.editForm.get(['imagen'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      user: this.editForm.get(['user'])!.value,
      pelicula: this.editForm.get(['pelicula'])!.value,
    };
  }
}
