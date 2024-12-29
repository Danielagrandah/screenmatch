package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosEpisodio;
import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import javax.crypto.spec.PSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=f1e64aca";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();

    public void muestraMenu(){
        var opcion = -1;
        while (opcion != 0 ){
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar Series Buscadas
                    
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
        DatosSerie datosSerie = getDatosSerie();
        List<DatosTemporadas> temporadas = new ArrayList<>();

        for (int i = 1; i <= datosSerie.totalDeTemporadas() ; i++) {
            var json = consumoApi.obtenerDatos(URL_BASE + datosSerie.titulo().replace(" ", "+") + "&Season=" + i + API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporadas);
        }
        temporadas.forEach(System.out::println);
    }
    private void buscarSerieWeb(){
        DatosSerie datos = getDatosSerie();
        datosSeries.add(datos);
        System.out.println(datos);

    }

    private void mostrarSeriesBuscadas() {
        datosSeries.forEach(System.out::println);
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
