import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../work-space.test-samples';

import { WorkSpaceFormService } from './work-space-form.service';

describe('WorkSpace Form Service', () => {
  let service: WorkSpaceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkSpaceFormService);
  });

  describe('Service methods', () => {
    describe('createWorkSpaceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWorkSpaceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            dateCreated: expect.any(Object),
            createdBy: expect.any(Object),
            dateModified: expect.any(Object),
            modifiedBy: expect.any(Object),
            sharedWorkspace: expect.any(Object),
            registereds: expect.any(Object),
          }),
        );
      });

      it('passing IWorkSpace should create a new form with FormGroup', () => {
        const formGroup = service.createWorkSpaceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            dateCreated: expect.any(Object),
            createdBy: expect.any(Object),
            dateModified: expect.any(Object),
            modifiedBy: expect.any(Object),
            sharedWorkspace: expect.any(Object),
            registereds: expect.any(Object),
          }),
        );
      });
    });

    describe('getWorkSpace', () => {
      it('should return NewWorkSpace for default WorkSpace initial value', () => {
        const formGroup = service.createWorkSpaceFormGroup(sampleWithNewData);

        const workSpace = service.getWorkSpace(formGroup) as any;

        expect(workSpace).toMatchObject(sampleWithNewData);
      });

      it('should return NewWorkSpace for empty WorkSpace initial value', () => {
        const formGroup = service.createWorkSpaceFormGroup();

        const workSpace = service.getWorkSpace(formGroup) as any;

        expect(workSpace).toMatchObject({});
      });

      it('should return IWorkSpace', () => {
        const formGroup = service.createWorkSpaceFormGroup(sampleWithRequiredData);

        const workSpace = service.getWorkSpace(formGroup) as any;

        expect(workSpace).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWorkSpace should not enable id FormControl', () => {
        const formGroup = service.createWorkSpaceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWorkSpace should disable id FormControl', () => {
        const formGroup = service.createWorkSpaceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
