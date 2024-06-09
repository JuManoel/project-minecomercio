package edu.prog2.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;
import edu.prog2.model.*;

@SuppressWarnings("rawtypes")
public abstract class TransaccionService implements IService {
    protected ProductoService productoService;
    protected List<Transaccion> list;
    protected String fileName;
    protected Class<? extends Transaccion> clase;

    @Override
    public JSONObject add(String strJson) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    public JSONObject get(int index) {
        return list.get(index).toJSONObject();
    }

    @Override
    public JSONObject get(String id) throws Exception {
        Transaccion transaccion = this.clase.getConstructor(String.class).newInstance(id);
        int i = list.indexOf(transaccion);
        return i > -1 ? get(i) : null;
    }

    @Override
    public Transaccion getItem(String id) throws Exception {
        JSONObject json = get(id);
        Transaccion transaccion = this.clase.getConstructor(JSONObject.class).newInstance(json);
        return transaccion;
    }

    @Override
    public JSONObject getAll() {
        try {
            JSONArray data = new JSONArray(Utils.readText(this.fileName));
            return new JSONObject().put("message", "ok").put("data", data);
        } catch (IOException | JSONException e) {
            Utils.printStackTrace(e);
            return Utils.keyValueToJson("message", "Sin acceso a datos de productos", "error", e.getMessage());
        }
    }

    @Override
    public List load() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'load'");
    }

    @Override
    public JSONObject update(String id, String strJson) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void refreshAll() throws Exception {
        list = new ArrayList<>();
        load();
    }

    @Override
    public JSONObject remove(String id) throws Exception {
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public Class getDataType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDataType'");
    }

}
