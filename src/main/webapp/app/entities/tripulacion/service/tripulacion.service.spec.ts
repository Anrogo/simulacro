import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITripulacion, Tripulacion } from '../tripulacion.model';

import { TripulacionService } from './tripulacion.service';

describe('Tripulacion Service', () => {
  let service: TripulacionService;
  let httpMock: HttpTestingController;
  let elemDefault: ITripulacion;
  let expectedResult: ITripulacion | ITripulacion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TripulacionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      dni: 'AAAAAAA',
      direccion: 'AAAAAAA',
      email: 'AAAAAAA',
      fotoContentType: 'image/png',
      foto: 'AAAAAAA',
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

    it('should create a Tripulacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Tripulacion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tripulacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          dni: 'BBBBBB',
          direccion: 'BBBBBB',
          email: 'BBBBBB',
          foto: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tripulacion', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          foto: 'BBBBBB',
        },
        new Tripulacion()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tripulacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          dni: 'BBBBBB',
          direccion: 'BBBBBB',
          email: 'BBBBBB',
          foto: 'BBBBBB',
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

    it('should delete a Tripulacion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTripulacionToCollectionIfMissing', () => {
      it('should add a Tripulacion to an empty array', () => {
        const tripulacion: ITripulacion = { id: 123 };
        expectedResult = service.addTripulacionToCollectionIfMissing([], tripulacion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tripulacion);
      });

      it('should not add a Tripulacion to an array that contains it', () => {
        const tripulacion: ITripulacion = { id: 123 };
        const tripulacionCollection: ITripulacion[] = [
          {
            ...tripulacion,
          },
          { id: 456 },
        ];
        expectedResult = service.addTripulacionToCollectionIfMissing(tripulacionCollection, tripulacion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tripulacion to an array that doesn't contain it", () => {
        const tripulacion: ITripulacion = { id: 123 };
        const tripulacionCollection: ITripulacion[] = [{ id: 456 }];
        expectedResult = service.addTripulacionToCollectionIfMissing(tripulacionCollection, tripulacion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tripulacion);
      });

      it('should add only unique Tripulacion to an array', () => {
        const tripulacionArray: ITripulacion[] = [{ id: 123 }, { id: 456 }, { id: 76664 }];
        const tripulacionCollection: ITripulacion[] = [{ id: 123 }];
        expectedResult = service.addTripulacionToCollectionIfMissing(tripulacionCollection, ...tripulacionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tripulacion: ITripulacion = { id: 123 };
        const tripulacion2: ITripulacion = { id: 456 };
        expectedResult = service.addTripulacionToCollectionIfMissing([], tripulacion, tripulacion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tripulacion);
        expect(expectedResult).toContain(tripulacion2);
      });

      it('should accept null and undefined values', () => {
        const tripulacion: ITripulacion = { id: 123 };
        expectedResult = service.addTripulacionToCollectionIfMissing([], null, tripulacion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tripulacion);
      });

      it('should return initial array if no Tripulacion is added', () => {
        const tripulacionCollection: ITripulacion[] = [{ id: 123 }];
        expectedResult = service.addTripulacionToCollectionIfMissing(tripulacionCollection, undefined, null);
        expect(expectedResult).toEqual(tripulacionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
