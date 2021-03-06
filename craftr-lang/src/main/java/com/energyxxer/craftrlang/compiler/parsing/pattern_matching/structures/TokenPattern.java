package com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.util.StringBounds;
import com.energyxxer.util.StringLocation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class TokenPattern<T> {

	protected String name = "";
	public abstract T getContents();
	public abstract TokenPattern<T> setName(String name);

	public abstract List<Token> search(String type);
	public abstract List<Token> deepSearch(String type);
	public abstract List<TokenPattern<?>> searchByName(String name);
	public abstract List<TokenPattern<?>> deepSearchByName(String name);

	public abstract TokenPattern<?> find(String path);

	public abstract String flatten(boolean separate);

	public abstract File getFile();

	public String getLocation() {
		StringLocation loc = getStringLocation();
		return getFile().getName() + ":" + loc.line + ":" + loc.column + "#" + loc.index;
	}

	public abstract StringLocation getStringLocation();
	public abstract StringBounds getStringBounds();
	public int getCharLength() {
		ArrayList<Token> tokens = flattenTokens();
		if(tokens.size() == 0) return 0;
		int start = tokens.get(0).loc.index;
		Token lastToken = tokens.get(tokens.size()-1);
		int end = lastToken.loc.index + lastToken.value.length();
		return end - start;
	}

	public String getFormattedPath() {
		StringLocation loc = getStringLocation();
		return "\b" + getFile() + "\b" + loc.index + "\b" + getCharLength() + "\b"
				+ getLocation() + "\b";
	}

	public abstract ArrayList<Token> flattenTokens();

	public abstract String getType();

    public String getName() {
        return name;
    }
}
