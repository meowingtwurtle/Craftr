package com.energyxxer.cbe.compile.analysis.token.matching;

import java.util.List;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse;
import com.energyxxer.cbe.util.Stack;

public abstract class TokenPatternMatch {
	public String name = "";
	public boolean optional;
	
	public abstract TokenMatchResponse match(List<Token> tokens);

	public abstract TokenMatchResponse match(List<Token> tokens, Stack st);

	public abstract int length();
	
	public boolean isOptional() {
		return optional;
	}
	
	public TokenPatternMatch setName(String name) {
		this.name = name;
		return this;
	}
	
}