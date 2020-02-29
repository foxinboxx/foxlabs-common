package org.foxlabs.util;

@FunctionalInterface
public interface Buildable<T> {
  
  T build();
  
}
