package edu.prog2.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Detalle implements Format {
    private Producto producto;
    private int cantidad;

    public Detalle() {
        // por defecto
        super();
    }

    public Detalle(Producto producto, int cantidad) throws Exception {
        // producto y cantidad
        setCantidad(cantidad);
        setProducto(producto);
    }

    public Detalle(Detalle detalle) throws Exception {
        // copia
        setCantidad(detalle.getCantidad());
        setProducto(detalle.getProducto());
    }

    public Detalle(JSONObject json) throws JSONException, Exception {
        // json
        setCantidad(json.getInt("cantidad"));
        setProducto(new Producto(json.getString("producto")));
    }

    public double getSubTotal() {
        // el subtotal incopleto
        return this.cantidad * (1 + (this.producto.getIva() / 100));
    }

    // acesores y modificadores
    public int getCantidad() {
        return cantidad;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setCantidad(int cantidad) throws Exception {
        if (cantidad <= 0) {
            throw new Exception("No puede valoes negativos");
        }
        this.cantidad = cantidad;
    }

    @Override // el equals
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

        return this.producto.equals(((Detalle) obj).producto) && this.cantidad == ((Detalle) obj).cantidad;
    }

    @Override // sirve para cojer el "codigo del la classe" como su memory posicion
    public int hashCode() {
        return super.hashCode();
    }

    @Override // to string
    public String toString() {
        String str = "producto:" + producto.getId() + "\n" +
                "Cantidad: " + this.cantidad + "\n";
        return str;
    }

    @Override // to json
    public JSONObject toJSONObject() {
        String json = """
                {
                    producto: %s,
                    cantidad: %d
                }
                """;
        json = String.format(json, this.producto.getId(), this.getCantidad());
        return new JSONObject(json);
    }

}
