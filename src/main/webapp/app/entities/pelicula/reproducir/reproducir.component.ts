import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPelicula } from '../pelicula.model';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'jhi-reproducir',
  templateUrl: './reproducir.component.html',
})
export class ReproducirComponent implements OnInit {
  pelicula: IPelicula | null = null;
  url: any;
  constructor(protected activatedRoute: ActivatedRoute, private _sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pelicula }) => {
      this.pelicula = pelicula;
    });
    this.url = this._sanitizer.bypassSecurityTrustResourceUrl(this.pelicula!.url!);
  }

  previousState(): void {
    window.history.back();
  }
}
