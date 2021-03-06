package com.energyxxer.craftr.ui.explorer;

import com.energyxxer.craftr.global.TabManager;
import com.energyxxer.craftr.ui.explorer.base.ExplorerMaster;
import com.energyxxer.craftr.ui.explorer.base.elements.ExplorerElement;
import com.energyxxer.craftrlang.compiler.report.Notice;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * Created by User on 5/16/2017.
 */
public class NoticeItem extends ExplorerElement {
    private ExplorerMaster master;
    private Notice notice;

    private int x;

    public NoticeItem(ExplorerMaster master, Notice notice) {
        this.master = master;
        this.notice = notice;

        this.x = master.getInitialIndent();
    }

    public NoticeItem(NoticeGroupElement parent, Notice notice) {
        this.master = parent.master;
        this.notice = notice;

        this.x = (parent.indentation + 1) * master.getIndentPerLevel() + master.getInitialIndent();
    }

    @Override
    public void render(Graphics g) {
        int y = master.getOffsetY();
        master.getFlatList().add(this);

        int x = this.x + 23;

        g.setColor((this.rollover || this.selected) ? master.getColors().get("item.rollover.background") : master.getColors().get("item.background"));
        g.fillRect(0, master.getOffsetY(), master.getWidth(), master.getRowHeight());
        if(this.selected) {
            g.setColor(master.getColors().get("item.selected.background"));

            switch(master.getSelectionStyle()) {
                case "FULL": {
                    g.fillRect(0, master.getOffsetY(), master.getWidth(), master.getRowHeight());
                    break;
                }
                case "LINE_LEFT": {
                    g.fillRect(0, master.getOffsetY(), master.getSelectionLineThickness(), master.getRowHeight());
                    break;
                }
                case "LINE_RIGHT": {
                    g.fillRect(master.getWidth() - master.getSelectionLineThickness(), master.getOffsetY(), master.getSelectionLineThickness(), master.getRowHeight());
                    break;
                }
                case "LINE_TOP": {
                    g.fillRect(0, master.getOffsetY(), master.getWidth(), master.getSelectionLineThickness());
                    break;
                }
                case "LINE_BOTTOM": {
                    g.fillRect(0, master.getOffsetY() + master.getRowHeight() - master.getSelectionLineThickness(), master.getWidth(), master.getSelectionLineThickness());
                    break;
                }
            }
        }

        //File Icon
        {
            int margin = ((master.getRowHeight() - 16) / 2);
            g.drawImage(master.getAssets().get(notice.getType().name().toLowerCase()),x,y + margin,16, 16,new Color(0,0,0,0),null);
        }
        x += 25;

        //File Name

        if(this.selected) {
            g.setColor(master.getColors().get("item.selected.foreground"));
        } else if(this.rollover) {
            g.setColor(master.getColors().get("item.rollover.foreground"));
        } else {
            g.setColor(master.getColors().get("item.foreground"));
        }
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        g.drawString(notice.getMessage(), x, master.getOffsetY() + metrics.getAscent() + ((master.getRowHeight() - metrics.getHeight())/2));
        x += metrics.stringWidth(notice.getMessage());

        master.setOffsetY(master.getOffsetY() + master.getRowHeight());
        master.setContentWidth(Math.max(master.getWidth(), x));
        for(ExplorerElement i : children) {
            i.render(g);
        }
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public int getHeight() {
        return master.getRowHeight();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1 && !e.isControlDown() && e.getClickCount() % 2 == 0 && notice.getFilePath() != null) {
            TabManager.openTab(notice.getFilePath(), notice.getLocationIndex(), notice.getLocationLength());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            master.setSelected(this, e);
        } else if(e.getButton() == MouseEvent.BUTTON3) {
            /*if(!this.selected) master.setSelected(this, new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), 0, e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), MouseEvent.BUTTON1));
            StyledPopupMenu menu = this.generatePopup();
            menu.show(e.getComponent(), e.getX(), e.getY());*/
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
