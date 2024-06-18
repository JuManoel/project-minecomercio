package edu.prog2.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Provedor extends Persona {
    private boolean intermediario;

    public Provedor() {
        // por defecto
        super();
    }

    public Provedor(String id, String nombre, String correo, String telefono, String password, boolean intermediario)
            throws Exception {
        // con todo
        super(id, nombre, correo, telefono, password);
        setIntermediario(intermediario);
    }

    public Provedor(String id) throws Exception {
        // solo id
        super(id);
    }

    public Provedor(String nombre, String correo, String telefono, String password, boolean intermediario)
            throws Exception {
        // sin el id
        super(nombre, correo, telefono, password);
        setIntermediario(intermediario);
    }

    public Provedor(Provedor provedor) throws Exception {
        // constructor copia
        super((Persona) provedor);
        setIntermediario(provedor.getIntermediario());
    }

    public Provedor(JSONObject json) throws JSONException, Exception {
        // con json
        super(json);
        setIntermediario(json.getBoolean("intermediario"));
    }

    // acessores y modificadores
    public void setIntermediario(boolean intermediario) {
        this.intermediario = intermediario;
    }

    public boolean getIntermediario() {
        return this.intermediario;
    }

    @Override
    public String toString() {// to string
        String str = super.toString();
        str += "intermediario: " + this.intermediario + "\n";
        return str;
    }
}
