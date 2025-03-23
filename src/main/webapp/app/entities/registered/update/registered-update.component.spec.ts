import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IWorkSpace } from 'app/entities/work-space/work-space.model';
import { WorkSpaceService } from 'app/entities/work-space/service/work-space.service';
import { RegisteredService } from '../service/registered.service';
import { IRegistered } from '../registered.model';
import { RegisteredFormService } from './registered-form.service';

import { RegisteredUpdateComponent } from './registered-update.component';

describe('Registered Management Update Component', () => {
  let comp: RegisteredUpdateComponent;
  let fixture: ComponentFixture<RegisteredUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let registeredFormService: RegisteredFormService;
  let registeredService: RegisteredService;
  let workSpaceService: WorkSpaceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RegisteredUpdateComponent],
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
      .overrideTemplate(RegisteredUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RegisteredUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    registeredFormService = TestBed.inject(RegisteredFormService);
    registeredService = TestBed.inject(RegisteredService);
    workSpaceService = TestBed.inject(WorkSpaceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call WorkSpace query and add missing value', () => {
      const registered: IRegistered = { id: 12098 };
      const workSpaces: IWorkSpace[] = [{ id: 3629 }];
      registered.workSpaces = workSpaces;

      const workSpaceCollection: IWorkSpace[] = [{ id: 3629 }];
      jest.spyOn(workSpaceService, 'query').mockReturnValue(of(new HttpResponse({ body: workSpaceCollection })));
      const additionalWorkSpaces = [...workSpaces];
      const expectedCollection: IWorkSpace[] = [...additionalWorkSpaces, ...workSpaceCollection];
      jest.spyOn(workSpaceService, 'addWorkSpaceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ registered });
      comp.ngOnInit();

      expect(workSpaceService.query).toHaveBeenCalled();
      expect(workSpaceService.addWorkSpaceToCollectionIfMissing).toHaveBeenCalledWith(
        workSpaceCollection,
        ...additionalWorkSpaces.map(expect.objectContaining),
      );
      expect(comp.workSpacesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const registered: IRegistered = { id: 12098 };
      const workSpace: IWorkSpace = { id: 3629 };
      registered.workSpaces = [workSpace];

      activatedRoute.data = of({ registered });
      comp.ngOnInit();

      expect(comp.workSpacesSharedCollection).toContainEqual(workSpace);
      expect(comp.registered).toEqual(registered);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRegistered>>();
      const registered = { id: 22233 };
      jest.spyOn(registeredFormService, 'getRegistered').mockReturnValue(registered);
      jest.spyOn(registeredService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registered });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: registered }));
      saveSubject.complete();

      // THEN
      expect(registeredFormService.getRegistered).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(registeredService.update).toHaveBeenCalledWith(expect.objectContaining(registered));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRegistered>>();
      const registered = { id: 22233 };
      jest.spyOn(registeredFormService, 'getRegistered').mockReturnValue({ id: null });
      jest.spyOn(registeredService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registered: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: registered }));
      saveSubject.complete();

      // THEN
      expect(registeredFormService.getRegistered).toHaveBeenCalled();
      expect(registeredService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRegistered>>();
      const registered = { id: 22233 };
      jest.spyOn(registeredService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registered });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(registeredService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareWorkSpace', () => {
      it('Should forward to workSpaceService', () => {
        const entity = { id: 3629 };
        const entity2 = { id: 28284 };
        jest.spyOn(workSpaceService, 'compareWorkSpace');
        comp.compareWorkSpace(entity, entity2);
        expect(workSpaceService.compareWorkSpace).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
