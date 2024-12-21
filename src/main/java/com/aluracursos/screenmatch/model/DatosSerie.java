package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

//JsonAlias sirve para poder leer el atributo
public record DatosSerie(@JsonAlias("title") String titulo,
                         @JsonAlias("totalSeasons") int totalDeTemporadas,
                         @JsonAlias("imdbRating") Double evaluacion) {
}
