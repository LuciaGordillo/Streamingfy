import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PerfilesService } from '../service/perfiles.service';

import { PerfilesComponent } from './perfiles.component';

describe('Perfiles Management Component', () => {
  let comp: PerfilesComponent;
  let fixture: ComponentFixture<PerfilesComponent>;
  let service: PerfilesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PerfilesComponent],
    })
      .overrideTemplate(PerfilesComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PerfilesComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PerfilesService);

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
    expect(comp.perfiles?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
