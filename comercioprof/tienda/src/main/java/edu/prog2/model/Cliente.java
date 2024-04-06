package edu.prog2.model;

import org.json.JSONObject;

public class Cliente extends Persona{
    private boolean credito;
    
    public Cliente() {
        super();
    }
    public Cliente(String id, String nombre, String telefono, String correo, String password,boolean credito) {
        super(id, nombre, telefono, correo, password);
        setCredito(credito);
    }
    public Cliente(String id, String nombre, String telefono, String correo, boolean credito) {
        super(id, nombre, telefono, correo);
        setCredito(credito);
    }
    public Cliente(Cliente cliente) {
        super((Persona) cliente);
        setCredito(cliente.getCredito());
    }
    public Cliente(JSONObject json) { //hacer ensayos de Json a Java
        super(json); //transforma de string a localdatetime
        setCredito(json.getBoolean("credito"));
      }

    public Cliente(String id) {
       super(id);
    }
      
    public void setCredito(boolean credito) {
        this.credito = credito;
    }
    public boolean getCredito(){
        return this.credito;
    }

    @Override
  public String toString() {
    String str =super.toString();
    str =String.format(
      "credito: %b\n"
      , getCredito());
      return str;
  }
}
