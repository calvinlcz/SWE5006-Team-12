import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 10110,
  message: 'reclassify makeover',
  alertTime: dayjs('2025-02-20'),
};

export const sampleWithPartialData: INotification = {
  id: 24804,
  message: 'ha',
  alertTime: dayjs('2025-02-20'),
};

export const sampleWithFullData: INotification = {
  id: 5787,
  message: 'some extra-large',
  alertTime: dayjs('2025-02-20'),
  disableNotification: true,
};

export const sampleWithNewData: NewNotification = {
  message: 'consequently voluntarily ew',
  alertTime: dayjs('2025-02-21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
