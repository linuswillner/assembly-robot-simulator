package com.assemblyrobot.ui.utils;

import java.util.ArrayList;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.val;

public abstract class TextFieldUtils {
  public static TextField[] getAllTextFields(Pane parent) {
    val fields = new ArrayList<TextField>();
    scanTextFields(parent, fields);
    return fields.toArray(new TextField[0]);
  }

  private static void scanTextFields(Pane parent, ArrayList<TextField> fieldList) {
    parent
        .getChildren()
        .forEach(
            node -> {
              if (node instanceof TextField) {
                fieldList.add((TextField) node);
              } else if (node instanceof Pane) {
                scanTextFields((Pane) node, fieldList); // Recurse
              }
            });
  }
}
