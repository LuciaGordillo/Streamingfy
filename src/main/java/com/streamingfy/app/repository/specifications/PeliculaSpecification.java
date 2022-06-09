package com.streamingfy.app.repository.specifications;

import com.streamingfy.app.domain.Pelicula;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

public interface PeliculaSpecification {
    public static Specification<Pelicula> busquedaPelicula(String filtro) {
        final Logger log = LoggerFactory.getLogger(Pelicula.class);
        return new Specification<Pelicula>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Pelicula> pelicula, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> organizador = new ArrayList<>();
                log.debug("LLega a specification");
                Expression<String> titulo = pelicula.get("nombre").as(String.class);
                if (null != filtro) {
                    log.debug("LLega a busquedaistado");

                    organizador.add(criteriaBuilder.like(titulo, "%" + filtro + "%"));
                }
                return criteriaBuilder.and(organizador.toArray(new Predicate[] {}));
            }
        };
    }
}
