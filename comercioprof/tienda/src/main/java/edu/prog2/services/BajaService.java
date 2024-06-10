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

    private ProductoService productoService;

    public BajaService() throws JSONException, Exception {
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
        JSONObject json = new JSONObject(strJson);
        json.put("id", Utils.getRandomKey(5));
        Producto pro = new Producto(productoService.get(json.getString("producto")));
        Baja baja = new Baja(json);
        baja.setProducto(pro);
        if (list.contains(baja)) {
            throw new ArrayStoreException(String.format("El Baja %s ya existe", baja.getId()));
        }
        if(baja.getCantidad()>baja.getProducto().getDisponible()){
            throw new IllegalArgumentException("Estas quitando mas productos que debias");
        }
        pro.setDisponible(pro.getDisponible()-baja.getCantidad());
        productoService.update(pro.getId(), pro.toJSONObject().toString());
        if (list.add(baja)) {
            Utils.writeJSON(list, fileName);
        }
        return new JSONObject().put("message", "ok").put("data", baja.toJSONObject());
    }


    @Override
    public final List<Transaccion> load() throws JSONException, Exception {
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
        list = new ArrayList<>();
        load();
    }

    @Override
    public JSONObject get(String id) throws Exception {
        Baja baja = new Baja(id);
        int i = list.indexOf(baja);
        return i > -1 ? get(i) : null;
    }

    @Override
    public Baja getItem(String id) throws Exception {
        Baja baja = (Baja) super.getItem(id);
        return baja;
    }

    @Override
    public Class<Baja> getDataType() {
        return Baja.class;
    }

}
