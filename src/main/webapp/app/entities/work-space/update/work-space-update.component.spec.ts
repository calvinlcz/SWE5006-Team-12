import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IRegistered } from 'app/entities/registered/registered.model';
import { RegisteredService } from 'app/entities/registered/service/registered.service';
import { WorkSpaceService } from '../service/work-space.service';
import { IWorkSpace } from '../work-space.model';
import { WorkSpaceFormService } from './work-space-form.service';

import { WorkSpaceUpdateComponent } from './work-space-update.component';

describe('WorkSpace Management Update Component', () => {
  let comp: WorkSpaceUpdateComponent;
  let fixture: ComponentFixture<WorkSpaceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workSpaceFormService: WorkSpaceFormService;
  let workSpaceService: WorkSpaceService;
  let registeredService: RegisteredService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WorkSpaceUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(WorkSpaceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkSpaceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workSpaceFormService = TestBed.inject(WorkSpaceFormService);
    workSpaceService = TestBed.inject(WorkSpaceService);
    registeredService = TestBed.inject(RegisteredService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Registered query and add missing value', () => {
      const workSpace: IWorkSpace = { id: 28284 };
      const registereds: IRegistered[] = [{ id: 22233 }];
      workSpace.registereds = registereds;

      const registeredCollection: IRegistered[] = [{ id: 22233 }];
      jest.spyOn(registeredService, 'query').mockReturnValue(of(new HttpResponse({ body: registeredCollection })));
      const additionalRegistereds = [...registereds];
      const expectedCollection: IRegistered[] = [...additionalRegistereds, ...registeredCollection];
      jest.spyOn(registeredService, 'addRegisteredToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workSpace });
      comp.ngOnInit();

      expect(registeredService.query).toHaveBeenCalled();
      expect(registeredService.addRegisteredToCollectionIfMissing).toHaveBeenCalledWith(
        registeredCollection,
        ...additionalRegistereds.map(expect.objectContaining),
      );
      expect(comp.registeredsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const workSpace: IWorkSpace = { id: 28284 };
      const registered: IRegistered = { id: 22233 };
      workSpace.registereds = [registered];

      activatedRoute.data = of({ workSpace });
      comp.ngOnInit();

      expect(comp.registeredsSharedCollection).toContainEqual(registered);
      expect(comp.workSpace).toEqual(workSpace);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkSpace>>();
      const workSpace = { id: 3629 };
      jest.spyOn(workSpaceFormService, 'getWorkSpace').mockReturnValue(workSpace);
      jest.spyOn(workSpaceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workSpace });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workSpace }));
      saveSubject.complete();

      // THEN
      expect(workSpaceFormService.getWorkSpace).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workSpaceService.update).toHaveBeenCalledWith(expect.objectContaining(workSpace));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkSpace>>();
      const workSpace = { id: 3629 };
      jest.spyOn(workSpaceFormService, 'getWorkSpace').mockReturnValue({ id: null });
      jest.spyOn(workSpaceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workSpace: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workSpace }));
      saveSubject.complete();

      // THEN
      expect(workSpaceFormService.getWorkSpace).toHaveBeenCalled();
      expect(workSpaceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkSpace>>();
      const workSpace = { id: 3629 };
      jest.spyOn(workSpaceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workSpace });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workSpaceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRegistered', () => {
      it('Should forward to registeredService', () => {
        const entity = { id: 22233 };
        const entity2 = { id: 12098 };
        jest.spyOn(registeredService, 'compareRegistered');
        comp.compareRegistered(entity, entity2);
        expect(registeredService.compareRegistered).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
