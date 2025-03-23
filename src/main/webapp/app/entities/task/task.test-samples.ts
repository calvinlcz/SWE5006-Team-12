import dayjs from 'dayjs/esm';

import { ITask, NewTask } from './task.model';

export const sampleWithRequiredData: ITask = {
  id: 9181,
  name: 'or consequently',
  dateCreated: dayjs('2025-02-20'),
  createdBy: 'unnaturally amongst supposing',
  dateModified: dayjs('2025-02-20'),
  modifiedBy: 'immense wide-eyed',
  status: 'Pending',
};

export const sampleWithPartialData: ITask = {
  id: 2746,
  name: 'past personalise croon',
  description: 'but',
  dateCreated: dayjs('2025-02-20'),
  createdBy: 'ill-fated',
  dateModified: dayjs('2025-02-21'),
  modifiedBy: 'devise',
  recurrenceRule: 'Weekly',
  priority: 'midwife',
  status: 'Pending',
};

export const sampleWithFullData: ITask = {
  id: 13396,
  name: 'where wherever',
  description: 'but than',
  dueDate: dayjs('2025-02-21'),
  dateCreated: dayjs('2025-02-20'),
  createdBy: 'solder frankly',
  dateModified: dayjs('2025-02-20'),
  modifiedBy: 'sweatshop',
  isRecurring: true,
  recurrenceRule: 'Monthly',
  priority: 'pish',
  status: 'Paused',
};

export const sampleWithNewData: NewTask = {
  name: 'expostulate',
  dateCreated: dayjs('2025-02-20'),
  createdBy: 'seemingly between',
  dateModified: dayjs('2025-02-20'),
  modifiedBy: 'psst',
  status: 'Inprogress',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
