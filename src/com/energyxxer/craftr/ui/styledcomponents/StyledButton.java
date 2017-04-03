package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.ui.components.XButton;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * Provides a button that reacts to theme changes and
 * adjusts their own color. It also provides a namespace for
 * more specific paths on the theme file. If the namespace is
 * not specified, it defaults to the general style.
 */
public class StyledButton extends XButton {

    private String namespace = null;

    public StyledButton(String label) {
        this(label, null, null);
    }

    public StyledButton(String label, String namespace) {
        this(label, namespace, null);
    }

    public StyledButton(String label, ImageIcon icon) {
        this(label, null, icon);
    }

    public StyledButton(String label, String namespace, ImageIcon icon) {
        super(label, icon);
        if(namespace != null) this.setNamespace(namespace);

        ThemeChangeListener.addThemeChangeListener(t -> {
            if(this.namespace != null) {
                setBackground       (t.getColor(this.namespace + ".button.background",          t.getColor("General.button.background",         new Color(215, 215, 215))));
                setForeground       (t.getColor(this.namespace + ".button.foreground",          t.getColor("General.button.foreground",         t.getColor("General.foreground", Color.BLACK))));
                setBorder(t.getColor(this.namespace + ".button.border.color",              t.getColor("General.button.border.color",             new Color(200, 200, 200))), Math.max(t.getInteger(1,this.namespace + ".button.border.thickness", "General.button.border.thickness"),0));
                setRolloverColor    (t.getColor(this.namespace + ".button.hover.background",    t.getColor("General.button.hover.background",   new Color(200, 202, 205))));
                setPressedColor     (t.getColor(this.namespace + ".button.pressed.background",  t.getColor("General.button.pressed.background", Color.WHITE)));
                setFont(new Font   (t.getString(this.namespace + ".button.font","General.button.font","General.font","default:Tahoma"),
                        (t.getBoolean(false,this.namespace + ".button.bold", "General.button.bold") ? Font.BOLD : Font.PLAIN) +
                                (t.getBoolean(false,this.namespace + ".button.italic","General.button.italic") ? Font.ITALIC : Font.PLAIN),12));
            } else {
                setBackground       (t.getColor("General.button.background",         new Color(215, 215, 215)));
                setForeground       (t.getColor("General.button.foreground",         t.getColor("General.foreground", Color.BLACK)));
                setBorder(t.getColor("General.button.border.color",             new Color(200, 200, 200)),Math.max(t.getInteger(1,"General.button.border.thickness"),0));
                setRolloverColor    (t.getColor("General.button.hover.background",   new Color(200, 202, 205)));
                setPressedColor     (t.getColor("General.button.pressed.background", Color.WHITE));
                setFont(new Font   (t.getString("General.button.font","General.font","default:Tahoma"),
                        (t.getBoolean(false,"General.button.bold") ? Font.BOLD : Font.PLAIN) +
                                (t.getBoolean(false,"General.button.italic") ? Font.ITALIC : Font.PLAIN),
                        12));
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
