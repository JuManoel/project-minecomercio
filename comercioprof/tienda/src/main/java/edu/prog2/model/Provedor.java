package edu.prog2.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Provedor extends Persona {
    private boolean intermediario;

    public Provedor() {
        super();
    }

    public Provedor(String id, String nombre, String correo, String telefono, String password, boolean intermediario)
            throws Exception {
        super(id, nombre, correo, telefono, password);
        setIntermediario(intermediario);
    }

    public Provedor(String id) throws Exception {
        super(id);
    }

    public Provedor(String nombre, String correo, String telefono, String password, boolean intermediario)
            throws Exception {
        super(nombre, correo, telefono, password);
        setIntermediario(intermediario);
    }

    public Provedor(Provedor provedor) throws Exception {
        super((Persona) provedor);
        setIntermediario(provedor.getIntermediario());
    }

    public Provedor(JSONObject json) throws JSONException, Exception {
        super(json);
        setIntermediario(json.getBoolean("intermediario"));
    }

    public void setIntermediario(boolean intermediario) {
        this.intermediario = intermediario;
    }

    public boolean getIntermediario() {
        return this.intermediario;
    }

    @Override
    public String toString() {
        String str = super.toString();
        str += "intermediario: " + this.intermediario + "\n";
        return str;
    }
}
