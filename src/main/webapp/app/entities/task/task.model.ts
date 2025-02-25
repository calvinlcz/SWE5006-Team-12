import dayjs from 'dayjs/esm';
import { IWorkSpace } from 'app/entities/work-space/work-space.model';
import { Rule } from 'app/entities/enumerations/rule.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface ITask {
  id: number;
  name?: string | null;
  description?: string | null;
  dueDate?: dayjs.Dayjs | null;
  dateCreated?: dayjs.Dayjs | null;
  createdBy?: string | null;
  dateModified?: dayjs.Dayjs | null;
  modifiedBy?: string | null;
  isRecurring?: boolean | null;
  recurrenceRule?: keyof typeof Rule | null;
  priority?: string | null;
  status?: keyof typeof Status | null;
  workSpace?: IWorkSpace | null;
}

export type NewTask = Omit<ITask, 'id'> & { id: null };
