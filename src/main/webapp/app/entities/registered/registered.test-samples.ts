import { IRegistered, NewRegistered } from './registered.model';

export const sampleWithRequiredData: IRegistered = {
  id: 12544,
};

export const sampleWithPartialData: IRegistered = {
  id: 5528,
  linkWorkspace: 'bitterly granular',
};

export const sampleWithFullData: IRegistered = {
  id: 17594,
  accountNo: 'petty debit',
  linkWorkspace: 'hubris standard around',
};

export const sampleWithNewData: NewRegistered = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
