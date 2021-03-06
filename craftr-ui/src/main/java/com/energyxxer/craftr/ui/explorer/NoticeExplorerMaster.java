package com.energyxxer.craftr.ui.explorer;

import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.ui.explorer.base.ExplorerMaster;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;
import com.energyxxer.craftrlang.compiler.report.Notice;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 5/16/2017.
 */
public class NoticeExplorerMaster extends ExplorerMaster {

    public NoticeExplorerMaster() {
        ThemeChangeListener.addThemeChangeListener(t -> {
            colors.put("background",t.getColor(Color.WHITE, "Explorer.background"));
            colors.put("item.background",t.getColor(new Color(0,0,0,0), "Explorer.item.background"));
            colors.put("item.foreground",t.getColor(Color.BLACK, "Explorer.item.foreground","General.foreground"));
            colors.put("item.selected.background",t.getColor(Color.BLUE, "Explorer.item.selected.background","Explorer.item.background"));
            colors.put("item.selected.foreground",t.getColor(Color.BLACK, "Explorer.item.selected.foreground","Explorer.item.rollover.foreground","Explorer.item.foreground","General.foreground"));
            colors.put("item.rollover.background",t.getColor(new Color(0,0,0,0), "Explorer.item.rollover.background","Explorer.item.background"));
            colors.put("item.rollover.foreground",t.getColor(Color.BLACK, "Explorer.item.rollover.foreground","Explorer.item.foreground","General.foreground"));

            rowHeight = Math.max(t.getInteger(20,"Explorer.item.height"), 1);
            indentPerLevel = Math.max(t.getInteger(20,"Explorer.item.indent"), 0);
            initialIndent = Math.max(t.getInteger(0,"Explorer.item.initialIndent"), 0);

            selectionStyle = t.getString("Explorer.item.selectionStyle","default:FULL");
            selectionLineThickness = Math.max(t.getInteger(2,"Explorer.item.selectionLineThickness"), 0);

            assets.put("expand", Commons.getIcon("expand").getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            assets.put("collapse",Commons.getIcon("collapse").getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            assets.put("info",Commons.getIcon("info").getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            assets.put("warning",Commons.getIcon("warn").getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            assets.put("error",Commons.getIcon("error").getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        });
    }

    public void addNotice(Notice n) {
        this.children.add(new NoticeItem(this, n));
    }

    public void addNoticeGroup(String label, List<Notice> notices) {
        this.children.add(new NoticeGroupElement(this, label, notices));
    }

    public void clear() {
        this.children.clear();

        this.repaint();
    }

    public void setNotices(HashMap<String, ArrayList<Notice>> map) {
        this.children.clear();

        if(map.containsKey(null)) {
            ArrayList<Notice> standalones = map.get(null);
            standalones.forEach(this::addNotice);
        }
        map.keySet().forEach(k -> {
            if(k != null) this.addNoticeGroup(k, map.get(k));
        });

        this.repaint();
    }
}
