package edu.prog2.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;
import edu.prog2.model.*;

public class CompraVentaService extends TransaccionService {

    private Class<? extends Persona> receptor;
    // private Class<? extends Persona> vendedor;
    private PersonaService serV;
    private PersonaService serR;
    // private final Class<? extends CompraVenta> clase;
    private String compraVenta;

    public CompraVentaService(Class<? extends Persona> receptor) throws Exception {
        // this.vendedor = Vendedor.class;
        this.receptor = receptor;
        this.productoService = new ProductoService();
        serV = new PersonaService(Vendedor.class);
        if (this.receptor.equals(Cliente.class)) {
            compraVenta = "venta";
            this.clase = Venta.class;
            serR = new PersonaService(Cliente.class);
        } else if (this.receptor.equals(Provedor.class)) {
            compraVenta = "compra";
            this.clase = Compra.class;
            serR = new PersonaService(Provedor.class);
        } else {
            throw new Exception("Intentas crear una cosa que no existe");
        }
        fileName = Utils.PATH + compraVenta + ".json";
        if (Utils.fileExists(fileName)) {
            load();
        } else {
            list = new ArrayList<>();
        }
    }

    @Override
    public JSONObject add(String strJson) throws Exception {
        CompraVenta cv = creatCompraVenta(strJson);
        list.add(cv);
        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message", "ok").put("data", cv.toJSONObject());
    }

    @Override
    public JSONObject get(String id) throws Exception {
        CompraVenta compraVenta = (CompraVenta) this.clase.getConstructor(String.class).newInstance(id);
        int i = list.indexOf(compraVenta);
        return i > -1 ? get(i) : null;
    }

    @Override
    public CompraVenta getItem(String id) throws Exception {
        CompraVenta cv = (CompraVenta) super.getItem(id);
        if (this.compraVenta.equals("venta")) {
            return (Venta) cv;
        }
        return (Compra) cv;
    }

    @Override
    public List<Transaccion> load() throws Exception {
        this.productoService = new ProductoService();
        list = new ArrayList<>();
        String data = Utils.readText(fileName);
        JSONArray jsonArr = new JSONArray(data);
        CompraVenta cv;
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            if (compraVenta.equals("compra")) {
                String id = jsonObj.getString("id");
                Vendedor v = new Vendedor(jsonObj.getJSONObject("vendedor"));
                Provedor pro = new Provedor(jsonObj.getJSONObject("provedor"));
                LocalDateTime fecha = LocalDateTime.parse(jsonObj.getString("fechaHora"));
                JSONArray de = jsonObj.getJSONArray("detalles");
                ArrayList<Detalle> d = new ArrayList<>();
                for (int j = 0; j < de.length(); j++) {
                    JSONObject jsonObject = de.getJSONObject(j);
                    Producto p = new Producto(jsonObject.getJSONObject("producto"));
                    d.add(new Detalle(p, jsonObject.getInt("cantidad")));
                }

                cv = new Compra(id, pro, v, fecha, d);
            } else {
                String id = jsonObj.getString("id");
                Vendedor v = new Vendedor(jsonObj.getJSONObject("vendedor"));
                Cliente c = new Cliente(jsonObj.getJSONObject("cliente"));
                LocalDateTime fecha = LocalDateTime.parse(jsonObj.getString("fechaHora"));
                JSONArray de = jsonObj.getJSONArray("detalles");
                ArrayList<Detalle> d = new ArrayList<>();
                for (int j = 0; j < de.length(); j++) {
                    JSONObject jsonObject = de.getJSONObject(j);
                    Producto p = productoService.getItem(jsonObject.getJSONObject("producto").getString("id"));
                    d.add(new Detalle(p, jsonObject.getInt("cantidad")));
                }
                cv = new Venta(id, c, v, fecha, d);

            }
            list.add(cv);
        }
        /* */
        return list;
    }

    @Override
    public JSONObject update(String id, String strJson) throws Exception {

        JSONObject json = new JSONObject(strJson);
        CompraVenta cv = getItem(id);
        int i = list.indexOf(cv);

        if (cv == null) {
            throw new NullPointerException("No se encontró el producto " + id);
        }

        if (json.has("fechaHora")) {
            // sólo permitir el cambio de descripcion si no existe otro producto con la
            // nueva descripcion
            LocalDateTime time = LocalDateTime.parse(json.getString("fechaHora"));
            if (!time.equals(cv.getFechaHora())) {
                CompraVenta aux = (CompraVenta) this.clase.getConstructor().newInstance();
                aux.setFechaHora(time);
                // se generará una excepción si existe un producto con la descripción que se
                // pretende utilizar para el cambio
                list.contains(aux);
            }
        }

        // crear un objeto JSON con las propiedades del objeto a actualizar
        JSONObject aux = cv.toJSONObject();
        // iterar sobre las propiedades del objeto json recibido como argumento
        JSONArray propiedades = json.names();
        for (int k = 0; k < propiedades.length(); k++) {
            // asignar a aux los nuevos valores de las propiedades dadas
            String propiedad = propiedades.getString(k);
            Object valor = json.get(propiedad);
            aux.put(propiedad, valor);
        }
        // utilizar aux para actualizar el producto
        Persona per;
        Vendedor ven = new Vendedor(serV.get(aux.getString("vendedor")));

        ArrayList<Detalle> detalles = new ArrayList<>();
        if(json.has("detalle")){
            JSONArray jArray = json.getJSONArray("detalle");
            for (int j = 0; j < jArray.length(); j++) {
                JSONObject jsonFor = (JSONObject) jArray.get(j);
                Producto pFor = new Producto(productoService.get(jsonFor.getString("producto")));
                detalles.add(new Detalle(pFor, jsonFor.getInt("cantidad")));
            }
        }
        
        if (compraVenta.equals("venta")) {
            per = new Cliente(serR.get(aux.getString("cliente")));
            cv = new Venta(cv.getId(), (Cliente) per, ven, LocalDateTime.parse(aux.getString("fechaHora")), detalles);

        } else {
            per = new Provedor(serR.get(aux.getString("provedor")));
            cv = new Compra(cv.getId(), (Provedor) per, ven, LocalDateTime.parse(aux.getString("fechaHora")), detalles);

        }
        list.set(i, cv);
        // actualizar el archivo de productos
        Utils.writeJSON(list, fileName);
        // devolver el producto con los cambios realizados
        return new JSONObject().put("message", "ok").put("data", cv.toJSONObject());
    }

    private void updateProductos(Producto pro, CompraVenta cv, int i) throws Exception {
        /*
         * This method is just for update cantidad of products
         */
        if (compraVenta.equals("compra")) {
            pro.setDisponible(pro.getDisponible() + cv.getDetalles().get(i).getCantidad());
        } else {
            pro.setDisponible(pro.getDisponible() - cv.getDetalles().get(i).getCantidad());
        }
        productoService.update(pro.getId(), pro.toJSONObject().toString());
    }

    private CompraVenta creatCompraVenta(String strJson) throws Exception {
        productoService = new ProductoService();// actualiza los productos
        CompraVenta cv;
        JSONObject json = new JSONObject(strJson);
        System.out.println(json.toString(2));
        json.put("id", Utils.getRandomKey(5));
        ArrayList<Detalle> detalles = new ArrayList<>();
        JSONArray jsonArray = json.getJSONArray("detalle");
        Producto p;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            p = new Producto(productoService.get(jsonObject.getString("producto")));
            detalles.add(new Detalle(p, jsonObject.getInt("cantidad")));

        }
        if (this.compraVenta.equals("venta")) {
            System.out.println(json.getString("fechaHora"));
            cv = new Venta(new Cliente(this.serR.get(json.getString("cliente"))),
            new Vendedor(serV.get(json.getString("vendedor"))),
            LocalDateTime.parse(json.getString("fechaHora")), detalles);
        } else if (compraVenta.equals("compra")) {
            Provedor prov = new Provedor(this.serR.get(json.getString("provedor")));
            cv = new Compra(prov, new Vendedor(serV.get(json.getString("vendedor"))),
                    LocalDateTime.parse(json.getString("fechaHora")), detalles);
        } else {
            throw new Exception("No existe ese tipo de transacion");
        }
        for (int i = 0; i < cv.getDetalles().size(); i++) {
            Detalle proD = cv.getDetalles().get(i);
            Producto pro = proD.getProducto();
            updateProductos(pro, cv, i);

        }
        cv.setId(json.getString("id"));
        return cv;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<CompraVenta> getDataType() {
        return (Class<CompraVenta>) this.clase;
    }

}
