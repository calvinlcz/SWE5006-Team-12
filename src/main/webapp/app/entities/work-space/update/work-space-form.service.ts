import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IWorkSpace, NewWorkSpace } from '../work-space.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorkSpace for edit and NewWorkSpaceFormGroupInput for create.
 */
type WorkSpaceFormGroupInput = IWorkSpace | PartialWithRequiredKeyOf<NewWorkSpace>;

type WorkSpaceFormDefaults = Pick<NewWorkSpace, 'id' | 'sharedWorkspace' | 'registereds'>;

type WorkSpaceFormGroupContent = {
  id: FormControl<IWorkSpace['id'] | NewWorkSpace['id']>;
  name: FormControl<IWorkSpace['name']>;
  dateCreated: FormControl<IWorkSpace['dateCreated']>;
  createdBy: FormControl<IWorkSpace['createdBy']>;
  dateModified: FormControl<IWorkSpace['dateModified']>;
  modifiedBy: FormControl<IWorkSpace['modifiedBy']>;
  sharedWorkspace: FormControl<IWorkSpace['sharedWorkspace']>;
  registereds: FormControl<IWorkSpace['registereds']>;
};

export type WorkSpaceFormGroup = FormGroup<WorkSpaceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkSpaceFormService {
  createWorkSpaceFormGroup(workSpace: WorkSpaceFormGroupInput = { id: null }): WorkSpaceFormGroup {
    const workSpaceRawValue = {
      ...this.getFormDefaults(),
      ...workSpace,
    };
    return new FormGroup<WorkSpaceFormGroupContent>({
      id: new FormControl(
        { value: workSpaceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(workSpaceRawValue.name, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      dateCreated: new FormControl(workSpaceRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(workSpaceRawValue.createdBy, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(workSpaceRawValue.dateModified, {
        validators: [Validators.required],
      }),
      modifiedBy: new FormControl(workSpaceRawValue.modifiedBy, {
        validators: [Validators.required],
      }),
      sharedWorkspace: new FormControl(workSpaceRawValue.sharedWorkspace),
      registereds: new FormControl(workSpaceRawValue.registereds ?? []),
    });
  }

  getWorkSpace(form: WorkSpaceFormGroup): IWorkSpace | NewWorkSpace {
    return form.getRawValue() as IWorkSpace | NewWorkSpace;
  }

  resetForm(form: WorkSpaceFormGroup, workSpace: WorkSpaceFormGroupInput): void {
    const workSpaceRawValue = { ...this.getFormDefaults(), ...workSpace };
    form.reset(
      {
        ...workSpaceRawValue,
        id: { value: workSpaceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WorkSpaceFormDefaults {
    return {
      id: null,
      sharedWorkspace: false,
      registereds: [],
    };
  }
}
