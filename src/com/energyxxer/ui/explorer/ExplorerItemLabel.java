package com.energyxxer.ui.explorer;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.energyxxer.cbe.Preferences;
import com.energyxxer.cbe.TabManager;
import com.energyxxer.cbe.Window;
import com.energyxxer.ui.common.MenuItems;
import com.energyxxer.ui.common.MenuItems.FileMenuItem;
import com.energyxxer.util.FileUtil;
import com.energyxxer.util.ImageManager;
import com.energyxxer.util.StringPrompt;
import com.energyxxer.util.StringUtil;
import com.energyxxer.util.StringValidator;

/**
 * Non-recursive label for an explorer item.
 * */
public class ExplorerItemLabel extends JLabel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3655326885011257489L;
	
	public boolean rollover = false;
	public boolean selected = false;

	public static Color rollover_background = new Color(240,245,255);
	public static Color rollover_border = new Color(200,220,230);

	public static Color selected_background = new Color(200,220,230);
	public static Color selected_border = new Color(180,200,210);
	
	public ExplorerItem parent;
	
	public ExplorerItemLabel(File file,ExplorerItem parent) {
		super(file.getName());
		this.parent = parent;
		updateLabel();
		this.addMouseListener(this);
		this.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
	}
	
	public void updateLabel() {
		File file = new File(parent.path);
		setText(file.getName());
		if(file.isDirectory()) {
			this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/folder.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			try {
				File workspaceFile = new File(Preferences.get("workspace_dir"));
				if(file.getParentFile().getCanonicalPath() == workspaceFile.getCanonicalPath()) {
					this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/project.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				}
			} catch (IOException e) {
				e.printStackTrace(new PrintWriter(Window.consoleout));
			}
		} else {
			if(file.getName().endsWith(".mcbe")) {
				this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/entity.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			} else if(file.getName().endsWith(".mcbi")) {
				this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/item.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			} else {
				this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/file.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			}
		}
		revalidate();
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		
		if(selected) {
			g2.setColor(selected_border);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			GradientPaint grad = new GradientPaint(this.getWidth()/2,0,Color.WHITE, this.getWidth()/2, this.getHeight(), selected_background);
			
			g2.setPaint(grad);
			g2.fillRect(1,1,this.getWidth()-2,this.getHeight()-2);
			
			g2.setPaint(null);
			
			g2.setColor(Color.WHITE);
			g2.fillRect(0,0,2,2);
			g2.fillRect(0,this.getHeight()-2,2,2);
			g2.fillRect(this.getWidth()-2,0,2,2);
			g2.fillRect(this.getWidth()-2,this.getHeight()-2,2,2);
			
			g2.setColor(selected_border);
			g2.fillRect(1,1,1,1);
			g2.fillRect(1,this.getHeight()-2,1,1);
			g2.fillRect(this.getWidth()-2,1,1,1);
			g2.fillRect(this.getWidth()-2,this.getHeight()-2,1,1);
		} else if(rollover) {
			g2.setColor(rollover_border);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			g2.setColor(rollover_background);
			g2.fillRect(1, 1, this.getWidth()-2, this.getHeight()-2);
			g2.setColor(Color.WHITE);
			g2.fillRect(0,0,2,2);
			g2.fillRect(0,this.getHeight()-2,2,2);
			g2.fillRect(this.getWidth()-2,0,2,2);
			g2.fillRect(this.getWidth()-2,this.getHeight()-2,2,2);
			
			g2.setColor(rollover_border);
			g2.fillRect(1,1,1,1);
			g2.fillRect(1,this.getHeight()-2,1,1);
			g2.fillRect(this.getWidth()-2,1,1,1);
			g2.fillRect(this.getWidth()-2,this.getHeight()-2,1,1);
		}
		
		super.paintComponent(g);
	}
	
	public static void setNewSelected(ExplorerItemLabel newSelected) {
		if(Explorer.selectedLabel != null) {
			Explorer.selectedLabel.selected = false;
			Explorer.selectedLabel.repaint();
		}
		Explorer.selectedLabel = newSelected;
		if(newSelected != null) {
			newSelected.selected = true;
			newSelected.repaint();
		}
	}
	
	public void open() {
		File file = new File(parent.path);
		if(file.isDirectory()) {
			parent.expand.doClick();
		} else {
			TabManager.openTab(parent.path);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		rollover = true;
		this.repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		rollover = false;
		this.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		setNewSelected(this);
		if (e.getClickCount() % 2 == 0 && !e.isConsumed() && e.getButton() == MouseEvent.BUTTON1) {
		     e.consume();
		     
		     open();
		}
		if(e.isPopupTrigger()) {
			showContextMenu(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setNewSelected(this);
		if(e.isPopupTrigger()) {
			showContextMenu(e);
		}
	}

	private static void showContextMenu(MouseEvent e) {
		ExplorerItemPopup menu = new ExplorerItemPopup();
        menu.show(e.getComponent(), e.getX(), e.getY());
	}
}

class ExplorerItemPopup extends JPopupMenu {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7968631495164738852L;
    public ExplorerItemPopup(){
    	add(MenuItems.newMenu("New                    "));
    	addSeparator();
    	
    	JMenuItem openItem = new JMenuItem("Open");
    	openItem.addActionListener(new AbstractAction() {
    		/**
			 * 
			 */
			private static final long serialVersionUID = -8437185508877673148L;

			public void actionPerformed(ActionEvent arg0) {
    			Explorer.selectedLabel.open();
    		}
    	});
    	
    	add(openItem);
    	
    	JMenuItem openInSystemItem = new JMenuItem("Show in System Explorer");
    	openInSystemItem.addActionListener(new AbstractAction() {
    		/**
			 * 
			 */
			private static final long serialVersionUID = 6207282331220892917L;

			public void actionPerformed(ActionEvent arg0) {
    			try {
    				Runtime.getRuntime().exec("explorer.exe /select," + Explorer.selectedLabel.parent.path);
				} catch (IOException e) {
					e.printStackTrace(new PrintWriter(Window.consoleout));
				}
    		}
    	});
    	
    	add(openInSystemItem);
    	addSeparator();
    	add(MenuItems.fileItem(FileMenuItem.COPY));
    	add(MenuItems.fileItem(FileMenuItem.PASTE));
    	add(MenuItems.fileItem(FileMenuItem.DELETE));
    	addSeparator();
    	JMenu refactorMenu = new JMenu("Refactor");

    		JMenuItem renameItem = MenuItems.fileItem(FileMenuItem.RENAME);
    		renameItem.addActionListener(new AbstractAction() {
    			/**
				 * 
				 */
				private static final long serialVersionUID = 382967017949373966L;

				public void actionPerformed(ActionEvent arg0) {
    				
    				String path = Explorer.selectedLabel.parent.path;
    				String name = new File(path).getName();
    				String rawName = StringUtil.stripExtension(name);
    				String extension = name.replaceAll(rawName, "");
    				String pathToParent = path.substring(0,path.lastIndexOf(name));
    				
    				String newName = StringPrompt.prompt("Rename", "Enter a new name for the file:", rawName, new StringValidator() {public boolean validate(String str) {return str.trim().length() > 0 && FileUtil.validateFilename(str) && !new File(pathToParent + str + extension).exists();}});
    				
    				if(newName != null) {
    					boolean renamed = new File(path).renameTo(new File(pathToParent + newName + extension));
    					if(renamed) {
    						ExplorerItem parentItem = Explorer.selectedLabel.parent;
        					parentItem.path = pathToParent + newName + extension;
        					if(parentItem.parent != null) {
        						parentItem.parent.collapse();
            					parentItem.parent.expand();
        					} else {
        						Window.explorer.generateProjectList();
        					}
        					
        					TabManager.renameTab(path, pathToParent + newName + extension);
        					
    					} else {
    						JOptionPane.showMessageDialog(null, "<html>The action can't be completed because the folder or file is open in another program.<br>Close the folder and try again.</html>", "An error ocurred.", JOptionPane.ERROR_MESSAGE);
    					}
    				}

    			}
    		});
			refactorMenu.add(renameItem);
			refactorMenu.add(MenuItems.fileItem(FileMenuItem.MOVE));
    	
    	add(refactorMenu);
    }
}