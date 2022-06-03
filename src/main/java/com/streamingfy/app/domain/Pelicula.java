package com.streamingfy.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pelicula.
 */
@Entity
@Table(name = "pelicula")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pelicula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "genero")
    private String genero;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "calificacion")
    private Integer calificacion;

    @Column(name = "estreno")
    private ZonedDateTime estreno;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "url")
    private String url;

    @OneToMany(mappedBy = "pelicula")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "pelicula", "listas" }, allowSetters = true)
    private Set<Perfiles> perfiles = new HashSet<>();

    @OneToMany(mappedBy = "pelicula")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pelicula", "perfiles" }, allowSetters = true)
    private Set<Lista> listas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pelicula id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Pelicula titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return this.genero;
    }

    public Pelicula genero(String genero) {
        this.setGenero(genero);
        return this;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Pelicula descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCalificacion() {
        return this.calificacion;
    }

    public Pelicula calificacion(Integer calificacion) {
        this.setCalificacion(calificacion);
        return this;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public ZonedDateTime getEstreno() {
        return this.estreno;
    }

    public Pelicula estreno(ZonedDateTime estreno) {
        this.setEstreno(estreno);
        return this;
    }

    public void setEstreno(ZonedDateTime estreno) {
        this.estreno = estreno;
    }

    public String getImagen() {
        return this.imagen;
    }

    public Pelicula imagen(String imagen) {
        this.setImagen(imagen);
        return this;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getUrl() {
        return this.url;
    }

    public Pelicula url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Perfiles> getPerfiles() {
        return this.perfiles;
    }

    public void setPerfiles(Set<Perfiles> perfiles) {
        if (this.perfiles != null) {
            this.perfiles.forEach(i -> i.setPelicula(null));
        }
        if (perfiles != null) {
            perfiles.forEach(i -> i.setPelicula(this));
        }
        this.perfiles = perfiles;
    }

    public Pelicula perfiles(Set<Perfiles> perfiles) {
        this.setPerfiles(perfiles);
        return this;
    }

    public Pelicula addPerfiles(Perfiles perfiles) {
        this.perfiles.add(perfiles);
        perfiles.setPelicula(this);
        return this;
    }

    public Pelicula removePerfiles(Perfiles perfiles) {
        this.perfiles.remove(perfiles);
        perfiles.setPelicula(null);
        return this;
    }

    public Set<Lista> getListas() {
        return this.listas;
    }

    public void setListas(Set<Lista> listas) {
        if (this.listas != null) {
            this.listas.forEach(i -> i.setPelicula(null));
        }
        if (listas != null) {
            listas.forEach(i -> i.setPelicula(this));
        }
        this.listas = listas;
    }

    public Pelicula listas(Set<Lista> listas) {
        this.setListas(listas);
        return this;
    }

    public Pelicula addLista(Lista lista) {
        this.listas.add(lista);
        lista.setPelicula(this);
        return this;
    }

    public Pelicula removeLista(Lista lista) {
        this.listas.remove(lista);
        lista.setPelicula(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pelicula)) {
            return false;
        }
        return id != null && id.equals(((Pelicula) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pelicula{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", genero='" + getGenero() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", calificacion=" + getCalificacion() +
            ", estreno='" + getEstreno() + "'" +
            ", imagen='" + getImagen() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
