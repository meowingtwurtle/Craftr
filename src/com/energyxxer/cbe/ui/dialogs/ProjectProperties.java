package com.energyxxer.cbe.ui.dialogs;

import com.energyxxer.cbe.logic.Project;
import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.minecraft.MinecraftConstants;
import com.energyxxer.cbe.ui.components.ComponentResizer;
import com.energyxxer.cbe.ui.components.XFileField;
import com.energyxxer.cbe.ui.styledcomponents.*;
import com.energyxxer.cbe.ui.theme.Theme;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.ImageManager;
import com.energyxxer.cbe.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ProjectProperties {
	
	//private static final Font FIELD_FONT = new Font("Consolas", 0, 12);

	private static Theme t;

	public static void show(Project project) {

		JDialog dialog = new JDialog(Window.jframe);


		ThemeChangeListener.addThemeChangeListener(th -> t = th);
		
		StyledTextField cPrefix;
		StyledFileField cWorld;
		
		JPanel pane = new JPanel(new BorderLayout());
		//JButton okay = new JButton("OK");
		//JButton cancel = new JButton("Cancel");
		
		pane.setPreferredSize(new Dimension(900,600));
		pane.setBackground(t.getColor("ProjectProperties.background", new Color(235, 235, 235)));
		
		{
			JPanel sidebar = new JPanel(new BorderLayout());

			ComponentResizer sidebarResizer = new ComponentResizer(sidebar);
			sidebarResizer.setResizable(false, false, false, true);

			String[] sections = new String[] { "General", "Compiler", "Editor", "Code Style", "Resources" };

			StyledList<String> navigator = new StyledList<>(sections, "ProjectProperties");
			sidebar.setBackground(navigator.getBackground());
			sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, t.getColor("ProjectProperties.content.border",new Color(200, 200, 200))));
			navigator.setPreferredSize(new Dimension(200,500));

			sidebar.add(navigator, BorderLayout.CENTER);
			
			pane.add(sidebar, BorderLayout.WEST);
		}
		
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setBackground(t.getColor("ProjectProperties.content.background", new Color(235, 235, 235)));
		pane.add(contentPane, BorderLayout.CENTER);

		JPanel contentCompiler = new JPanel(new BorderLayout());
		{
			contentCompiler.setBackground(contentPane.getBackground());
			contentPane.add(contentCompiler, BorderLayout.CENTER);
			
			{
				JPanel header = new JPanel(new BorderLayout());
				header.setBackground(t.getColor("ProjectProperties.content.header.background", new Color(235, 235, 235)));
				header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, t.getColor("ProjectProperties.content.header.border", new Color(200, 200, 200))));
				header.setPreferredSize(new Dimension(0,40));
				contentCompiler.add(header, BorderLayout.NORTH);
				
				{
					JPanel padding = new JPanel();
					padding.setOpaque(false);
					padding.setPreferredSize(new Dimension(25,25));
					header.add(padding, BorderLayout.WEST);
				}
				
				JLabel label = new JLabel("Compiler");
				label.setForeground(t.getColor("ProjectProperties.content.header.foreground", Color.BLACK));
				label.setFont(new Font(t.getString("ProjectProperties.content.header.font",t.getString("General.font","Tahoma")),1,20));
				header.add(label, BorderLayout.CENTER);
			}
			
			{
				JPanel padding_left = new JPanel();
				padding_left.setOpaque(false);
				padding_left.setPreferredSize(new Dimension(50,25));
				contentCompiler.add(padding_left, BorderLayout.WEST);
			}
			{
				JPanel padding_right = new JPanel();
				padding_right.setOpaque(false);
				padding_right.setPreferredSize(new Dimension(50,25));
				contentCompiler.add(padding_right, BorderLayout.EAST);
			}
			
			{
				
				JPanel content = new JPanel();
				content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
				content.setOpaque(false);
				contentCompiler.add(content, BorderLayout.CENTER);
				
				{
					JPanel padding = new JPanel();
					padding.setOpaque(false);
					padding.setMinimumSize(new Dimension(1,20));
					padding.setMaximumSize(new Dimension(1,20));
					content.add(padding);
				}

				{
					content.add(new StyledLabel("Prefix:", "ProjectProperties.content"));
				}
				{
					JPanel prefixFields = new JPanel();

					prefixFields.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
					prefixFields.setOpaque(false);
					prefixFields.setAlignmentX(Component.LEFT_ALIGNMENT);
					{
						cPrefix = new StyledTextField(project.getPrefix(),"ProjectProperties.content");
						cPrefix.setPreferredSize(new Dimension(150,25));
						//cPrefix.setAlignmentX(Component.LEFT_ALIGNMENT);

						prefixFields.add(cPrefix);
					}
					{
						StyledButton randomize = new StyledButton("Generate Random Prefix", "ProjectProperties.content");
						randomize.setPreferredSize(new Dimension(150, 25));

						randomize.addActionListener(e -> cPrefix.setText(StringUtil.getRandomString(4)));

						prefixFields.add(randomize);
					}
					{
						StyledButton reset = new StyledButton("Reset Prefix", "ProjectProperties.content");
						reset.setPreferredSize(new Dimension(100, 25));

						reset.addActionListener(e -> cPrefix.setText(StringUtil.getInitials(project.getName()).toLowerCase()));

						prefixFields.add(reset);
					}

					prefixFields.setMaximumSize(new Dimension(prefixFields.getMaximumSize().width, 30));

					content.add(prefixFields);
				}

				{
					JPanel margin = new JPanel();
					margin.setMinimumSize(new Dimension(200,15));
					margin.setMaximumSize(new Dimension(200,15));
					margin.setOpaque(false);
					margin.setAlignmentX(Component.LEFT_ALIGNMENT);

					content.add(margin);
				}
				
				{
					{
						JLabel label = new JLabel("World Output:");
						label.setForeground(t.getColor("ProjectProperties.content.label.foreground", Color.BLACK));
						label.setFont(new Font(t.getString("ProjectProperties.content.label.font",t.getString("General.font","Tahoma")),1,12));
						content.add(label);
					}
					File file = new File(MinecraftConstants.getMinecraftDir() + File.separator + "saves");
					if(project.getWorld() != null) file = new File(project.getWorld());
					cWorld = new StyledFileField(file,"ProjectProperties.content");
					cWorld.setDialogTitle("Open world...");
					cWorld.setOperation(XFileField.OPEN_DIRECTORY);
					cWorld.setMaximumSize(new Dimension(cWorld.getMaximumSize().width,25));
					cWorld.setAlignmentX(Component.LEFT_ALIGNMENT);
					
					content.add(cWorld);
				}
				
			}
			
		}
		
		{
			JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
			buttons.setPreferredSize(new Dimension(0,50));
			buttons.setBackground(contentPane.getBackground());
			
			{
				JPanel padding = new JPanel();
				padding.setOpaque(false);
				padding.setPreferredSize(new Dimension(25,25));
				buttons.add(padding);
			}
			
			{
				StyledButton okay = new StyledButton("OK", "ProjectProperties");
				okay.setPreferredSize(new Dimension(75, 25));
				buttons.add(okay);

				okay.addActionListener(e -> {
					project.setPrefix(cPrefix.getText());
					project.setWorld(cWorld.getFile().getAbsolutePath());
					project.updateConfig();

					dialog.setVisible(false);
					dialog.dispose();
				});
			}
			
			{
				StyledButton cancel = new StyledButton("Cancel", "ProjectProperties");
				cancel.setPreferredSize(new Dimension(75, 25));
				buttons.add(cancel);

				cancel.addActionListener(e -> {
					dialog.setVisible(false);
					dialog.dispose();
				});
			}
			
			contentPane.add(buttons, BorderLayout.SOUTH);
		}

		dialog.setVisible(true);
		dialog.setContentPane(pane);
		dialog.pack();
		//dialog.setResizable(false);
		
		dialog.setTitle("Editing properties for project \"" + project.getName() + "\"");
		dialog.setIconImage(ImageManager.load("/assets/logo/logo_icon.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
		
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		center.x -= dialog.getWidth()/2;
		center.y -= dialog.getHeight()/2;
		
		dialog.setLocation(center);
		
		//JOptionPane.showOptionDialog(Window.jframe, optionPane, "Editing properties for project \"" + project.getName() + "\"", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] { okay, cancel }, null);
	}
}
