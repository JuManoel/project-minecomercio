package edu.prog2.services;

import java.util.List;
import org.json.JSONObject;

public interface IService<T> {
  public JSONObject add(String strJson) throws Exception;

  public JSONObject get(int index);

  public JSONObject get(String id) throws Exception;

  public T getItem(String id) throws Exception;

  public JSONObject getAll();

  public List<T> load() throws Exception;

  public JSONObject update(String id, String strJson) throws Exception;

  public void refreshAll() throws Exception;

  public JSONObject remove(String id) throws Exception;

  /**
   * Permite conocer la clase del parámetro de tipo genérico T. Este método es
   * importante
   * sobre cuando se referencian subclases mediante superclaes y se desea conocer
   * de
   * forma fácil, el tipo con el que se está trabajando.
   * 
   * @return Una instancia de tipo Class que representa la clase del parámetro T
   */
  public Class<T> getDataType();
}
