import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IWorkSpace } from 'app/entities/work-space/work-space.model';
import { WorkSpaceService } from 'app/entities/work-space/service/work-space.service';
import { TaskService } from '../service/task.service';
import { ITask } from '../task.model';
import { TaskFormService } from './task-form.service';

import { TaskUpdateComponent } from './task-update.component';

describe('Task Management Update Component', () => {
  let comp: TaskUpdateComponent;
  let fixture: ComponentFixture<TaskUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let taskFormService: TaskFormService;
  let taskService: TaskService;
  let workSpaceService: WorkSpaceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TaskUpdateComponent],
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
      .overrideTemplate(TaskUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TaskUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    taskFormService = TestBed.inject(TaskFormService);
    taskService = TestBed.inject(TaskService);
    workSpaceService = TestBed.inject(WorkSpaceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call WorkSpace query and add missing value', () => {
      const task: ITask = { id: 22244 };
      const workSpace: IWorkSpace = { id: 3629 };
      task.workSpace = workSpace;

      const workSpaceCollection: IWorkSpace[] = [{ id: 3629 }];
      jest.spyOn(workSpaceService, 'query').mockReturnValue(of(new HttpResponse({ body: workSpaceCollection })));
      const additionalWorkSpaces = [workSpace];
      const expectedCollection: IWorkSpace[] = [...additionalWorkSpaces, ...workSpaceCollection];
      jest.spyOn(workSpaceService, 'addWorkSpaceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ task });
      comp.ngOnInit();

      expect(workSpaceService.query).toHaveBeenCalled();
      expect(workSpaceService.addWorkSpaceToCollectionIfMissing).toHaveBeenCalledWith(
        workSpaceCollection,
        ...additionalWorkSpaces.map(expect.objectContaining),
      );
      expect(comp.workSpacesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const task: ITask = { id: 22244 };
      const workSpace: IWorkSpace = { id: 3629 };
      task.workSpace = workSpace;

      activatedRoute.data = of({ task });
      comp.ngOnInit();

      expect(comp.workSpacesSharedCollection).toContainEqual(workSpace);
      expect(comp.task).toEqual(task);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITask>>();
      const task = { id: 25192 };
      jest.spyOn(taskFormService, 'getTask').mockReturnValue(task);
      jest.spyOn(taskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ task });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: task }));
      saveSubject.complete();

      // THEN
      expect(taskFormService.getTask).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(taskService.update).toHaveBeenCalledWith(expect.objectContaining(task));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITask>>();
      const task = { id: 25192 };
      jest.spyOn(taskFormService, 'getTask').mockReturnValue({ id: null });
      jest.spyOn(taskService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ task: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: task }));
      saveSubject.complete();

      // THEN
      expect(taskFormService.getTask).toHaveBeenCalled();
      expect(taskService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITask>>();
      const task = { id: 25192 };
      jest.spyOn(taskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ task });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(taskService.update).toHaveBeenCalled();
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
