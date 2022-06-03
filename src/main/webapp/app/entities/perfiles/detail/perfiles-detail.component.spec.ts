import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PerfilesDetailComponent } from './perfiles-detail.component';

describe('Perfiles Management Detail Component', () => {
  let comp: PerfilesDetailComponent;
  let fixture: ComponentFixture<PerfilesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PerfilesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ perfiles: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PerfilesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PerfilesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load perfiles on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.perfiles).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
