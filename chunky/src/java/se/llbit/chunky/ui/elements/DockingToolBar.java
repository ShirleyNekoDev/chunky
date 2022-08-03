package se.llbit.chunky.ui.elements;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.EnumConverter;
import javafx.geometry.Pos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maximilian Stiede
 */
public class DockingToolBar extends Region implements Styleable {



  public final BooleanProperty expandedProperty() {
    return expanded;
  }
  private BooleanProperty expanded = new BooleanPropertyBase(true) {
    @Override protected void invalidated() {
      final boolean active = get();
//      pseudoClassStateChanged(PSEUDO_CLASS_EXPANDED,   active);
//      pseudoClassStateChanged(PSEUDO_CLASS_COLLAPSED, !active);
      notifyAccessibleAttributeChanged(AccessibleAttribute.EXPANDED);
    }

    @Override
    public Object getBean() {
      return DockingToolBar.this;
    }

    @Override
    public String getName() {
      return "expanded";
    }
  };
  public final void setExpanded(boolean value) {
    expandedProperty().set(value);
  }
  public final boolean isExpanded() {
    return expanded.get();
  }

  public ObjectProperty<Pos> alignmentProperty() {
    if (alignment == null) {
      alignment = new StyleableObjectProperty<>(Pos.TOP_LEFT) {
        @Override
        public void invalidated() {
          requestLayout();
        }

        @Override
        public Object getBean() {
          return DockingToolBar.this;
        }

        @Override
        public String getName() {
          return "alignment";
        }

        @Override
        public CssMetaData<DockingToolBar, Pos> getCssMetaData() {
          return StyleableProperties.ALIGNMENT;
        }
      };
    }
    return alignment;
  }

  private ObjectProperty<Pos> alignment;
  public final void setAlignment(Pos value) {
    alignmentProperty().set(value);
  }
  public final Pos getAlignment() {
    return alignment == null ? Pos.TOP_LEFT : alignment.get();
  }

  private static class StyleableProperties {
    private static final CssMetaData<DockingToolBar, Pos> ALIGNMENT =
      new CssMetaData<DockingToolBar, Pos>("-fx-alignment",
        new EnumConverter<>(Pos.class), Pos.TOP_LEFT){

        @Override
        public boolean isSettable(DockingToolBar node) {
          return node.alignment == null || !node.alignment.isBound();
        }

        @Override
        public StyleableProperty<Pos> getStyleableProperty(DockingToolBar node) {
          return (StyleableProperty<Pos>) node.alignmentProperty();
        }
      };

    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
    static {
      final List<CssMetaData<? extends Styleable, ?>> styleables =
        new ArrayList<>(Region.getClassCssMetaData());
      styleables.add(ALIGNMENT);
      STYLEABLES = Collections.unmodifiableList(styleables);
    }
  }

  public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
    return StyleableProperties.STYLEABLES;
  }
  @Override
  public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
    return getClassCssMetaData();
  }
}
