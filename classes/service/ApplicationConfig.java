package service;

import java.util.*;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("resources") //localhost - war file name - this name("resources") - class - method name
public class ApplicationConfig extends Application{
   
   @Override
   public Set<Class<?>> getClasses(){
      return getRestResourceClasses();
   }
   
   private Set<Class<?>> getRestResourceClasses(){
      Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
      resources.add(service.Service.class);
      return resources;
   }
}