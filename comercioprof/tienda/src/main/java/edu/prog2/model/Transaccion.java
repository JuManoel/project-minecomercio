package edu.prog2.model;

import edu.prog2.helpers.Utils;
import java.time.LocalDateTime;

import org.json.JSONObject;

public abstract class Transaccion implements Format {

  protected String id;
  protected LocalDateTime fechaHora;

  public Transaccion() {
    // el dato LocalDateTime.MIN se verá con un formato feo, pero déjelo así
    this(Utils.getRandomKey(5), LocalDateTime.MAX);
  }

  public Transaccion(String id, LocalDateTime fechaHora) {
    setFechaHora(fechaHora);
    setId(id);
  }

  public Transaccion(Transaccion transaccion) {
    setFechaHora(transaccion.getFechaHora());
    setId(transaccion.getId());
  }

  public Transaccion(String id) {
    setId(id);
    setFechaHora(LocalDateTime.MIN);
  }

  public Transaccion(JSONObject json) {
    this(json.getString("id"), LocalDateTime.parse(json.getString("fechaHora")));
  }

  public LocalDateTime getFechaHora() {
    return fechaHora;
  }

  public final void setFechaHora(LocalDateTime fechaHora) {
    LocalDateTime aux = LocalDateTime.parse("2024-01-01T00:00:00");
    if (fechaHora.isBefore(aux)) {  
      throw new IllegalArgumentException("Fecha inferior a 2024-01-01 00:00");
    }

    this.fechaHora = fechaHora;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    final Transaccion other = (Transaccion) obj;

    if (!this.id.equals(other.getId())) {
      // mostrar una advertencia cuando los IDs sean distintos y las descripciones
      // sean iguales
      if (this.fechaHora.equals(other.getFechaHora())) {
        String message = String.format("Advertencia. Las Transaciones id %s y %s tinen la misma Fecha y Hora \"%s\"",
            id, other.id, this.fechaHora);
        System.out.println(message);
      }
      return false;
    }

    return true;
  }

  @Override
  public String toString() {
    String str = String.format(
        "Id: %s\n" +
            "fechaHora: %s\n",
        getId(), getFechaHora().toString());
    return str;
  }

  @Override
  public JSONObject toJSONObject() {
    String strJson;
    strJson = """
        {
          id:"%s",
          fechaHora: "%s"
        }
          """;
    strJson = String.format(strJson, this.id, this.fechaHora.toString());
    return new JSONObject(strJson);
  }
}
