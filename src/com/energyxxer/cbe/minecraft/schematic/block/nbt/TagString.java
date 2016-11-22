package com.energyxxer.cbe.minecraft.schematic.block.nbt;

import com.energyxxer.cbe.util.StringUtil;

public class TagString extends Tag {
	String value;
	
	public TagString(String name, String value) {
		super(name);
		this.value = value;
	}

	@Override
	public String toAnonymousString() {
		return "\"" + StringUtil.addSlashes(value) + "\"";
	}

	@Override
	public String toString() {
		return String.format("%s:%s", name, toAnonymousString());
	}
}
