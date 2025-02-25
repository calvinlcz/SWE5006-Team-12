import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRegistered } from '../registered.model';
import { RegisteredService } from '../service/registered.service';

@Component({
  templateUrl: './registered-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RegisteredDeleteDialogComponent {
  registered?: IRegistered;

  protected registeredService = inject(RegisteredService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.registeredService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
