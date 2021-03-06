package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by User on 12/14/2016.
 */
public class StyledPopupMenu extends JPopupMenu {

    private String namespace = null;

    public StyledPopupMenu() {
        this(null,null);
    }

    public StyledPopupMenu(String label) {
        this(label,null);
    }

    public StyledPopupMenu(String label, String namespace) {
        if(label != null) setLabel(label);
        if(namespace != null) this.setNamespace(namespace);

        ThemeChangeListener.addThemeChangeListener(t -> {
            if (this.namespace != null) {
                setBackground(t.getColor(new Color(215, 215, 215), this.namespace + ".menu.background", "General.menu.background"));
                int borderThickness = Math.max(t.getInteger(1,this.namespace + ".menu.border.thickness","General.menu.border.thickness"),0);
                setBorder(BorderFactory.createMatteBorder(borderThickness, borderThickness, borderThickness, borderThickness, t.getColor(new Color(200, 200, 200), this.namespace + ".menu.border.color", "General.menu.border.color")));
            } else {
                setBackground(t.getColor(new Color(215, 215, 215), "General.menu.background"));
                int borderThickness = Math.max(t.getInteger(1,"General.menu.border.thickness"),0);
                setBorder(BorderFactory.createMatteBorder(borderThickness, borderThickness, borderThickness, borderThickness ,t.getColor(new Color(200, 200, 200), "General.menu.border.color")));
            }
        });
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void addSeparator() {
        this.add(new StyledSeparator(namespace));
    }
}
