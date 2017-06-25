package com.golead.disManager.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ResultSetMap implements Map<String, Object> {
   private Map<String, Object> map;

   public ResultSetMap() {
      this.map = new HashMap<String, Object>();
   }

   public ResultSetMap(Map<String, Object> map) {
      this.map = map;
   }

   @Override
   public Object get(Object key) {
      String k = key.toString().toLowerCase();
      return map.get(k);
   }

   @Override
   public void clear() {

      map.clear();
   }

   @Override
   public boolean containsKey(Object key) {

      return map.containsKey(key);
   }

   @Override
   public boolean containsValue(Object value) {

      return map.containsValue(value);
   }

   @Override
   public Set<java.util.Map.Entry<String, Object>> entrySet() {

      return map.entrySet();
   }

   @Override
   public boolean isEmpty() {

      return map.isEmpty();
   }

   @Override
   public Set<String> keySet() {

      return map.keySet();
   }

   @Override
   public Object put(String key, Object value) {

      return map.put(key, value);
   }

   @Override
   public void putAll(Map<? extends String, ? extends Object> m) {

      map.putAll(m);
   }

   @Override
   public Object remove(Object key) {

      return map.remove(key);
   }

   @Override
   public int size() {

      return map.size();
   }

   @Override
   public Collection<Object> values() {

      return map.values();
   }
}
