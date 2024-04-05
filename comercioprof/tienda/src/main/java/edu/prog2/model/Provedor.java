package edu.prog2.model;

import org.json.JSONObject;

public class Provedor extends Persona{
    private boolean intermediario;
    public Provedor() {
        super();
    }

    public Provedor(String id, String nombre, String correo, String telefono, String password, boolean intermediario) {
        super(id, nombre, correo, telefono, password);
        setIntermediario(intermediario);
    }

    public Provedor(String id) {
        super(id);
    }

    public Provedor(String nombre, String correo, String telefono, String password, boolean intermediario) {
        super(nombre, correo, telefono, password);
        setIntermediario(intermediario);
    }

    public Provedor(Provedor provedor) {
        super((Persona) provedor);
        setIntermediario(provedor.getIntermediario());
    }
    public Provedor(JSONObject json) {
        super(json);
        setIntermediario(json.getBoolean("intermediario"));
    }

    public void setIntermediario(boolean intermediario) {
        this.intermediario = intermediario;
    }

    public boolean getIntermediario(){
        return this.intermediario;
    }

    @Override
  public String toString() {
    String str =super.toString();
      return str;
  }
}
