package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;
import com.energyxxer.xswing.XTextField;

import java.awt.Color;
import java.awt.Font;

/**
 * Provides a text field that reacts to theme changes and
 * adjusts their own color. It also provides a namespace for
 * more specific paths on the theme file. If the namespace is
 * not specified, it defaults to the general style.
 */
public class StyledTextField extends XTextField {

    private String namespace = null;

    public StyledTextField() {
        this(null,null,-1);
    }

    public StyledTextField(String text) {
        this(text,null,-1);
    }

    public StyledTextField(String text, String namespace) {
        this(text,namespace,-1);
    }

    public StyledTextField(int columns) {
        this(null,null,columns);
    }

    public StyledTextField(int columns, String namespace) {
        this(null,namespace,columns);
    }

    public StyledTextField(String text, String namespace, int columns) {
        if(text != null) this.setText(text);
        if(namespace != null) this.setNamespace(namespace);
        if(columns >= 0) this.setColumns(columns);

        ThemeChangeListener.addThemeChangeListener(t -> {
            if(this.namespace != null) {
                setBackground       (t.getColor(new Color(220, 220, 220), this.namespace + ".textfield.background","General.textfield.background"));
                setForeground       (t.getColor(Color.BLACK, this.namespace + ".textfield.foreground","General.textfield.foreground","General.foreground"));
                setSelectionColor   (t.getColor(new Color(50, 100, 175), this.namespace + ".textfield.selection.background","General.textfield.selection.background"));
                setSelectedTextColor(t.getColor(getForeground(), this.namespace + ".textfield.selection.foreground","General.textfield.selection.foreground"));
                setBorder(t.getColor(new Color(200, 200, 200), this.namespace + ".textfield.border.color","General.textfield.border.color"),Math.max(t.getInteger(1,this.namespace + ".textfield.border.borderThickness","General.textfield.border.borderThickness"),0));
                setFont(new Font   (t.getString(this.namespace + ".textfield.font","General.textfield.font","General.font","default:Tahoma"),0,12));

                setDisabledTextColor(t.getColor(getForeground(), this.namespace + ".textfield.disabled.foreground","General.textfield.disabled.foreground"));
            } else {
                setBackground       (t.getColor(new Color(220, 220, 220), "General.textfield.background"));
                setForeground       (t.getColor(Color.BLACK, "General.textfield.foreground","General.foreground"));
                setSelectionColor   (t.getColor(new Color(50, 100, 175), "General.textfield.selection.background"));
                setSelectedTextColor(t.getColor(getForeground(), "General.textfield.selection.foreground"));
                setBorder(t.getColor(new Color(200, 200, 200), "General.textfield.border.color"),Math.max(t.getInteger(1,"General.textfield.border.borderThickness"),0));
                setFont(new Font   (t.getString("General.textfield.font","General.font","default:Tahoma"),0,12));

                setDisabledTextColor(t.getColor(getForeground(), "General.textfield.disabled.foreground"));
            }
        });
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return this.namespace;
    }
}
