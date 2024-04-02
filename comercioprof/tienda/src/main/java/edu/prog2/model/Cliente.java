package edu.prog2.model;

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

    public void setCredito(boolean credito) {
        this.credito = credito;
    }
    public boolean getCredito(){
        return this.credito;
    }
}
