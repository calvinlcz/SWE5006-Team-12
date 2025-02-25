import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IWorkSpace } from 'app/entities/work-space/work-space.model';
import { WorkSpaceService } from 'app/entities/work-space/service/work-space.service';
import { Rule } from 'app/entities/enumerations/rule.model';
import { Status } from 'app/entities/enumerations/status.model';
import { TaskService } from '../service/task.service';
import { ITask } from '../task.model';
import { TaskFormGroup, TaskFormService } from './task-form.service';

@Component({
  selector: 'jhi-task-update',
  templateUrl: './task-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TaskUpdateComponent implements OnInit {
  isSaving = false;
  task: ITask | null = null;
  ruleValues = Object.keys(Rule);
  statusValues = Object.keys(Status);

  workSpacesSharedCollection: IWorkSpace[] = [];

  protected taskService = inject(TaskService);
  protected taskFormService = inject(TaskFormService);
  protected workSpaceService = inject(WorkSpaceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TaskFormGroup = this.taskFormService.createTaskFormGroup();

  compareWorkSpace = (o1: IWorkSpace | null, o2: IWorkSpace | null): boolean => this.workSpaceService.compareWorkSpace(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ task }) => {
      this.task = task;
      if (task) {
        this.updateForm(task);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const task = this.taskFormService.getTask(this.editForm);
    if (task.id !== null) {
      this.subscribeToSaveResponse(this.taskService.update(task));
    } else {
      this.subscribeToSaveResponse(this.taskService.create(task));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITask>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(task: ITask): void {
    this.task = task;
    this.taskFormService.resetForm(this.editForm, task);

    this.workSpacesSharedCollection = this.workSpaceService.addWorkSpaceToCollectionIfMissing<IWorkSpace>(
      this.workSpacesSharedCollection,
      task.workSpace,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.workSpaceService
      .query()
      .pipe(map((res: HttpResponse<IWorkSpace[]>) => res.body ?? []))
      .pipe(
        map((workSpaces: IWorkSpace[]) =>
          this.workSpaceService.addWorkSpaceToCollectionIfMissing<IWorkSpace>(workSpaces, this.task?.workSpace),
        ),
      )
      .subscribe((workSpaces: IWorkSpace[]) => (this.workSpacesSharedCollection = workSpaces));
  }
}
