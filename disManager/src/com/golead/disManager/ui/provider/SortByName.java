package com.golead.disManager.ui.provider;

import java.util.Comparator;

public class SortByName implements Comparator<Object> {
   public int compare(Object o1, Object o2) {
      String s1 = (String) o1;
      String s2 = (String) o2;
      return s1.compareTo(s2) * -1;
   }
}