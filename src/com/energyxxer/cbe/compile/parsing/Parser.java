package com.energyxxer.cbe.compile.parsing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.energyxxer.cbe.compile.analysis.LangStructures;
import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse;
import com.energyxxer.cbe.compile.analysis.token.TokenStream;
import com.energyxxer.cbe.compile.analysis.token.TokenType;
import com.energyxxer.cbe.compile.analysis.token.structures.TokenItem;
import com.energyxxer.cbe.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.cbe.compile.parsing.classes.CBEEntity;
import com.energyxxer.cbe.global.ProjectManager;
import com.energyxxer.cbe.logic.Project;

public class Parser {
	
	private ArrayList<ArrayList<Token>> tokens = new ArrayList<ArrayList<Token>>();
	private EntityRegistry reg = new EntityRegistry();

	public Parser(TokenStream ts, Project project) {
		ArrayList<Token> currentList = new ArrayList<Token>();
		for(Token t : ts.tokens) {
			currentList.add(t);
			if(t.type == TokenType.END_OF_FILE) {
				ArrayList<Token> f = new ArrayList<Token>();
				for(Token t2 : currentList) {
					f.add(t2);
				}
				tokens.add(f);
				currentList.clear();
			}
		}
		
		for(ArrayList<Token> f : tokens) {
			TokenMatchResponse match = LangStructures.UNIT_DECLARATION.match(f);
			
			if(!match.matched) {
				System.out.println(match.getFormattedErrorMessage());
				return;
			}
			
			TokenPattern<?> pattern = match.pattern;

			String type = pattern.search(TokenType.UNIT_TYPE).get(0).value;
			Token nameToken = ((TokenItem) pattern.searchByName("UNIT_NAME").get(0)).getContents();
			String name = nameToken.value;
			String file = nameToken.file;
			
			//System.out.println(file);
			
			List<TokenPattern<?>> actions = pattern.searchByName("UNIT_ACTION");
			
			if(type.equals("entity")) {
				CBEEntity e = new CBEEntity(name, project,new File(file)).setDeclaration(pattern);
				for(TokenPattern<?> action : actions) {
					if(action.search(TokenType.UNIT_ACTION).get(0).value.equals("extends")) {
						e.entityExtends = action.search(TokenType.IDENTIFIER).get(0).value;
					}
				}
				
				reg.add(e);
			}
		}
		if(!reg.close()) return;
		
		for(CBEEntity e : reg) {
			ProjectManager.setIconFor(e.file, "*entities" + File.separator + e.entityType);
		}
		//System.out.println(reg);
	}
}