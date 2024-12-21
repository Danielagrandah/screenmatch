package com.aluracursos.screenmatch;


import com.aluracursos.screenmatch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;


public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.muestraMenu();
//		var consumoApi = new ConsumoAPI();
//		var json = consumoApi.obtenerDatos("http://www.omdbapi.com/?t=game+of+thrones&apikey=f1e64aca");
//		// Muestra una imagen aleqtoriq
//		var json = consumoApi.obtenerDatos("https://coffee.alexflipnote.dev/random.json");
//		System.out.println(json);
//
//		ConvierteDatos conversor = new ConvierteDatos();
//		var datos = conversor.obtenerDatos(json, DatosSerie.class);
//		System.out.println(datos);
//
//		json = consumoApi.obtenerDatos("https://www.omdbapi.com/?t=game+of+thrones&Season=1&episode=1&apikey=f1e64aca");
//		DatosEpisodio episodio = conversor.obtenerDatos(json, DatosEpisodio.class);
//		System.out.println(episodio);

/*		List<DatosTemporadas> temporadas = new ArrayList<>();
		for (int i = 1; i <= datos.totalDeTemporadas() ; i++) {
			json = consumoApi.obtenerDatos("https://www.omdbapi.com/?t=game+of+thrones&Season="+ i +"&apikey=f1e64aca");
			var datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
			temporadas.add(datosTemporadas);
		}
		temporadas.forEach(System.out::println);*/

	}
}
