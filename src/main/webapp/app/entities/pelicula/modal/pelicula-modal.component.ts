import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPelicula } from '../pelicula.model';
import { PeliculaService } from '../service/pelicula.service';

import { DomSanitizer } from '@angular/platform-browser';
@Component({
  templateUrl: './pelicula-modal.component.html',
})
export class PeliculaModalComponent implements OnInit {
  url: any;
  pelicula?: IPelicula;
  constructor(protected peliculaService: PeliculaService, protected activeModal: NgbActiveModal, private _sanitizer: DomSanitizer) {}
  ngOnInit(): void {
    this.url = this._sanitizer.bypassSecurityTrustResourceUrl(this.pelicula!.url!);
  }

  cancel(): void {
    this.activeModal.dismiss();
  }
}
