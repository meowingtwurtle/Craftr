package com.energyxxer.craftr.ui.editor.inspector;

import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.Tab;
import com.energyxxer.craftr.ui.editor.CraftrEditorComponent;
import com.energyxxer.craftrlang.compiler.lexical_analysis.Scanner;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenStream;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.util.StringBounds;

import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.File;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Created by User on 1/1/2017.
 */
public class Inspector implements Highlighter.HighlightPainter {

    private volatile ArrayList<InspectionItem> items = new ArrayList<>();

    private Tab tab;
    private CraftrEditorComponent editor;

    public Inspector(Tab tab, CraftrEditorComponent editor) {
        this.tab = tab;
        this.editor = editor;

        try
        {
            editor.getHighlighter().addHighlight(0, 0, this);
        }
        catch(BadLocationException ble) {}
    }

    public void inspect(TokenStream ts) {
        items.clear();

        for(InspectionStructureMatch inspect : InspectionStructures.getAll()) {
            ArrayList<TokenPattern<?>> matches = ts.search(inspect);
            for(TokenPattern<?> match : matches) {
                items.add(new InspectionItem(inspect.type, inspect.name, match.getStringBounds()));
            }
        }
        editor.repaint();
    }

    @Override
    public void paint(Graphics g, int p0, int p1, Shape graphicBounds, JTextComponent c) {
        try {
            for (InspectionItem item : items) {

                g.setColor(CraftrWindow.getTheme().getColor(item.type.colorKey));

                try {

                    StringBounds bounds = item.bounds;

                    for (int l = bounds.start.line; l <= bounds.end.line; l++) {
                        Rectangle rectangle;
                        if (l == bounds.start.line) {
                            rectangle = editor.modelToView(bounds.start.index);
                            if (bounds.start.line == bounds.end.line) {
                                //One line only
                                rectangle.width = editor.modelToView(bounds.end.index).x - rectangle.x;
                            } else {
                                rectangle.width = c.getWidth() - rectangle.x;
                            }
                        } else if (l == bounds.end.line) {
                            rectangle = editor.modelToView(bounds.end.index);
                            rectangle.width = rectangle.x - c.modelToView(0).x;
                            rectangle.x = c.modelToView(0).x; //0
                        } else {
                            rectangle = editor.modelToView(bounds.start.index);
                            rectangle.x = c.modelToView(0).x; //0
                            rectangle.y += rectangle.height * (l - bounds.start.line);
                            rectangle.width = c.getWidth();
                        }

                        if (item.type.line) {
                            for (int x = rectangle.x; x < rectangle.x + rectangle.width; x += 4) {
                                g.drawLine(x, rectangle.y + rectangle.height, x + 2, rectangle.y + rectangle.height - 2);
                                g.drawLine(x + 2, rectangle.y + rectangle.height - 2, x + 4, rectangle.y + rectangle.height);
                            }
                        } else {
                            g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                        }
                    }
                } catch (BadLocationException e) {
                    //e.printStackTrace();
                }
            }
        } catch(ConcurrentModificationException e) {}
    }

    public void insertNotices(ArrayList<Notice> notices) {
        for(Notice n : notices) {
            InspectionType type = InspectionType.SUGGESTION;
            switch(n.getType()) {
                case ERROR: {
                    type = InspectionType.ERROR;
                    break;
                }
                case WARNING: {
                    type = InspectionType.WARNING;
                    break;
                }
            }
            InspectionItem item = new InspectionItem(type, n.getMessage(), new StringBounds(editor.getLocationForOffset(n.getLocationIndex()), editor.getLocationForOffset(n.getLocationIndex() + n.getLocationLength())));
            System.out.println("Created item: " + item);
            items.add(item);
        }
    }
}
