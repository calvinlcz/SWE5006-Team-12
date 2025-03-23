import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../registered.test-samples';

import { RegisteredFormService } from './registered-form.service';

describe('Registered Form Service', () => {
  let service: RegisteredFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RegisteredFormService);
  });

  describe('Service methods', () => {
    describe('createRegisteredFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRegisteredFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accountNo: expect.any(Object),
            linkWorkspace: expect.any(Object),
            workSpaces: expect.any(Object),
          }),
        );
      });

      it('passing IRegistered should create a new form with FormGroup', () => {
        const formGroup = service.createRegisteredFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accountNo: expect.any(Object),
            linkWorkspace: expect.any(Object),
            workSpaces: expect.any(Object),
          }),
        );
      });
    });

    describe('getRegistered', () => {
      it('should return NewRegistered for default Registered initial value', () => {
        const formGroup = service.createRegisteredFormGroup(sampleWithNewData);

        const registered = service.getRegistered(formGroup) as any;

        expect(registered).toMatchObject(sampleWithNewData);
      });

      it('should return NewRegistered for empty Registered initial value', () => {
        const formGroup = service.createRegisteredFormGroup();

        const registered = service.getRegistered(formGroup) as any;

        expect(registered).toMatchObject({});
      });

      it('should return IRegistered', () => {
        const formGroup = service.createRegisteredFormGroup(sampleWithRequiredData);

        const registered = service.getRegistered(formGroup) as any;

        expect(registered).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRegistered should not enable id FormControl', () => {
        const formGroup = service.createRegisteredFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRegistered should disable id FormControl', () => {
        const formGroup = service.createRegisteredFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
