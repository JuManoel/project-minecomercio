package edu.prog2.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;
import edu.prog2.model.Baja;
import edu.prog2.model.Producto;
import edu.prog2.model.Transaccion;

public class BajaService extends TransaccionService {
    // el service de baja
    private ProductoService productoService;

    public BajaService() throws JSONException, Exception {
        // cojo el archivo de baja.json
        this.fileName = Utils.PATH + "Baja.json";
        productoService = new ProductoService();
        if (Utils.fileExists(fileName)) {
            load();
        } else {
            list = new ArrayList<>();
        }
    }

    @Override
    public JSONObject add(String strJson) throws Exception {
        // metodo para adicionar una baja dado una string de json
        JSONObject json = new JSONObject(strJson);
        json.put("id", Utils.getRandomKey(5));
        Producto pro = new Producto(productoService.get(json.getString("producto")));
        Baja baja = new Baja(json);
        baja.setProducto(pro);
        if (list.contains(baja)) {// valido si ya no existe esa baja
            throw new ArrayStoreException(String.format("El Baja %s ya existe", baja.getId()));
        }
        if (baja.getCantidad() > baja.getProducto().getDisponible()) {// validar si la cantidad de baja es aceptale
            throw new IllegalArgumentException("Estas quitando mas productos que debias");
        }
        pro.setDisponible(pro.getDisponible() - baja.getCantidad());// cambio el producto
        productoService.update(pro.getId(), pro.toJSONObject().toString());// hago el cambio con ayuda del service
        if (list.add(baja)) {
            Utils.writeJSON(list, fileName);
        }
        return new JSONObject().put("message", "ok").put("data", baja.toJSONObject());
    }

    @Override
    public final List<Transaccion> load() throws JSONException, Exception {
        // cargo todas las bajas
        list = new ArrayList<>();

        String data = Utils.readText(fileName);
        JSONArray jsonArr = new JSONArray(data);

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            list.add(new Baja(jsonObj));
        }

        return list;
    }

    @Override
    public void refreshAll() throws JSONException, Exception {
        // para recarga la base de datos
        list = new ArrayList<>();
        load();
    }

    @Override
    public JSONObject get(String id) throws Exception {
        // cojer dado un id
        Baja baja = new Baja(id);
        int i = list.indexOf(baja);
        return i > -1 ? get(i) : null;
    }

    @Override
    public Baja getItem(String id) throws Exception {
        // cojer dado el id
        Baja baja = (Baja) super.getItem(id);
        return baja;
    }

    @Override
    public Class<Baja> getDataType() {
        // devulve la classe de Baja
        return Baja.class;
    }

}
