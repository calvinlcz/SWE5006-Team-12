import { IWorkSpace } from 'app/entities/work-space/work-space.model';

export interface IRegistered {
  id: number;
  accountNo?: string | null;
  linkWorkspace?: string | null;
  workSpaces?: IWorkSpace[] | null;
}

export type NewRegistered = Omit<IRegistered, 'id'> & { id: null };
