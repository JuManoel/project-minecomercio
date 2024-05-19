package edu.prog2.model;

import org.json.JSONException;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;

public abstract class Persona implements Format {

  protected String id;
  protected String nombre;
  protected String correo;
  protected String telefono;
  protected String password; // anglicismo

  public Persona(String id, String nombre, String correo, String telefono, String password) throws Exception {
    setCorreo(correo);
    setId(id);
    setNombre(nombre);
    setPassword(password);
    setTelefono(telefono);
  }

  public Persona(String id) throws Exception {
    this(id, "NN", "", "", "");
  }

  public Persona(String nombre, String correo, String telefono, String password) throws Exception {
    setCorreo(correo);
    setId(Utils.getRandomKey(5));
    setNombre(nombre);
    setPassword(password);
    setTelefono(telefono);
  }

  public Persona(Persona persona) throws Exception {
    setCorreo(persona.getCorreo());
    setId(persona.getId());
    setNombre(persona.getNombre());
    setPassword(persona.getPassword());
    setTelefono(persona.getTelefono());
  }

  public Persona() {
    super();
  }

  public Persona(JSONObject json) throws JSONException, Exception {
    this(json.getString("id"), json.getString("nombre"), json.optString("correo"), json.getString("telefono"),
        json.getString("password")); // transforma de string a localdatetime
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) throws Exception {
    if (id == null || id.isBlank()) {
      throw new Exception("El ID tiene q tener algo");
    }
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  @Override
  // este método evita un warning al momento de implementar equals
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    // las referencias this y obj apuntan a la misma instancia
    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    return this.id.equals(((Persona) obj).id);
  }

  @Override
  public String toString() {
    String str = String.format(
        "Id: %s\n" +
            "Nombre: %s\n" +
            "Correo: %s\n" +
            "Telefono: %s\n" +
            "Password: %s\n",
        getId(), getNombre(), getCorreo(), getTelefono(), getPassword());
    return str;
  }

  @Override
  public JSONObject toJSONObject() {
    return new JSONObject(this);
  }
}
