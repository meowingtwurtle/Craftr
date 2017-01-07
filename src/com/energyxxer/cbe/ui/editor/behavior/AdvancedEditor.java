package com.energyxxer.cbe.ui.editor.behavior;

import com.energyxxer.cbe.global.Commons;
import com.energyxxer.cbe.ui.editor.behavior.caret.EditorCaret;
import com.energyxxer.cbe.ui.editor.behavior.editmanager.CompoundEdit;
import com.energyxxer.cbe.ui.editor.behavior.editmanager.Edit;
import com.energyxxer.cbe.ui.editor.behavior.editmanager.EditManager;
import com.energyxxer.cbe.util.StringLocation;
import com.energyxxer.cbe.util.StringUtil;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by User on 1/5/2017.
 */
public class AdvancedEditor extends JTextPane implements KeyListener, CaretListener {

    private EditorCaret caret;

    private EditManager editManager = new EditManager(this);

    {
        this.addKeyListener(this);
        this.addCaretListener(this);

        this.setCaret(this.caret = new EditorCaret(this));

        //this.getInputMap().setParent(null);
        this.setInputMap(JComponent.WHEN_FOCUSED,new InputMap());

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK),"undo");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK),"redo");

        this.getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editManager.undo();
            }
        });
        this.getActionMap().put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editManager.redo();
            }
        });
    }

    public AdvancedEditor() {
    }

    public AdvancedEditor(StyledDocument doc) {
        super(doc);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        e.consume();
        if(e.getKeyChar() == '`') {
            caret.pushFrom(0,-1);
        } else
        if(!e.isControlDown() && !Commons.isSpecialCharacter(e.getKeyChar())) {
            editManager.insertEdit(new Edit("" + e.getKeyChar(), caret.getFlatLocations()));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_TAB) {
            e.consume();

            ArrayList<Integer> locations = new ArrayList<>(caret.getFlatLocations());
            int characterDrift = 0;

            CompoundEdit insertTabulation = new CompoundEdit();

            for(int i = 0; i < locations.size()-1; i += 2) {
                int d = locations.get(i) + characterDrift;
                int spaces = 4 - ((getLocationForOffset(d).column-1) % 4);
                spaces = (spaces > 0) ? spaces : 4;
                characterDrift += spaces;
                String str = StringUtil.repeat(" ", spaces);
                insertTabulation.appendEdit(new Edit(str, Arrays.asList(d,d)));
            }
            editManager.insertEdit(insertTabulation);
        } else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            e.consume();

            String text = "";
            try {
                text = getDocument().getText(0,getDocument().getLength());
            } catch(BadLocationException ble) {
                ble.printStackTrace();
            }

            ArrayList<Integer> locations = new ArrayList<>(caret.getFlatLocations());

            for(int i = 0; i < locations.size()-1; i += 2) {
                StringLocation loc = getLocationForOffset(locations.get(i));
                int end = loc.index;
                try {
                    end = Utilities.getRowEnd(this, loc.index);
                } catch(BadLocationException ble) {}
                String currentLine = text.substring(loc.index-(loc.column-1),end);
                if(currentLine.trim().length() == 0 && currentLine.length()%4 == 0) {
                    locations.set(i, Math.max(0,locations.get(i)-loc.column));
                } else {
                    locations.set(i, Math.max(0,locations.get(i)-1));
                }
            }
            editManager.insertEdit(new Edit("",locations));
        } else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            e.consume();

            String text = "";
            try {
                text = getDocument().getText(0,getDocument().getLength());
            } catch(BadLocationException ble) {
                ble.printStackTrace();
            }

            ArrayList<Integer> locations = new ArrayList<>(caret.getFlatLocations());
            int characterDrift = 0;

            CompoundEdit insertNewline = new CompoundEdit();

            for(int i = 0; i < locations.size()-1; i += 2) {
                int d = locations.get(i) + characterDrift;
                String str = "\n";

                StringLocation loc = getLocationForOffset(d);

                String currentLine = text.substring(loc.index-(loc.column-1),loc.index);
                int spaces = 0;
                for(int j = 0; j < currentLine.length(); j++) {
                    if(currentLine.charAt(j) == ' ') spaces++; else break;
                }
                int tabs = spaces/4;

                str += StringUtil.repeat("    ", tabs + ((currentLine.trim().endsWith("{")) ? 1 : 0));

                insertNewline.appendEdit(new Edit(str, Arrays.asList(d, locations.get(i+1)+characterDrift)));
            }

            editManager.insertEdit(insertNewline);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void caretUpdate(CaretEvent e) {

    }

    public StringLocation getLocationForOffset(int index) {
        try {
            int line = (index == 0) ? 1 : 0;
            try {
                int offset = index;
                while(offset > 0) {
                    int rs = Utilities.getRowStart(this, offset);
                    if(rs < 0) {
                        line = 1;
                        break;
                    }
                    offset = rs - 1;
                    line++;
                }
            } catch(BadLocationException ble) {
                ble.printStackTrace();
            }
            int column;
            int rs = Utilities.getRowStart(this, index);
            column = (rs >= 0) ? index - rs + 1 : 1;
            return new StringLocation(index, line, column);
        } catch(BadLocationException e) {
            return null;
        }
    }

    protected String getCaretInfo() {
        return caret.getCaretInfo();
    }

    @Override
    public EditorCaret getCaret() {
        return caret;
    }

    //Delegates and deprecated methods

    @Deprecated
    public void replaceSelection(String content) {
        super.replaceSelection(content);
    }

    @Deprecated
    public void moveCaretPosition(int pos) {
        super.moveCaretPosition(pos);
    }

    @Override
    public void setCaretPosition(int position) {
        caret.setPosition(position);
    }

    @Deprecated
    public int getCaretPosition() {
        return super.getCaretPosition();
    }

    @Deprecated
    public String getSelectedText() {
        return super.getSelectedText();
    }

    @Deprecated
    public int getSelectionStart() {
        return super.getSelectionStart();
    }

    @Deprecated
    public void setSelectionStart(int selectionStart) {
        super.setSelectionStart(selectionStart);
    }

    @Deprecated
    public int getSelectionEnd() {
        return super.getSelectionEnd();
    }

    @Deprecated
    public void setSelectionEnd(int selectionEnd) {
        super.setSelectionEnd(selectionEnd);
    }

    @Deprecated
    public void select(int selectionStart, int selectionEnd) {
        super.select(selectionStart, selectionEnd);
    }

    @Deprecated
    public void selectAll() {
        super.selectAll();
    }
}