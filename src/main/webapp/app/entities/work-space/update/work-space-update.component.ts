import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRegistered } from 'app/entities/registered/registered.model';
import { RegisteredService } from 'app/entities/registered/service/registered.service';
import { IWorkSpace } from '../work-space.model';
import { WorkSpaceService } from '../service/work-space.service';
import { WorkSpaceFormGroup, WorkSpaceFormService } from './work-space-form.service';

@Component({
  selector: 'jhi-work-space-update',
  templateUrl: './work-space-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WorkSpaceUpdateComponent implements OnInit {
  isSaving = false;
  workSpace: IWorkSpace | null = null;

  registeredsSharedCollection: IRegistered[] = [];

  protected workSpaceService = inject(WorkSpaceService);
  protected workSpaceFormService = inject(WorkSpaceFormService);
  protected registeredService = inject(RegisteredService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WorkSpaceFormGroup = this.workSpaceFormService.createWorkSpaceFormGroup();

  compareRegistered = (o1: IRegistered | null, o2: IRegistered | null): boolean => this.registeredService.compareRegistered(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workSpace }) => {
      this.workSpace = workSpace;
      if (workSpace) {
        this.updateForm(workSpace);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workSpace = this.workSpaceFormService.getWorkSpace(this.editForm);
    if (workSpace.id !== null) {
      this.subscribeToSaveResponse(this.workSpaceService.update(workSpace));
    } else {
      this.subscribeToSaveResponse(this.workSpaceService.create(workSpace));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkSpace>>): void {
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

  protected updateForm(workSpace: IWorkSpace): void {
    this.workSpace = workSpace;
    this.workSpaceFormService.resetForm(this.editForm, workSpace);

    this.registeredsSharedCollection = this.registeredService.addRegisteredToCollectionIfMissing<IRegistered>(
      this.registeredsSharedCollection,
      ...(workSpace.registereds ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.registeredService
      .query()
      .pipe(map((res: HttpResponse<IRegistered[]>) => res.body ?? []))
      .pipe(
        map((registereds: IRegistered[]) =>
          this.registeredService.addRegisteredToCollectionIfMissing<IRegistered>(registereds, ...(this.workSpace?.registereds ?? [])),
        ),
      )
      .subscribe((registereds: IRegistered[]) => (this.registeredsSharedCollection = registereds));
  }
}
