import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IWorkSpace } from '../work-space.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../work-space.test-samples';

import { RestWorkSpace, WorkSpaceService } from './work-space.service';

const requireRestSample: RestWorkSpace = {
  ...sampleWithRequiredData,
  dateCreated: sampleWithRequiredData.dateCreated?.format(DATE_FORMAT),
  dateModified: sampleWithRequiredData.dateModified?.format(DATE_FORMAT),
};

describe('WorkSpace Service', () => {
  let service: WorkSpaceService;
  let httpMock: HttpTestingController;
  let expectedResult: IWorkSpace | IWorkSpace[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(WorkSpaceService);
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

    it('should create a WorkSpace', () => {
      const workSpace = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(workSpace).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorkSpace', () => {
      const workSpace = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(workSpace).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorkSpace', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorkSpace', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WorkSpace', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWorkSpaceToCollectionIfMissing', () => {
      it('should add a WorkSpace to an empty array', () => {
        const workSpace: IWorkSpace = sampleWithRequiredData;
        expectedResult = service.addWorkSpaceToCollectionIfMissing([], workSpace);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workSpace);
      });

      it('should not add a WorkSpace to an array that contains it', () => {
        const workSpace: IWorkSpace = sampleWithRequiredData;
        const workSpaceCollection: IWorkSpace[] = [
          {
            ...workSpace,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWorkSpaceToCollectionIfMissing(workSpaceCollection, workSpace);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkSpace to an array that doesn't contain it", () => {
        const workSpace: IWorkSpace = sampleWithRequiredData;
        const workSpaceCollection: IWorkSpace[] = [sampleWithPartialData];
        expectedResult = service.addWorkSpaceToCollectionIfMissing(workSpaceCollection, workSpace);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workSpace);
      });

      it('should add only unique WorkSpace to an array', () => {
        const workSpaceArray: IWorkSpace[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const workSpaceCollection: IWorkSpace[] = [sampleWithRequiredData];
        expectedResult = service.addWorkSpaceToCollectionIfMissing(workSpaceCollection, ...workSpaceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workSpace: IWorkSpace = sampleWithRequiredData;
        const workSpace2: IWorkSpace = sampleWithPartialData;
        expectedResult = service.addWorkSpaceToCollectionIfMissing([], workSpace, workSpace2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workSpace);
        expect(expectedResult).toContain(workSpace2);
      });

      it('should accept null and undefined values', () => {
        const workSpace: IWorkSpace = sampleWithRequiredData;
        expectedResult = service.addWorkSpaceToCollectionIfMissing([], null, workSpace, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workSpace);
      });

      it('should return initial array if no WorkSpace is added', () => {
        const workSpaceCollection: IWorkSpace[] = [sampleWithRequiredData];
        expectedResult = service.addWorkSpaceToCollectionIfMissing(workSpaceCollection, undefined, null);
        expect(expectedResult).toEqual(workSpaceCollection);
      });
    });

    describe('compareWorkSpace', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWorkSpace(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 3629 };
        const entity2 = null;

        const compareResult1 = service.compareWorkSpace(entity1, entity2);
        const compareResult2 = service.compareWorkSpace(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 3629 };
        const entity2 = { id: 28284 };

        const compareResult1 = service.compareWorkSpace(entity1, entity2);
        const compareResult2 = service.compareWorkSpace(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 3629 };
        const entity2 = { id: 3629 };

        const compareResult1 = service.compareWorkSpace(entity1, entity2);
        const compareResult2 = service.compareWorkSpace(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
