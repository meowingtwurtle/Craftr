package com.energyxxer.xswing;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Font;

public class SystemDefaults {
	public static final Color BACKGROUND = new JPanel().getBackground();
	public static final Color FOREGROUND = new JLabel().getForeground();
	public static final Font FONT = new JLabel().getFont();
	public static final Border BORDER = new JPanel().getBorder();
	
}
