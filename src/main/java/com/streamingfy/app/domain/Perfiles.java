package com.streamingfy.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Perfiles.
 */
@Entity
@Table(name = "perfiles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Perfiles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "perfiles", "listas" }, allowSetters = true)
    private Pelicula pelicula;

    @OneToMany(mappedBy = "perfiles")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pelicula", "perfiles" }, allowSetters = true)
    private Set<Lista> listas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Perfiles id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagen() {
        return this.imagen;
    }

    public Perfiles imagen(String imagen) {
        this.setImagen(imagen);
        return this;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Perfiles nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Perfiles user(User user) {
        this.setUser(user);
        return this;
    }

    public Pelicula getPelicula() {
        return this.pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public Perfiles pelicula(Pelicula pelicula) {
        this.setPelicula(pelicula);
        return this;
    }

    public Set<Lista> getListas() {
        return this.listas;
    }

    public void setListas(Set<Lista> listas) {
        if (this.listas != null) {
            this.listas.forEach(i -> i.setPerfiles(null));
        }
        if (listas != null) {
            listas.forEach(i -> i.setPerfiles(this));
        }
        this.listas = listas;
    }

    public Perfiles listas(Set<Lista> listas) {
        this.setListas(listas);
        return this;
    }

    public Perfiles addLista(Lista lista) {
        this.listas.add(lista);
        lista.setPerfiles(this);
        return this;
    }

    public Perfiles removeLista(Lista lista) {
        this.listas.remove(lista);
        lista.setPerfiles(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Perfiles)) {
            return false;
        }
        return id != null && id.equals(((Perfiles) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Perfiles{" +
            "id=" + getId() +
            ", imagen='" + getImagen() + "'" +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
