package edu.prog2.services;

import java.util.List;
import org.json.JSONObject;

public interface Service<T> {
  public JSONObject add(String json) throws Exception;

  public JSONObject get(int index);

  public JSONObject get(String id) throws Exception;

  public T getItem(String id) throws Exception;

  public JSONObject getAll();

  public List<T> load() throws Exception;

  public JSONObject update(String id, String json) throws Exception;

  public void refreshAll() throws Exception;

  public JSONObject remove(String id) throws Exception;

  public String endPoint();

}
