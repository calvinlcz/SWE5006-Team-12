import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITask, NewTask } from '../task.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITask for edit and NewTaskFormGroupInput for create.
 */
type TaskFormGroupInput = ITask | PartialWithRequiredKeyOf<NewTask>;

type TaskFormDefaults = Pick<NewTask, 'id' | 'isRecurring'>;

type TaskFormGroupContent = {
  id: FormControl<ITask['id'] | NewTask['id']>;
  name: FormControl<ITask['name']>;
  description: FormControl<ITask['description']>;
  dueDate: FormControl<ITask['dueDate']>;
  dateCreated: FormControl<ITask['dateCreated']>;
  createdBy: FormControl<ITask['createdBy']>;
  dateModified: FormControl<ITask['dateModified']>;
  modifiedBy: FormControl<ITask['modifiedBy']>;
  isRecurring: FormControl<ITask['isRecurring']>;
  recurrenceRule: FormControl<ITask['recurrenceRule']>;
  priority: FormControl<ITask['priority']>;
  status: FormControl<ITask['status']>;
  workSpace: FormControl<ITask['workSpace']>;
};

export type TaskFormGroup = FormGroup<TaskFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TaskFormService {
  createTaskFormGroup(task: TaskFormGroupInput = { id: null }): TaskFormGroup {
    const taskRawValue = {
      ...this.getFormDefaults(),
      ...task,
    };
    return new FormGroup<TaskFormGroupContent>({
      id: new FormControl(
        { value: taskRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(taskRawValue.name, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      description: new FormControl(taskRawValue.description),
      dueDate: new FormControl(taskRawValue.dueDate),
      dateCreated: new FormControl(taskRawValue.dateCreated, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(taskRawValue.createdBy, {
        validators: [Validators.required],
      }),
      dateModified: new FormControl(taskRawValue.dateModified, {
        validators: [Validators.required],
      }),
      modifiedBy: new FormControl(taskRawValue.modifiedBy, {
        validators: [Validators.required],
      }),
      isRecurring: new FormControl(taskRawValue.isRecurring),
      recurrenceRule: new FormControl(taskRawValue.recurrenceRule),
      priority: new FormControl(taskRawValue.priority),
      status: new FormControl(taskRawValue.status, {
        validators: [Validators.required],
      }),
      workSpace: new FormControl(taskRawValue.workSpace),
    });
  }

  getTask(form: TaskFormGroup): ITask | NewTask {
    return form.getRawValue() as ITask | NewTask;
  }

  resetForm(form: TaskFormGroup, task: TaskFormGroupInput): void {
    const taskRawValue = { ...this.getFormDefaults(), ...task };
    form.reset(
      {
        ...taskRawValue,
        id: { value: taskRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TaskFormDefaults {
    return {
      id: null,
      isRecurring: false,
    };
  }
}
