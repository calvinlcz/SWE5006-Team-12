import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RegisteredDetailComponent } from './registered-detail.component';

describe('Registered Management Detail Component', () => {
  let comp: RegisteredDetailComponent;
  let fixture: ComponentFixture<RegisteredDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisteredDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./registered-detail.component').then(m => m.RegisteredDetailComponent),
              resolve: { registered: () => of({ id: 22233 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RegisteredDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisteredDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load registered on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RegisteredDetailComponent);

      // THEN
      expect(instance.registered()).toEqual(expect.objectContaining({ id: 22233 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
