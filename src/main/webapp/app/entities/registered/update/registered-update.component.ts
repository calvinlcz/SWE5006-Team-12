import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IWorkSpace } from 'app/entities/work-space/work-space.model';
import { WorkSpaceService } from 'app/entities/work-space/service/work-space.service';
import { IRegistered } from '../registered.model';
import { RegisteredService } from '../service/registered.service';
import { RegisteredFormGroup, RegisteredFormService } from './registered-form.service';

@Component({
  selector: 'jhi-registered-update',
  templateUrl: './registered-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RegisteredUpdateComponent implements OnInit {
  isSaving = false;
  registered: IRegistered | null = null;

  workSpacesSharedCollection: IWorkSpace[] = [];

  protected registeredService = inject(RegisteredService);
  protected registeredFormService = inject(RegisteredFormService);
  protected workSpaceService = inject(WorkSpaceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RegisteredFormGroup = this.registeredFormService.createRegisteredFormGroup();

  compareWorkSpace = (o1: IWorkSpace | null, o2: IWorkSpace | null): boolean => this.workSpaceService.compareWorkSpace(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ registered }) => {
      this.registered = registered;
      if (registered) {
        this.updateForm(registered);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const registered = this.registeredFormService.getRegistered(this.editForm);
    if (registered.id !== null) {
      this.subscribeToSaveResponse(this.registeredService.update(registered));
    } else {
      this.subscribeToSaveResponse(this.registeredService.create(registered));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegistered>>): void {
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

  protected updateForm(registered: IRegistered): void {
    this.registered = registered;
    this.registeredFormService.resetForm(this.editForm, registered);

    this.workSpacesSharedCollection = this.workSpaceService.addWorkSpaceToCollectionIfMissing<IWorkSpace>(
      this.workSpacesSharedCollection,
      ...(registered.workSpaces ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.workSpaceService
      .query()
      .pipe(map((res: HttpResponse<IWorkSpace[]>) => res.body ?? []))
      .pipe(
        map((workSpaces: IWorkSpace[]) =>
          this.workSpaceService.addWorkSpaceToCollectionIfMissing<IWorkSpace>(workSpaces, ...(this.registered?.workSpaces ?? [])),
        ),
      )
      .subscribe((workSpaces: IWorkSpace[]) => (this.workSpacesSharedCollection = workSpaces));
  }
}
