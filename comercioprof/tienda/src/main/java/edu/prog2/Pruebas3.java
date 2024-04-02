package edu.prog2;

import io.javalin.Javalin;
import java.util.Locale;

public class Pruebas3 {

  public static void main(String[] args) {
    // esencial para estandarizar el formato monetario con separador de punto decimal, no con coma
    Locale.setDefault(Locale.of("es_CO"));
    Javalin.create(/*config*/).get("/", ctx -> ctx.result("Hola mundo")).start(7070);
  }
}
