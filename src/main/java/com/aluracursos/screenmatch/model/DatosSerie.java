package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
//JsonAlias sirve para poder leer el atributo
public record DatosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") int totalDeTemporadas,
                         @JsonAlias("imdbRating") Double evaluacion,
                         @JsonAlias("Genre") String genero,
                         @JsonAlias("Actors") String actores,
                         @JsonAlias("Language") String idiomas,
                         @JsonAlias("Awards") String premios,
                         @JsonAlias("Poster") String poster,
                         @JsonAlias("Plot") String sinopsis){
}
