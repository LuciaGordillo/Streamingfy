import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPerfiles } from '../perfiles.model';
import { PerfilesService } from '../service/perfiles.service';
import { PerfilesDeleteDialogComponent } from '../delete/perfiles-delete-dialog.component';

@Component({
  selector: 'jhi-perfiles',
  templateUrl: './perfiles.component.html',
})
export class PerfilesComponent implements OnInit {
  perfiles?: IPerfiles[];
  isLoading = false;

  constructor(protected perfilesService: PerfilesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.perfilesService.query().subscribe({
      next: (res: HttpResponse<IPerfiles[]>) => {
        this.isLoading = false;
        this.perfiles = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPerfiles): number {
    return item.id!;
  }

  delete(perfiles: IPerfiles): void {
    const modalRef = this.modalService.open(PerfilesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.perfiles = perfiles;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
