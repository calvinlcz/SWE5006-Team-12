import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWorkSpace } from '../work-space.model';
import { WorkSpaceService } from '../service/work-space.service';

@Component({
  templateUrl: './work-space-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WorkSpaceDeleteDialogComponent {
  workSpace?: IWorkSpace;

  protected workSpaceService = inject(WorkSpaceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workSpaceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
