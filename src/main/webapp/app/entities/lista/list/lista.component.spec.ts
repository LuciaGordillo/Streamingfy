import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ListaService } from '../service/lista.service';

import { ListaComponent } from './lista.component';

describe('Lista Management Component', () => {
  let comp: ListaComponent;
  let fixture: ComponentFixture<ListaComponent>;
  let service: ListaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ListaComponent],
    })
      .overrideTemplate(ListaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ListaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ListaService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.listas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
