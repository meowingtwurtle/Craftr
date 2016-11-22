package com.energyxxer.cbe.ui.theme;

import java.awt.Color;

public class LightTheme extends Theme {
	
	private static Theme INSTANCE = new LightTheme();
	
	private LightTheme() {
		
		name = "Light Theme";
		path = "light_theme/";
		
		//Primary Colors
		p1 = new Color(255, 255, 255);
		p2 = new Color(235, 235, 235);
		p3 = new Color(215, 215, 215);
		p4 = new Color(200, 202, 205);
		p5 = null;
		p6 = null;
		p7 = null;
		p8 = null;
		//Secondary Colors
		s1 = null;
		s2 = null;
		s3 = null;
		s4 = null;
		//Text Colors
		t1 = new Color(0,0,0);
		t2 = null;
		t3 = new Color(150, 150, 150);
		t4 = null;
		//Line Colors
		l1 = new Color(200, 200, 200);
		l2 = new Color(180, 200, 210);
		//Button Colors
		b1 = new Color(240, 245, 255);
		b2 = new Color(200, 220, 230);
		b3 = new Color(200, 220, 230);
		b4 = new Color(180, 200, 210);
		//Gradient Colors
		g1 = Color.WHITE;
	}
	
	public static Theme getInstance() {
		return INSTANCE;
	}
}