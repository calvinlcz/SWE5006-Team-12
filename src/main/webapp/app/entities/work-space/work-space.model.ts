import dayjs from 'dayjs/esm';
import { IRegistered } from 'app/entities/registered/registered.model';

export interface IWorkSpace {
  id: number;
  name?: string | null;
  dateCreated?: dayjs.Dayjs | null;
  createdBy?: string | null;
  dateModified?: dayjs.Dayjs | null;
  modifiedBy?: string | null;
  sharedWorkspace?: boolean | null;
  registereds?: IRegistered[] | null;
}

export type NewWorkSpace = Omit<IWorkSpace, 'id'> & { id: null };
