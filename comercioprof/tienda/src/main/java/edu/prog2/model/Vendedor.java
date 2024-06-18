package edu.prog2.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Vendedor extends Persona {

    private boolean admin;

    public Vendedor() {
        // el por defecto
        super();
    }

    public Vendedor(String id, String nombre, String telefono, String correo, String password, boolean admin)
            throws Exception {
        // con todo
        super(id, nombre, telefono, correo, password);
        setAdmin(admin);
    }

    public Vendedor(String id, String nombre, String telefono, String correo, boolean admin) throws Exception {
        // sin contraseña
        super(id, nombre, telefono, correo);
        setAdmin(admin);
    }

    public Vendedor(Vendedor vendedor) throws Exception {
        // copia
        super((Persona) vendedor);
        setAdmin(vendedor.getAdmin());
    }

    public Vendedor(JSONObject json) throws JSONException, Exception {
        // json
        super(json);
        setAdmin(json.getBoolean("admin"));
    }

    public Vendedor(String id) throws Exception {
        // solo id
        super(id);
    }

    // acessores y modificadores
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean getAdmin() {
        return this.admin;
    }

    @Override
    public String toString() {// to string
        String str = super.toString();
        str += String.format(
                "admin: %b\n", getAdmin());
        return str;
    }
}
