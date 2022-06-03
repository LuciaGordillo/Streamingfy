import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ListaService } from '../service/lista.service';
import { ILista, Lista } from '../lista.model';
import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { PeliculaService } from 'app/entities/pelicula/service/pelicula.service';
import { IPerfiles } from 'app/entities/perfiles/perfiles.model';
import { PerfilesService } from 'app/entities/perfiles/service/perfiles.service';

import { ListaUpdateComponent } from './lista-update.component';

describe('Lista Management Update Component', () => {
  let comp: ListaUpdateComponent;
  let fixture: ComponentFixture<ListaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let listaService: ListaService;
  let peliculaService: PeliculaService;
  let perfilesService: PerfilesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ListaUpdateComponent],
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
      .overrideTemplate(ListaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ListaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    listaService = TestBed.inject(ListaService);
    peliculaService = TestBed.inject(PeliculaService);
    perfilesService = TestBed.inject(PerfilesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Pelicula query and add missing value', () => {
      const lista: ILista = { id: 456 };
      const pelicula: IPelicula = { id: 48971 };
      lista.pelicula = pelicula;

      const peliculaCollection: IPelicula[] = [{ id: 87246 }];
      jest.spyOn(peliculaService, 'query').mockReturnValue(of(new HttpResponse({ body: peliculaCollection })));
      const additionalPeliculas = [pelicula];
      const expectedCollection: IPelicula[] = [...additionalPeliculas, ...peliculaCollection];
      jest.spyOn(peliculaService, 'addPeliculaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lista });
      comp.ngOnInit();

      expect(peliculaService.query).toHaveBeenCalled();
      expect(peliculaService.addPeliculaToCollectionIfMissing).toHaveBeenCalledWith(peliculaCollection, ...additionalPeliculas);
      expect(comp.peliculasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Perfiles query and add missing value', () => {
      const lista: ILista = { id: 456 };
      const perfiles: IPerfiles = { id: 83342 };
      lista.perfiles = perfiles;

      const perfilesCollection: IPerfiles[] = [{ id: 48103 }];
      jest.spyOn(perfilesService, 'query').mockReturnValue(of(new HttpResponse({ body: perfilesCollection })));
      const additionalPerfiles = [perfiles];
      const expectedCollection: IPerfiles[] = [...additionalPerfiles, ...perfilesCollection];
      jest.spyOn(perfilesService, 'addPerfilesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lista });
      comp.ngOnInit();

      expect(perfilesService.query).toHaveBeenCalled();
      expect(perfilesService.addPerfilesToCollectionIfMissing).toHaveBeenCalledWith(perfilesCollection, ...additionalPerfiles);
      expect(comp.perfilesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const lista: ILista = { id: 456 };
      const pelicula: IPelicula = { id: 74892 };
      lista.pelicula = pelicula;
      const perfiles: IPerfiles = { id: 56773 };
      lista.perfiles = perfiles;

      activatedRoute.data = of({ lista });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(lista));
      expect(comp.peliculasSharedCollection).toContain(pelicula);
      expect(comp.perfilesSharedCollection).toContain(perfiles);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Lista>>();
      const lista = { id: 123 };
      jest.spyOn(listaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lista });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lista }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(listaService.update).toHaveBeenCalledWith(lista);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Lista>>();
      const lista = new Lista();
      jest.spyOn(listaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lista });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lista }));
      saveSubject.complete();

      // THEN
      expect(listaService.create).toHaveBeenCalledWith(lista);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Lista>>();
      const lista = { id: 123 };
      jest.spyOn(listaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lista });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(listaService.update).toHaveBeenCalledWith(lista);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPeliculaById', () => {
      it('Should return tracked Pelicula primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPeliculaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPerfilesById', () => {
      it('Should return tracked Perfiles primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPerfilesById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
