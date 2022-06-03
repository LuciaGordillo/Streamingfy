import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPerfiles } from '../perfiles.model';

@Component({
  selector: 'jhi-perfiles-detail',
  templateUrl: './perfiles-detail.component.html',
})
export class PerfilesDetailComponent implements OnInit {
  perfiles: IPerfiles | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ perfiles }) => {
      this.perfiles = perfiles;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
