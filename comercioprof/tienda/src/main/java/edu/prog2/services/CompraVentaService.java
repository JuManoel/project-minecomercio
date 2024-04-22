package edu.prog2.services;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.util.ssl.SslContextFactory.Client;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.prog2.helpers.Utils;
import edu.prog2.model.Cliente;
import edu.prog2.model.CompraVenta;
import edu.prog2.model.Detalle;
import edu.prog2.model.Producto;
import edu.prog2.model.Vendedor;

public class CompraVentaService implements IService{

    private List<JSONObject> list;
    private final String fileName;
    private final Class<? extends CompraVenta> clase;

    public CompraVentaService(Class<? extends CompraVenta> clase) throws Exception {
        this.clase = clase;
        fileName = Utils.PATH + clase.getSimpleName() + ".json";
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
        CompraVenta p = clase.getConstructor(JSONObject.class).newInstance(json);
        for (JSONObject jsonObject : list) {
            if(jsonObject.getString("id").equals(json.getString("id"))){
                throw new Exception("Ya existe elemento con el ID");
            }
        }
        list.add(json);
        Utils.writeJSON(list, fileName);
        /*
        try {
            // Ler o conteúdo atual do arquivo JSON
            FileReader fileReader = new FileReader(fileName);
            JSONTokener tokener = new JSONTokener(fileReader);
            JSONArray jsonArray = new JSONArray(tokener);
            fileReader.close();

            // Converter a string JSON para um objeto JSON

            // Adicionar o novo objeto JSON ao array existente
            jsonArray.put(json);

            // Escrever o conteúdo atualizado de volta no arquivo JSON
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(jsonArray.toString(2)); // O segundo argumento (2) é para a formatação de espaços
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return new JSONObject().put("message", "ok").put("data", p.toJSONObject());
    }

    @Override
    public JSONObject get(int index) {
        return list.get(index);
    }

    @Override
    public JSONObject get(String id) throws Exception {
        CompraVenta compraVenta = this.clase.getConstructor(String.class).newInstance(id);
        int i = list.indexOf(compraVenta);
        return i > -1 ? get(i) : null;
    }

    @Override
    public CompraVenta getItem(String id) throws Exception {
        JSONObject json= get(id);
        return this.clase.getConstructor(JSONObject.class).newInstance(json);
    }

    @Override
    public JSONObject getAll() {
        try {
            JSONArray data = new JSONArray(Utils.readText(fileName));
            return new JSONObject().put("message", "ok").put("data", data);
          } catch (IOException | JSONException e) {
            Utils.printStackTrace(e);
            return Utils.keyValueToJson("message", "Sin acceso a datos de productos", "error", e.getMessage());
          }
    }

    @Override
    public List<JSONObject> load() throws Exception {
        list = new ArrayList<>();
        String data = Utils.readText(fileName);
        JSONArray jsonArr = new JSONArray(data);

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            list.add(jsonObj);
        }

        return list;
    }

    @Override
    public JSONObject update(String id, String strJson) throws Exception {
       // buscar la persona que se debe actualizar
        CompraVenta compraVenta =getItem(id);
        JSONObject jsonR=compraVenta.toJSONObject();
        int i = list.indexOf(jsonR);

        if (jsonR == null) {
            String mensaje = String.format("No existe un %s con la identificación %s", clase.getSimpleName(), id);
            throw new NullPointerException(mensaje);
        }

        // crear un objeto JSON con las propiedades del objeto a actualizar
        JSONObject aux = compraVenta.toJSONObject();
        // iterar sobre las propiedades del objeto json recibido como argumento
        JSONObject json = new JSONObject(strJson);

        JSONArray propiedades = json.names();
        for (int k = 0; k < propiedades.length(); k++) {
            // asignar a aux los nuevos valores de las propiedades dadas
            String propiedad = propiedades.getString(k);
            Object valor = json.get(propiedad);
            aux.put(propiedad, valor);
        }

        // utilizar aux para actualizar la persona
        CompraVenta cv = clase.getConstructor(JSONObject.class).newInstance(aux);
        list.set(i, cv.toJSONObject());
        // actualizar el archivo
        Utils.writeJSON(list, fileName);
        // devolver la instancia con los cambios realizados
        return new JSONObject().put("message", "ok").put("data", cv.toJSONObject());
    }

    @Override
    public void refreshAll() throws Exception {
        list = new ArrayList<>();
        load();
    }

    @Override
    public JSONObject remove(String id) throws Exception {
        CompraVenta compraVenta = getItem(id);
        JSONObject json=compraVenta.toJSONObject();
        if(this.list.remove(json)){
          Utils.writeJSON(list, fileName);
        // devolver la instancia con los cambios realizados
          return new JSONObject().put("message", "ok").put("data", compraVenta.toJSONObject());
        }
        throw new Exception("No se pudo remover la compraVenta con el ID:"+id);
    }

    @Override
    public Class<CompraVenta> getDataType() {
        return (Class<CompraVenta>) this.clase;
    }
    
}
