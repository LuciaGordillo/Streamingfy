import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ListaDetailComponent } from './lista-detail.component';

describe('Lista Management Detail Component', () => {
  let comp: ListaDetailComponent;
  let fixture: ComponentFixture<ListaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ lista: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ListaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ListaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load lista on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.lista).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
