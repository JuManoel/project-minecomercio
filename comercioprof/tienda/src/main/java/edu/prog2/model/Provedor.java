package edu.prog2.model;

import org.json.JSONObject;

public class Provedor extends Persona{
    public Provedor() {
        super();
    }

    public Provedor(String id, String nombre, String correo, String telefono, String password) {
        super(id, nombre, correo, telefono, password);
    }

    public Provedor(String id) {
        super(id);
    }

    public Provedor(String nombre, String correo, String telefono, String password) {
        super(nombre, correo, telefono, password);
    }

    public Provedor(Provedor provedor) {
        super((Persona) provedor);
    }
    public Provedor(JSONObject json) {
        super(json);
    }

    @Override
  public String toString() {
    String str =super.toString();
      return str;
  }
}
