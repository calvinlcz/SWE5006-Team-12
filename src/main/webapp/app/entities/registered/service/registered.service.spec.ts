import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IRegistered } from '../registered.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../registered.test-samples';

import { RegisteredService } from './registered.service';

const requireRestSample: IRegistered = {
  ...sampleWithRequiredData,
};

describe('Registered Service', () => {
  let service: RegisteredService;
  let httpMock: HttpTestingController;
  let expectedResult: IRegistered | IRegistered[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RegisteredService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Registered', () => {
      const registered = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(registered).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Registered', () => {
      const registered = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(registered).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Registered', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Registered', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Registered', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRegisteredToCollectionIfMissing', () => {
      it('should add a Registered to an empty array', () => {
        const registered: IRegistered = sampleWithRequiredData;
        expectedResult = service.addRegisteredToCollectionIfMissing([], registered);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(registered);
      });

      it('should not add a Registered to an array that contains it', () => {
        const registered: IRegistered = sampleWithRequiredData;
        const registeredCollection: IRegistered[] = [
          {
            ...registered,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRegisteredToCollectionIfMissing(registeredCollection, registered);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Registered to an array that doesn't contain it", () => {
        const registered: IRegistered = sampleWithRequiredData;
        const registeredCollection: IRegistered[] = [sampleWithPartialData];
        expectedResult = service.addRegisteredToCollectionIfMissing(registeredCollection, registered);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(registered);
      });

      it('should add only unique Registered to an array', () => {
        const registeredArray: IRegistered[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const registeredCollection: IRegistered[] = [sampleWithRequiredData];
        expectedResult = service.addRegisteredToCollectionIfMissing(registeredCollection, ...registeredArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const registered: IRegistered = sampleWithRequiredData;
        const registered2: IRegistered = sampleWithPartialData;
        expectedResult = service.addRegisteredToCollectionIfMissing([], registered, registered2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(registered);
        expect(expectedResult).toContain(registered2);
      });

      it('should accept null and undefined values', () => {
        const registered: IRegistered = sampleWithRequiredData;
        expectedResult = service.addRegisteredToCollectionIfMissing([], null, registered, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(registered);
      });

      it('should return initial array if no Registered is added', () => {
        const registeredCollection: IRegistered[] = [sampleWithRequiredData];
        expectedResult = service.addRegisteredToCollectionIfMissing(registeredCollection, undefined, null);
        expect(expectedResult).toEqual(registeredCollection);
      });
    });

    describe('compareRegistered', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRegistered(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 22233 };
        const entity2 = null;

        const compareResult1 = service.compareRegistered(entity1, entity2);
        const compareResult2 = service.compareRegistered(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 22233 };
        const entity2 = { id: 12098 };

        const compareResult1 = service.compareRegistered(entity1, entity2);
        const compareResult2 = service.compareRegistered(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 22233 };
        const entity2 = { id: 22233 };

        const compareResult1 = service.compareRegistered(entity1, entity2);
        const compareResult2 = service.compareRegistered(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
