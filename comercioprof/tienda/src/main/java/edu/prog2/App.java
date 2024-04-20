package edu.prog2;

import edu.prog2.helpers.Controller;
import edu.prog2.helpers.Utils;
import edu.prog2.model.*;
import edu.prog2.services.*;
import io.javalin.Javalin;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class App {

  private static final Logger LOG = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) throws Exception {
    Locale.setDefault(Locale.of("es_CO"));
    IService<Producto> productoService = new ProductoService();
    IService<Persona> clienteService = new PersonaService(Cliente.class);
    IService<Persona> provedorService = new PersonaService(Provedor.class);
    IService<Persona> vendedorService = new PersonaService(Vendedor.class);
    String message = String.format(
       "%sIniciando la API Rest de Ventas. Use Ctrl+C para detener la ejecución%s",
       Utils.CYAN, Utils.RESET
    );
    LOG.info(message);

    Utils.trace = true;
    System.out.println(Utils.trace);
    System.out.println(args);
    if (args.length > 0) {
      Utils.trace = Boolean.parseBoolean(args[0]);
    }

    if (Utils.trace) {
      LOG.info(String.format(
         "%sHabilitada la traza de errores%s", Utils.YELLOW, Utils.RESET
      ));
    } else {
      LOG.info(String.format(
         "%sEnvíe un argumento true|false para habilitar|deshabilitar la traza de errores%s",
         Utils.YELLOW, Utils.RESET
      ));
    }

    Javalin
    .create(config -> {
      config.http.defaultContentType = "application/json";
   
         config.bundledPlugins.enableCors(cors -> {
          cors.addRule(it -> it.anyHost());
      });
   
   
      config.router.apiBuilder(() -> {
          new Controller<>(productoService);
          new Controller<>(clienteService);
          new Controller<>(vendedorService);
          new Controller<>(provedorService);
      });
   }  )
    .start(7070)
    .get("/", ctx -> ctx.json(
         "{ \"data\": \"Bienvenido al servicio de ventas\", \"message\": \"ok\" }"
     ))
    .exception(
      Exception.class,
      (e, ctx) -> {
          Utils.printStackTrace(e);
          String error = Utils.keyValueToStrJson(
            "message", e.getMessage(), "request", ctx.fullUrl()
          );
          ctx.json(error).status(400);
      }
   )
  .error(404, ctx -> { /* nada por implementar aún */ });

  Runtime
  .getRuntime()
  .addShutdownHook(
  new Thread(() -> {
    LOG.info(String.format(
      "%sEl servidor Jetty de Javalin ha sido detenido%s%n", 
      Utils.RED, Utils.RESET
    ));
  })
);
  }
}