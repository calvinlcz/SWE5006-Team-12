import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IRegistered, NewRegistered } from '../registered.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRegistered for edit and NewRegisteredFormGroupInput for create.
 */
type RegisteredFormGroupInput = IRegistered | PartialWithRequiredKeyOf<NewRegistered>;

type RegisteredFormDefaults = Pick<NewRegistered, 'id' | 'workSpaces'>;

type RegisteredFormGroupContent = {
  id: FormControl<IRegistered['id'] | NewRegistered['id']>;
  accountNo: FormControl<IRegistered['accountNo']>;
  linkWorkspace: FormControl<IRegistered['linkWorkspace']>;
  workSpaces: FormControl<IRegistered['workSpaces']>;
};

export type RegisteredFormGroup = FormGroup<RegisteredFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RegisteredFormService {
  createRegisteredFormGroup(registered: RegisteredFormGroupInput = { id: null }): RegisteredFormGroup {
    const registeredRawValue = {
      ...this.getFormDefaults(),
      ...registered,
    };
    return new FormGroup<RegisteredFormGroupContent>({
      id: new FormControl(
        { value: registeredRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      accountNo: new FormControl(registeredRawValue.accountNo),
      linkWorkspace: new FormControl(registeredRawValue.linkWorkspace),
      workSpaces: new FormControl(registeredRawValue.workSpaces ?? []),
    });
  }

  getRegistered(form: RegisteredFormGroup): IRegistered | NewRegistered {
    return form.getRawValue() as IRegistered | NewRegistered;
  }

  resetForm(form: RegisteredFormGroup, registered: RegisteredFormGroupInput): void {
    const registeredRawValue = { ...this.getFormDefaults(), ...registered };
    form.reset(
      {
        ...registeredRawValue,
        id: { value: registeredRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RegisteredFormDefaults {
    return {
      id: null,
      workSpaces: [],
    };
  }
}
