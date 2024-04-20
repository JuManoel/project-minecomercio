package edu.prog2.services;

import edu.prog2.helpers.Utils;
import edu.prog2.model.Producto;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductoService implements IService<Producto> {

  private List<Producto> list;
  private final String fileName;

  public ProductoService() throws JSONException, Exception {
    fileName = Utils.PATH + "Producto.json";

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
    Producto p = new Producto(json);
    if (list.contains(p)) {
      throw new ArrayStoreException(String.format("El producto %s ya existe", p.getDescripcion()));
    }

    if (list.add(p)) {
      Utils.writeJSON(list, fileName);
    }
    return new JSONObject().put("message", "ok").put("data", p.toJSONObject());
  }

  @Override
  public JSONObject get(int index) {
    return list.get(index).toJSONObject();
  }

  @Override
  public JSONObject get(String id) throws Exception {
    int i = list.indexOf(new Producto(id));
    return i > -1 ? get(i) : null;
  }

  @Override
  public Producto getItem(String id) throws Exception {
    int i = list.indexOf(new Producto(id));
    return i > -1 ? list.get(i) : null;
  }

  @Override
  public JSONObject getAll() {
    try {
      JSONArray data = new JSONArray(Utils.readText(fileName));
      return new JSONObject().put("message", "ok").put("data", data);
    } catch (Exception e) {
      Utils.printStackTrace(e);
      return Utils.keyValueToJson("message", "Sin acceso a datos de productos", "error", e.getMessage());
    }
  }

  @Override
  public final List<Producto> load() throws JSONException, Exception {
    list = new ArrayList<>();

    String data = Utils.readText(fileName);
    JSONArray jsonArr = new JSONArray(data);

    for (int i = 0; i < jsonArr.length(); i++) {
      JSONObject jsonObj = jsonArr.getJSONObject(i);
      list.add(new Producto(jsonObj));
    }

    return list;
  }

  @Override
  public JSONObject update(String id, String strJson) throws Exception {
    JSONObject json = new JSONObject(strJson);
    // buscar el producto que se debe actualizar
    Producto producto = getItem(id);
    int i = list.indexOf(producto);

    if (producto == null) {
      throw new NullPointerException("No se encontró el producto " + id);
    }

    if (json.has("descripcion")) {
      // sólo permitir el cambio de descripcion si no existe otro producto con la nueva descripcion
      String descripcion = json.getString("descripcion");
      if (!descripcion.equalsIgnoreCase(producto.getDescripcion())) {
        Producto aux = new Producto();
        aux.setDescripcion(descripcion);
        // se generará una excepción si existe un producto con la descripción que se pretende utilizar para el cambio
        list.contains(aux);
      }
    }

    // crear un objeto JSON con las propiedades del objeto a actualizar
    JSONObject aux = producto.toJSONObject();
    // iterar sobre las propiedades del objeto json recibido como argumento
    JSONArray propiedades = json.names();
    for (int k = 0; k < propiedades.length(); k++) {
      // asignar a aux los nuevos valores de las propiedades dadas
      String propiedad = propiedades.getString(k);
      Object valor = json.get(propiedad);
      aux.put(propiedad, valor);
    }

    // utilizar aux para actualizar el producto
    producto = new Producto(aux);
    list.set(i, producto);
    // actualizar el archivo de productos
    Utils.writeJSON(list, fileName);
    // devolver el producto con los cambios realizados
    return new JSONObject().put("message", "ok").put("data", producto.toJSONObject());
  }

  @Override
  public void refreshAll() throws JSONException, Exception {
    list = new ArrayList<>();
    load();
  }

  @Override
  public JSONObject remove(String id) throws Exception {
    Producto producto = getItem(id);
    if(this.list.remove(producto)){
      Utils.writeJSON(list, fileName);
    // devolver la instancia con los cambios realizados
      return new JSONObject().put("message", "ok").put("data", producto.toJSONObject());
    }
    throw new Exception("No se pudo remover la persona con el ID:"+id);  }

    @Override
    public Class<Producto> getDataType() {
      return Producto.class;
    }

}
