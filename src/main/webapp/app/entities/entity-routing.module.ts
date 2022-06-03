import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pelicula',
        data: { pageTitle: 'streamingfyApp.pelicula.home.title' },
        loadChildren: () => import('./pelicula/pelicula.module').then(m => m.PeliculaModule),
      },
      {
        path: 'lista',
        data: { pageTitle: 'streamingfyApp.lista.home.title' },
        loadChildren: () => import('./lista/lista.module').then(m => m.ListaModule),
      },
      {
        path: 'perfiles',
        data: { pageTitle: 'streamingfyApp.perfiles.home.title' },
        loadChildren: () => import('./perfiles/perfiles.module').then(m => m.PerfilesModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
