import dayjs from 'dayjs/esm';

import { IWorkSpace, NewWorkSpace } from './work-space.model';

export const sampleWithRequiredData: IWorkSpace = {
  id: 1639,
  name: 'recklessly',
  dateCreated: dayjs('2025-02-20'),
  createdBy: 'rigidly microchip especially',
  dateModified: dayjs('2025-02-20'),
  modifiedBy: 'now unaccountably caring',
};

export const sampleWithPartialData: IWorkSpace = {
  id: 29756,
  name: 'rebuke information',
  dateCreated: dayjs('2025-02-21'),
  createdBy: 'what mob alive',
  dateModified: dayjs('2025-02-20'),
  modifiedBy: 'complication',
  sharedWorkspace: true,
};

export const sampleWithFullData: IWorkSpace = {
  id: 24220,
  name: 'curry whether around',
  dateCreated: dayjs('2025-02-20'),
  createdBy: 'athwart',
  dateModified: dayjs('2025-02-20'),
  modifiedBy: 'clamp',
  sharedWorkspace: false,
};

export const sampleWithNewData: NewWorkSpace = {
  name: 'well-worn savour',
  dateCreated: dayjs('2025-02-20'),
  createdBy: 'as scuffle',
  dateModified: dayjs('2025-02-20'),
  modifiedBy: 'jaunty besides swathe',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
