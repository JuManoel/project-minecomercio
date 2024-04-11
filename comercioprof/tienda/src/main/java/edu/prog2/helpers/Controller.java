package edu.prog2.helpers;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.patch;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

import edu.prog2.model.TipoProducto;
import edu.prog2.services.Service;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class Controller<T> {

  public Controller(final Service<T> service) {
    path(
      service.endPoint(),//isso permite pegar acessar as classes
      () -> {
        get("", (ctx) -> response(ctx, service.getAll()));//controla as solicitações de HTTP

        get(
          "/{param}",
          ctx -> {
            String arg = ctx.pathParam("param");
            if (arg.equals("categorias")) {
              response(ctx, TipoProducto.getAll());
            } else if (arg.matches("-?\\d+")) {
              // si es un número en base 10, buscar por índice
              int i = Integer.parseInt(arg, 10);
              response(ctx, service.get(i));
            } else {
              response(ctx, service.get(arg));
            }
          }
        );

        post("", ctx -> response(ctx, service.add(ctx.body())));

        patch( // también hubiera podido ser put
          "/{param}",
          ctx -> {
            String id = ctx.pathParam("param");
            String newData = ctx.body();
            response(ctx, service.update(id, newData));
          }
        );

        delete("/{param}", ctx -> response(ctx, service.remove(ctx.pathParam("param"))));
      }
    );
  }

  private Context response(@NotNull Context ctx, JSONObject json) {
    if (json == null) {
      ctx.status(404);
      json = new JSONObject().put("request", ctx.fullUrl()).put("error", "La solicitud ha producido 'null' como respuesta");
    } else if (json.has("error")) {
      ctx.status(404);
      json.put("request", ctx.fullUrl());
    }
    return ctx.json(json.toString());
  }

  /**
   * Recibe un objeto Service<T> como parámetro y devuelve una cadena que representa el
   * nombre de la clase del servicio, convertido a minúsculas y sin el sufijo "Service".
   * @param service
   * @return
   */
  private String getClassName(final Service<T> service) {
    String serviceName = service.getClass().getSimpleName();
    int fin = serviceName.length() - 7;
    String className = serviceName.substring(0, fin).toLowerCase();
    System.out.println(className);
    return className;
  }
}
