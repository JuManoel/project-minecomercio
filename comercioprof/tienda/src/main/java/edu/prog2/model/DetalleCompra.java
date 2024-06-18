package edu.prog2.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DetalleCompra extends Detalle {
    // el detalle de compra tiene como unico proposito hacer el calculo del
    // subtotal.
    // pero por supuesto el nescesita los constructores
    public DetalleCompra() {
        super();
    }

    public DetalleCompra(Producto producto, int cantidad) throws Exception {
        super(producto, cantidad);
    }

    public DetalleCompra(Detalle detalle) throws Exception {
        super(detalle);
    }

    public DetalleCompra(JSONObject json) throws JSONException, Exception {
        super(json);
    }

    @Override
    public double getSubTotal() {// la continuacion del metodo subtotal
        return Math.round(super.getSubTotal() * this.getProducto().getValorBase());
    }
}
