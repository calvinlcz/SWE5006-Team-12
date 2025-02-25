import dayjs from 'dayjs/esm';
import { ITask } from 'app/entities/task/task.model';

export interface INotification {
  id: number;
  message?: string | null;
  alertTime?: dayjs.Dayjs | null;
  disableNotification?: boolean | null;
  task?: ITask | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };
