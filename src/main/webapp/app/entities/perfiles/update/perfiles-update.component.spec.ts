import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PerfilesService } from '../service/perfiles.service';
import { IPerfiles, Perfiles } from '../perfiles.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { PeliculaService } from 'app/entities/pelicula/service/pelicula.service';

import { PerfilesUpdateComponent } from './perfiles-update.component';

describe('Perfiles Management Update Component', () => {
  let comp: PerfilesUpdateComponent;
  let fixture: ComponentFixture<PerfilesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let perfilesService: PerfilesService;
  let userService: UserService;
  let peliculaService: PeliculaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PerfilesUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PerfilesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PerfilesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    perfilesService = TestBed.inject(PerfilesService);
    userService = TestBed.inject(UserService);
    peliculaService = TestBed.inject(PeliculaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const perfiles: IPerfiles = { id: 456 };
      const user: IUser = { id: 2879 };
      perfiles.user = user;

      const userCollection: IUser[] = [{ id: 98062 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ perfiles });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Pelicula query and add missing value', () => {
      const perfiles: IPerfiles = { id: 456 };
      const pelicula: IPelicula = { id: 12109 };
      perfiles.pelicula = pelicula;

      const peliculaCollection: IPelicula[] = [{ id: 72290 }];
      jest.spyOn(peliculaService, 'query').mockReturnValue(of(new HttpResponse({ body: peliculaCollection })));
      const additionalPeliculas = [pelicula];
      const expectedCollection: IPelicula[] = [...additionalPeliculas, ...peliculaCollection];
      jest.spyOn(peliculaService, 'addPeliculaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ perfiles });
      comp.ngOnInit();

      expect(peliculaService.query).toHaveBeenCalled();
      expect(peliculaService.addPeliculaToCollectionIfMissing).toHaveBeenCalledWith(peliculaCollection, ...additionalPeliculas);
      expect(comp.peliculasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const perfiles: IPerfiles = { id: 456 };
      const user: IUser = { id: 77122 };
      perfiles.user = user;
      const pelicula: IPelicula = { id: 63427 };
      perfiles.pelicula = pelicula;

      activatedRoute.data = of({ perfiles });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(perfiles));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.peliculasSharedCollection).toContain(pelicula);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Perfiles>>();
      const perfiles = { id: 123 };
      jest.spyOn(perfilesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfiles });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: perfiles }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(perfilesService.update).toHaveBeenCalledWith(perfiles);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Perfiles>>();
      const perfiles = new Perfiles();
      jest.spyOn(perfilesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfiles });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: perfiles }));
      saveSubject.complete();

      // THEN
      expect(perfilesService.create).toHaveBeenCalledWith(perfiles);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Perfiles>>();
      const perfiles = { id: 123 };
      jest.spyOn(perfilesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfiles });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(perfilesService.update).toHaveBeenCalledWith(perfiles);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPeliculaById', () => {
      it('Should return tracked Pelicula primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPeliculaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
