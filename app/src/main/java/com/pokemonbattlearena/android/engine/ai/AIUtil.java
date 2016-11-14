package com.pokemonbattlearena.android.engine.ai;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 11/13/16.
 */

public class AIUtil {
  public static <T> List<T> nullToEmpty(final List<T> l) {
    if (l == null){
      return new ArrayList<>();
    } else {
      return l;
    }
  }
}
