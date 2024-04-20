package edu.prog2.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Vendedor extends Persona{

    private boolean admin;

    public Vendedor() {
        super();
    }
    public Vendedor(String id, String nombre, String telefono, String correo, String password, boolean admin) throws Exception {
        super(id, nombre, telefono, correo, password);
        setAdmin(admin);
    }
    public Vendedor(String id, String nombre, String telefono, String correo, boolean admin) throws Exception {
        super(id, nombre, telefono, correo);
        setAdmin(admin);
    }
    public Vendedor(Vendedor vendedor) throws Exception {
        super((Persona) vendedor);
        setAdmin(vendedor.getAdmin());
    }

    public Vendedor(JSONObject json) throws JSONException, Exception {
        super(json);
        setAdmin(json.getBoolean("admin"));
    }

    public Vendedor(String id) throws Exception {
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
