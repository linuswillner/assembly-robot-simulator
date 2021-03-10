package com.assemblyrobot.ui.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.val;

public abstract class TextFieldUtils {
  /**
   * Recursively returns all {@link TextField} nodes inside a given {@link Pane}.
   *
   * @param parent Parent {@link Pane} which to scan.
   * @return {@link TextField}[]
   */
  public static TextField[] getAllTextFields(Pane parent) {
    val fields = new ArrayList<TextField>();
    scanTextFields(parent, fields);
    return fields.toArray(new TextField[0]);
  }

  /**
   * Internal hierarchy walker for {@link TextFieldUtils#getAllTextFields(Pane)}. Updates the list
   * of {@link TextField}s in said method until the entire hierarchy has been traversed.
   *
   * @param parent {@link Pane}
   * @param fieldList {@link ArrayList}&lt;{@link TextField}&gt;
   */
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

  /**
   * Configures a {@link TextField} to only accept integer values.
   *
   * @param textField {@link TextField}
   */
  public static void configureIntegerOnlyTextField(TextField textField) {
    textField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
              }
            });
  }

  /**
   * Configures a {@link TextField} to only accept decimal values.
   *
   * @param textField {@link TextField}
   */
  public static void configureDecimalOnlyTextField(TextField textField) {
    textField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (!newValue.matches("\\d([.]\\d$)")) {
                textField.setText(newValue.replaceAll("[^\\d[.]]", ""));
              }

              if (!newValue.matches("[.]+")) {
                char[] chars = newValue.toCharArray();
                Set<Character> charSet = new LinkedHashSet<>();
                for (char c : chars) {
                  charSet.add(c);
                }

                StringBuilder sb = new StringBuilder();
                for (Character character : charSet) {
                  sb.append(character);
                }

                try {
                  textField.setText(sb.toString());
                } catch (StackOverflowError e) {
                  // Silently swallow these, as they are harmless and may occur from inputting extra
                  // dots at the end of the line, which is presently an unremediable issue
                }
              }
            });
  }
}
