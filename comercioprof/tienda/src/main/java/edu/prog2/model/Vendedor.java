package edu.prog2.model;

import org.json.JSONObject;

public class Vendedor extends Persona{

    private boolean admin;

    public Vendedor() {
        super();
    }
    public Vendedor(String id, String nombre, String telefono, String correo, String password, boolean admin) {
        super(id, nombre, telefono, correo, password);
        setAdmin(admin);
    }
    public Vendedor(String id, String nombre, String telefono, String correo, boolean admin) {
        super(id, nombre, telefono, correo);
        setAdmin(admin);
    }
    public Vendedor(Vendedor vendedor) {
        super((Persona) vendedor);
        setAdmin(vendedor.getAdmin());
    }

    public Vendedor(JSONObject json) {
        super(json);
        setAdmin(json.getBoolean("admin"));
    }

    public Vendedor(String id) {
        super(id);
     }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    public boolean getAdmin(){
        return this.admin;
    }

    @Override
  public String toString() {
    String str =super.toString();
    str =String.format(
      "admin: %b\n"
      , getAdmin());
      return str;
  }
}
