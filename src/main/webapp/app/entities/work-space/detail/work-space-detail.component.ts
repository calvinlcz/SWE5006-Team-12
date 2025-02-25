import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IWorkSpace } from '../work-space.model';

@Component({
  selector: 'jhi-work-space-detail',
  templateUrl: './work-space-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class WorkSpaceDetailComponent {
  workSpace = input<IWorkSpace | null>(null);

  previousState(): void {
    window.history.back();
  }
}
