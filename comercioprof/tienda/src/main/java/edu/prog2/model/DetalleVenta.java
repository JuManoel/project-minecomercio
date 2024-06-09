package edu.prog2.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DetalleVenta extends Detalle {
    public DetalleVenta() {
        super();
    }

    public DetalleVenta(Producto producto, int cantidad) throws Exception {
        super(producto, cantidad);
    }

    public DetalleVenta(Detalle detalle) throws Exception {
        super(detalle);
    }

    public DetalleVenta(JSONObject json) throws JSONException, Exception {
        super(json);
    }
    @Override
    public double getSubTotal() {
        return Math.round(super.getSubTotal()*this.getProducto().getValorVenta());
    }
}
