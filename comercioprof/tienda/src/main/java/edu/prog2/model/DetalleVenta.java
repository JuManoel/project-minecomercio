package edu.prog2.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DetalleVenta extends Detalle {
    // de la misma forma que el DetalleCompra, detalleVenta tiene la misma mission,
    // solo para hacer el
    // calculo del subtotal para ventas
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
    public double getSubTotal() {// contemplem el metodo getSubTotal completo
        return Math.round(super.getSubTotal() * this.getProducto().getValorVenta());
    }
}
