import { IUser } from 'app/entities/user/user.model';
import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { ILista } from 'app/entities/lista/lista.model';

export interface IPerfiles {
  id?: number;
  imagen?: string | null;
  nombre?: string | null;
  user?: IUser | null;
  pelicula?: IPelicula | null;
  listas?: ILista[] | null;
}

export class Perfiles implements IPerfiles {
  constructor(
    public id?: number,
    public imagen?: string | null,
    public nombre?: string | null,
    public user?: IUser | null,
    public pelicula?: IPelicula | null,
    public listas?: ILista[] | null
  ) {}
}

export function getPerfilesIdentifier(perfiles: IPerfiles): number | undefined {
  return perfiles.id;
}
