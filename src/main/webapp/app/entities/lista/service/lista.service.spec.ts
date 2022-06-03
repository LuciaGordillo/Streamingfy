import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILista, Lista } from '../lista.model';

import { ListaService } from './lista.service';

describe('Lista Service', () => {
  let service: ListaService;
  let httpMock: HttpTestingController;
  let elemDefault: ILista;
  let expectedResult: ILista | ILista[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ListaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      countryName: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Lista', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Lista()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Lista', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          countryName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Lista', () => {
      const patchObject = Object.assign({}, new Lista());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Lista', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          countryName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Lista', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addListaToCollectionIfMissing', () => {
      it('should add a Lista to an empty array', () => {
        const lista: ILista = { id: 123 };
        expectedResult = service.addListaToCollectionIfMissing([], lista);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lista);
      });

      it('should not add a Lista to an array that contains it', () => {
        const lista: ILista = { id: 123 };
        const listaCollection: ILista[] = [
          {
            ...lista,
          },
          { id: 456 },
        ];
        expectedResult = service.addListaToCollectionIfMissing(listaCollection, lista);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Lista to an array that doesn't contain it", () => {
        const lista: ILista = { id: 123 };
        const listaCollection: ILista[] = [{ id: 456 }];
        expectedResult = service.addListaToCollectionIfMissing(listaCollection, lista);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lista);
      });

      it('should add only unique Lista to an array', () => {
        const listaArray: ILista[] = [{ id: 123 }, { id: 456 }, { id: 52348 }];
        const listaCollection: ILista[] = [{ id: 123 }];
        expectedResult = service.addListaToCollectionIfMissing(listaCollection, ...listaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lista: ILista = { id: 123 };
        const lista2: ILista = { id: 456 };
        expectedResult = service.addListaToCollectionIfMissing([], lista, lista2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lista);
        expect(expectedResult).toContain(lista2);
      });

      it('should accept null and undefined values', () => {
        const lista: ILista = { id: 123 };
        expectedResult = service.addListaToCollectionIfMissing([], null, lista, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lista);
      });

      it('should return initial array if no Lista is added', () => {
        const listaCollection: ILista[] = [{ id: 123 }];
        expectedResult = service.addListaToCollectionIfMissing(listaCollection, undefined, null);
        expect(expectedResult).toEqual(listaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
