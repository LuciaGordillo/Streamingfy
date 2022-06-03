import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPerfiles, Perfiles } from '../perfiles.model';

import { PerfilesService } from './perfiles.service';

describe('Perfiles Service', () => {
  let service: PerfilesService;
  let httpMock: HttpTestingController;
  let elemDefault: IPerfiles;
  let expectedResult: IPerfiles | IPerfiles[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PerfilesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      imagen: 'AAAAAAA',
      nombre: 'AAAAAAA',
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

    it('should create a Perfiles', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Perfiles()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Perfiles', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          imagen: 'BBBBBB',
          nombre: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Perfiles', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
        },
        new Perfiles()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Perfiles', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          imagen: 'BBBBBB',
          nombre: 'BBBBBB',
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

    it('should delete a Perfiles', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPerfilesToCollectionIfMissing', () => {
      it('should add a Perfiles to an empty array', () => {
        const perfiles: IPerfiles = { id: 123 };
        expectedResult = service.addPerfilesToCollectionIfMissing([], perfiles);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(perfiles);
      });

      it('should not add a Perfiles to an array that contains it', () => {
        const perfiles: IPerfiles = { id: 123 };
        const perfilesCollection: IPerfiles[] = [
          {
            ...perfiles,
          },
          { id: 456 },
        ];
        expectedResult = service.addPerfilesToCollectionIfMissing(perfilesCollection, perfiles);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Perfiles to an array that doesn't contain it", () => {
        const perfiles: IPerfiles = { id: 123 };
        const perfilesCollection: IPerfiles[] = [{ id: 456 }];
        expectedResult = service.addPerfilesToCollectionIfMissing(perfilesCollection, perfiles);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(perfiles);
      });

      it('should add only unique Perfiles to an array', () => {
        const perfilesArray: IPerfiles[] = [{ id: 123 }, { id: 456 }, { id: 58272 }];
        const perfilesCollection: IPerfiles[] = [{ id: 123 }];
        expectedResult = service.addPerfilesToCollectionIfMissing(perfilesCollection, ...perfilesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const perfiles: IPerfiles = { id: 123 };
        const perfiles2: IPerfiles = { id: 456 };
        expectedResult = service.addPerfilesToCollectionIfMissing([], perfiles, perfiles2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(perfiles);
        expect(expectedResult).toContain(perfiles2);
      });

      it('should accept null and undefined values', () => {
        const perfiles: IPerfiles = { id: 123 };
        expectedResult = service.addPerfilesToCollectionIfMissing([], null, perfiles, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(perfiles);
      });

      it('should return initial array if no Perfiles is added', () => {
        const perfilesCollection: IPerfiles[] = [{ id: 123 }];
        expectedResult = service.addPerfilesToCollectionIfMissing(perfilesCollection, undefined, null);
        expect(expectedResult).toEqual(perfilesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
