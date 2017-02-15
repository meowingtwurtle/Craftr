package com.energyxxer.craftr.global;

import com.energyxxer.craftr.compile.analysis.token.TokenType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 2/11/2017.
 */
public class CraftrUtil {

    private static final List<String>
            modifiers = Arrays.asList("public", "static", "typestatic", "abstract", "final", "protected", "private", "synchronized", "compilation", "ingame"),
            unit_types = Arrays.asList("entity", "item", "feature", "class"),
            unit_actions = Arrays.asList("extends", "implements"),
            data_types = Arrays.asList("int", "String", "float", "boolean", "type", "void", "Thread"),
            keywords = Arrays.asList("if", "else", "while", "for", "switch", "case", "default", "new", "event", "init", "package", "import", "operator"),
            action_keywords = Arrays.asList("break", "continue", "return"),
            booleans = Arrays.asList("true", "false"),
            nulls = Collections.singletonList("null");

    public static String classify(String token) {
        for(String p : modifiers) {
            if(token.equals(p)) {
                return TokenType.MODIFIER;
            }
        }
        for(String p : unit_types) {
            if(token.equals(p)) {
                return TokenType.UNIT_TYPE;
            }
        }
        for(String p : unit_actions) {
            if(token.equals(p)) {
                return TokenType.UNIT_ACTION;
            }
        }
        for(String p : data_types) {
            if(token.equals(p)) {
                return TokenType.DATA_TYPE;
            }
        }
        for(String p : keywords) {
            if(token.equals(p)) {
                return TokenType.KEYWORD;
            }
        }
        for(String p : action_keywords) {
            if(token.equals(p)) {
                return TokenType.ACTION_KEYWORD;
            }
        }
        for(String p : booleans) {
            if(token.equals(p)) {
                return TokenType.BOOLEAN;
            }
        }
        for(String p : nulls) {
            if(token.equals(p)) {
                return TokenType.NULL;
            }
        }

        return TokenType.IDENTIFIER;
    }

    public static boolean isValidIdentifier(String str) {
        if(str.length() <= 0) return false;
        for(int i = 0; i < str.length(); i++) {
            if((i == 0 && !Character.isJavaIdentifierStart(str.charAt(0))) || (!Character.isJavaIdentifierPart(str.charAt(i)))) {
                return false;
            }
        }
        return classify(str) == TokenType.IDENTIFIER;
    }
}