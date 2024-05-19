package edu.prog2.services;

import edu.prog2.helpers.Utils;
import edu.prog2.model.Cliente;
import edu.prog2.model.Persona;
import edu.prog2.model.Provedor;
import edu.prog2.model.Vendedor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PersonaService implements IService<Persona> {

  private List<Persona> list;
  private final String fileName;
  private final Class<? extends Persona> clase;

  public PersonaService(Class<? extends Persona> clase) throws Exception {
    /*
     * Constructor de Persona Service
     * Class<? extends Persona> clase => es la clase de la persoan que estamos
     * trabajando con
     * No devuelve nada
     * Inicializa el atributo clase, list y filename
     * todos eses atributos dependen del tipo de persona que se ingresa
     */
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
    /*
     * add adiciona la persona en un json
     * String strJson => el json que contiene todos los dados de las personas
     */
    // converter la string para un json object
    JSONObject json = new JSONObject(strJson);
    // creamos un id aleatorio
    json.put("id", Utils.getRandomKey(5));
    // generamos una nueva persona p
    Persona p = clase.getConstructor(JSONObject.class).newInstance(json);
    // encriptamos el password
    p.setPassword(Utils.MD5(p.getPassword()));

    // validamos si la persona ya no existe
    if (list.contains(p)) {
      throw new ArrayStoreException(String.format("El %s con ID %s ya existe", clase.getSimpleName(), p.getId()));
    }
    // adicionamos la persona
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
    Persona persona = this.clase.getConstructor(String.class).newInstance(id);
    int i = list.indexOf(persona);
    return i > -1 ? get(i) : null;
  }

  @Override
  public Persona getItem(String id) throws Exception {
    JSONObject json = get(id);
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
  public final List<Persona> load() throws Exception {
    list = new ArrayList<>();

    String data = Utils.readText(fileName);
    JSONArray jsonArr = new JSONArray(data);

    for (int i = 0; i < jsonArr.length(); i++) {
      JSONObject jsonObj = jsonArr.getJSONObject(i);
      list.add(this.clase.getConstructor(JSONObject.class).newInstance(jsonObj));
    }

    return list;
  }

  @Override
  public JSONObject update(String id, String strJson) throws Exception {
    /*
     * update, actualiza el json cambiando los valores de la persona
     */
    // coje la persona por el id
    Persona persona = getItem(id);
    // coje el index
    int i = list.indexOf(persona);

    if (persona == null) {
      String mensaje = String.format("No existe un %s con la identificación %s", clase.getSimpleName(), id);
      throw new NullPointerException(mensaje);
    }

    // crear un objeto JSON con las propiedades del objeto a actualizar
    JSONObject aux = persona.toJSONObject();
    // iterar sobre las propiedades del objeto json recibido como argumento
    JSONObject json = new JSONObject(strJson);
    if (json.has("password")) {
      // sólo asignar la contraseña entrante si es distinta a la actual
      String newPassword = json.getString("password");
      if (!aux.getString("password").equals(newPassword)) {
        json.put("password", Utils.MD5(newPassword));
      }
    }

    JSONArray propiedades = json.names();
    for (int k = 0; k < propiedades.length(); k++) {
      // asignar a aux los nuevos valores de las propiedades dadas
      String propiedad = propiedades.getString(k);
      Object valor = json.get(propiedad);
      aux.put(propiedad, valor);
    }

    // utilizar aux para actualizar la persona
    Persona p = clase.getConstructor(JSONObject.class).newInstance(aux);
    list.set(i, p);
    // actualizar el archivo
    Utils.writeJSON(list, fileName);
    // devolver la instancia con los cambios realizados
    return new JSONObject().put("message", "ok").put("data", p.toJSONObject());
  }

  @Override
  public void refreshAll() throws Exception {
    list = new ArrayList<>();
    load();
  }

  private ArrayList<CompraVentaService> compraVenta() throws Exception {
    /*
     * compraVenta retorna una lista de
     */
    ArrayList<CompraVentaService> cv = new ArrayList<>();
    if (this.clase != Vendedor.class) {
      cv.add(new CompraVentaService(this.clase));
    } else {
      cv.add(new CompraVentaService(Cliente.class));
      cv.add(new CompraVentaService(Provedor.class));
    }
    return cv;
  }

  private boolean canRemove(ArrayList<CompraVentaService> cv, String id) {
    for (CompraVentaService compraVentaService : cv) {
      JSONArray jsonArray = compraVentaService.getAll().getJSONArray("data");
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject pR = jsonArray.getJSONObject(i).getJSONObject(this.clase.getSimpleName().toLowerCase());
        String idC = pR.getString("id");
        if (id.equals(idC)) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public JSONObject remove(String id) throws Exception {
    Persona persona = getItem(id);
    ArrayList<CompraVentaService> cv = compraVenta();
    if (!canRemove(cv, id)) {
      throw new Exception("No se pudo remover la persona con el ID:" + id + " esta registrada en una Compra o Venta");
    }
    if (this.list.remove(persona)) {
      Utils.writeJSON(list, fileName);
      // devolver la instancia con los cambios realizados
      return new JSONObject().put("message", "ok").put("data", persona.toJSONObject());
    }
    throw new Exception("No se pudo remover la persona con el ID:" + id);

  }

  @SuppressWarnings("unchecked")
  @Override
  public Class<Persona> getDataType() {
    return (Class<Persona>) this.clase;
  }

}
