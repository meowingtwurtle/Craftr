package com.energyxxer.craftr.ui.explorer;

import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.global.ProjectManager;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 2/7/2017.
 */
public class ExplorerMaster extends JPanel implements MouseListener {

    private final File root;

    private HashMap<ExplorerFlag, Boolean> explorerFlags = new HashMap<>();

    private ArrayList<ExplorerItem> children = new ArrayList<>();
    private ArrayList<ExplorerItem> selectedItems = new ArrayList<>();

    ArrayList<ExplorerItem> flatList = new ArrayList<>();
    int width = 0;
    int offsetY = 0;

    ArrayList<String> openDirectories = new ArrayList<>();

    static HashMap<String, Color> colors = new HashMap<>();
    static HashMap<String, Image> assets = new HashMap<>();

    static final int ROW_HEIGHT = 20;
    static final int INDENT_IN_PIXELS = 20;

    public ExplorerMaster(File root) {
        this.root = root;
        this.addMouseListener(this);

        explorerFlags.put(ExplorerFlag.FLATTEN_EMPTY_PACKAGES, true);
        explorerFlags.put(ExplorerFlag.SHOW_PROJECT_FILES, true);

        refresh();
    }

    public void refresh() {
        ProjectManager.loadWorkspace();

        ArrayList<String> copy = new ArrayList<>();
        copy.addAll(openDirectories);
        refresh(copy);
    }

    private void refresh(ArrayList<String> toOpen) {
        children.clear();
        flatList.clear();
        openDirectories.clear();

        File[] subfiles = root.listFiles();
        if(subfiles == null) return;

        ArrayList<File> subfiles1 = new ArrayList<>();

        for(File f : subfiles) {
            if(f.isDirectory()) {
                this.children.add(new ExplorerItem(this, f, toOpen));
            } else {
                subfiles1.add(f);
            }
        }
        for(File f : subfiles1) {
            this.children.add(new ExplorerItem(this, f, toOpen));
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        offsetY = 0;
        flatList.clear();
        g.setColor(colors.get("background"));
        g.fillRect(0,0,this.getWidth(), this.getHeight());
        for(ExplorerItem i : children) {
            i.render(g);
        }

        if(this.getPreferredSize().height != offsetY + ROW_HEIGHT) {
            this.setPreferredSize(new Dimension(width, offsetY + ROW_HEIGHT));
            this.setSize(new Dimension(width, offsetY + ROW_HEIGHT));
            this.getParent().revalidate();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int index = e.getY()/ ROW_HEIGHT;
        if(index >= 0 && index < flatList.size()) {
            flatList.get(index).mouseClicked(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int index = e.getY()/ ROW_HEIGHT;
        if(index >= 0 && index < flatList.size()) {
            flatList.get(index).mousePressed(e);
        } else if(e.getButton() == MouseEvent.BUTTON1) {
            clearSelected();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int index = e.getY()/ ROW_HEIGHT;
        if(index >= 0 && index < flatList.size()) {
            flatList.get(index).mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        int index = e.getY()/ ROW_HEIGHT;
        if(index >= 0 && index < flatList.size()) {
            flatList.get(index).mouseEntered(e);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        int index = e.getY()/ ROW_HEIGHT;
        if(index >= 0 && index < flatList.size()) {
            flatList.get(index).mouseExited(e);
        }
    }

    private void clearSelected() {
        for(ExplorerItem item : selectedItems) {
            item.selected = false;
        }
        selectedItems.clear();
    }

    private void addSelected(ExplorerItem item) {
        this.addSelected(item, true);
    }

    private void addSelected(ExplorerItem item, boolean invert) {
        if(!selectedItems.contains(item)) {
            item.selected = true;
            selectedItems.add(item);
        } else if(invert) {
            item.selected = false;
            selectedItems.remove(item);
        }
    }

    void setSelected(ExplorerItem item, MouseEvent e) {
        ExplorerItem lastItem = null;
        if(this.selectedItems.size() > 0) lastItem = this.selectedItems.get(this.selectedItems.size()-1);
        if(!e.isControlDown()) {
            clearSelected();
        }
        if(e.isShiftDown() && lastItem != null) {
            int startIndex = flatList.indexOf(lastItem);
            int endIndex = flatList.indexOf(item);

            int start = Math.min(startIndex, endIndex);
            int end = Math.max(startIndex, endIndex);
            for(int i = start; i <= end; i++) {
                addSelected(flatList.get(i), false);
            }
        } else {
            addSelected(item);
        }
        repaint();
    }

    public List<String> getSelectedFiles() {
        List<String> list = new ArrayList<>();
        selectedItems.forEach(item -> list.add(item.path));
        return list;
    }

    public boolean getFlag(ExplorerFlag flag) {
        return this.explorerFlags.get(flag);
    }

    static {
        ThemeChangeListener.addThemeChangeListener(t -> {
            colors.put("background",t.getColor("Explorer.background",Color.WHITE));
            colors.put("item.foreground",t.getColor("Explorer.item.foreground",t.getColor("General.foreground",Color.BLACK)));
            colors.put("item.selected.background",t.getColor("Explorer.item.selected.background",Color.BLUE));

            assets.put("expand",Commons.getIcon("expand").getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            assets.put("collapse",Commons.getIcon("collapse").getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        });
    }
}