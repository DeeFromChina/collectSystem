package com.golead.dasService.filter;

import java.io.File;
import java.io.FilenameFilter;

public class NameFilter implements FilenameFilter {
   private String type;

   public NameFilter(String type) {
      this.type = type;
   }

   public boolean accept(File dir, String name) {
      return name.endsWith(type);
   }
}
