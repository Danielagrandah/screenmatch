package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;

import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;


import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=f1e64aca";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series;
    private Optional<Serie> serieBuscada;

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void muestraMenu(){
        var opcion = -1;
        while (opcion != 0 ){
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar Series Buscadas
                    4 - Buscar Series por titulo
                    5 - Top 5 de Series
                    6 - Buscar Series por categoria.
                    7 - Buscar por Series por temporadas y por evaluacion.
                    8 - Buscar episodios por nombre.
                    9 - Top 5 episodios por serie.
                    
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    buscarSeriesPorTemporadaYEvaluacion();
                    break;
                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                    top5EpisodiosPorSerie();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación");
                default:
                    System.out.println("Opción invalida");
            }

        }
    }




    private DatosSerie getDatosSerie() {
        // Busca los datos generales de la Serie.
        System.out.println("*******************\n"+ "Por favor escribe el nombre de la serie que deaseas buscar: ");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie de la cual quieres ver los episodios");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()){
            var serieEncontrada = serie.get();

            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalDeTemporadas() ; i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&Season=" + i + API_KEY);
                var datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporadas);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }


    }
    private void buscarSerieWeb(){
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repositorio.save(serie);
        //datosSeries.add(datos);
        System.out.println(datos);

    }

    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escribe el nombre de la serie que deseas buscar  ");
        var nombreSerie = teclado.nextLine();
        serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if (serieBuscada.isPresent()){
            System.out.println("La serie buscada es: " + serieBuscada.get());
        }else{
            System.out.println("Serie no encontrada");
        }
    }

    // 5 - Buscar el top 5 de las series
    private void buscarTop5Series() {
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo()  +
                "Evaluacion" + s.getEvaluacion()));
    }
     //6 -Buscar serie por Categoria
     private void buscarSeriesPorCategoria() {
         System.out.println("Escribe el genero/categoria de la serie que deseas buscar  ");
         var genero = teclado.nextLine();
         var categoria = Categoria.fromEspanol(genero);
         List<Serie> seriePorCategoria = repositorio.findByGenero(categoria);
         System.out.println("La serie de la categoria: " + genero);
         seriePorCategoria.forEach(System.out::println);

     }

     // 7 - Temporadas y Evaluacion

    private void buscarSeriesPorTemporadaYEvaluacion() {
        System.out.println("Escribe el numero de temporadas deseas buscar");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Escribe la evaluacion a partir de la cual deseas buscar");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> seriePorTemporadaYEvaluacion = repositorio.filtrarSeriesPorTemporadaYEvaluacion(totalTemporadas, evaluacion);
        System.out.println("****Series encontradas****");
        seriePorTemporadaYEvaluacion.forEach(s ->
                System.out.println(s.getTitulo() + " - Evaluación: "+ s.getEvaluacion()));

    }
    /// 7 sin utilizar la @Query
   /*  se debe cambiar la linea de lista
        List<Serie> seriePorTemporadaYEvaluacion = repositorio.findByTotalDeTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(totalTemporadas, evaluacion);
    }*/

    /// 8
    private void buscarEpisodiosPorTitulo() {
        System.out.println("Escribe el nombre del episodio que deseas buscar");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e -> System.out.printf("Serie: %s Temporada %s Episodio %s Evaluacion %s\n", e.getSerie(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));
    }

    /// 9 Top 5 de episodios por Titulo
    private void top5EpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if( serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
            topEpisodios.forEach(e -> System.out.printf("Serie: %s Temporada %s Episodio %s Evaluacion %s\n", e.getSerie(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));

        }


    }

/*


        //Mostrar solo el titulo de los episodios para las temporadas

        */
/*for (int i = 0; i < datos.totalDeTemporadas(); i++) {
            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemporada.size(); j++) {
                System.out.println(episodiosTemporada.get(j).titulo());

            }

        }*//*

        // Simplica el fori e imprime el titulo de los episodios para las temporadas
        //temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //Convertir todas las infroamciones a una lista del tipo datosEpisodio

        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());



        // Top 5 Episodios
       */
/* System.out.println("Top 5 mejores Episodios ");
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primer Filtro N/A" + e)) //permite verificar paso paso por lo que se esta ejecuntando.
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .peek(e -> System.out.println("Segundo Filtro ordenación (M>m)" + e))
                .map(e -> e.titulo().toUpperCase())
                .peek(e -> System.out.println("Tercer Filtro Mayusculas (m>M)" + e))
                .limit(5)
                .forEach(System.out::println);
*//*

        //Convirtiendo los datos a una lista del tipo episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        //episodios.forEach(System.out::println);

        //Busqueda de epsiodios por anio

        */
/*System.out.println("Por favor indica el año a partir del cual deseas ver los episodios");
        var fecha = teclado.nextInt();
        teclado.nextLine();

        LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);*//*


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
*/
/*        episodios.stream()
                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() + ","+
                                " Episodio: " + e.getTitulo() + ","+
                                " Fecha de Lanzamiento: " + e.getFechaDeLanzamiento().format(dtf)
                ));*//*


        //Busca episodios por pedazo del titulo
      */
/*  System.out.println("por favor ingresa el titulo del episodio que deseas ver: ");
        var pedazoTitulo = teclado.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase())) // encuentra un pedazo de titulo
                .findFirst();
        if(episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado: ");
            System.out.println("Los datos son: " +episodioBuscado.get());
        }else{
            System.out.println("No se encontro ningun episodio relacionado con ese nombre: " + pedazoTitulo);
        }*//*


        Map<Integer, Double> evaluacionesPorTemporada = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0 )
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println("Evaluaciones por temporada: " + evaluacionesPorTemporada );

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("Media de las evaluciones: " + est.getAverage());
        System.out.println("Episodio meor evaluado: " + est.getMax());
        System.out.println("Episodio peor evaluado: " + est.getMin());

*/


}
