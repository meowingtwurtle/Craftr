package com.energyxxer.craftr.compile.parsing.classes.values;

import com.energyxxer.craftr.compile.analysis.token.structures.TokenItem;
import com.energyxxer.craftr.compile.exceptions.IllegalOperandsException;
import com.energyxxer.craftr.compile.parsing.classes.evaluation.Evaluator;
import com.energyxxer.craftr.global.Console;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 12/20/2016.
 */
public class CraftrNullValue extends CraftrValue {
    public CraftrNullValue() {
    }

    @Override
    public String getType() {
        return "null";
    }

    @Override
    public String getInternalType() {
        return "null";
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public CraftrValue addition(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CraftrValue subtraction(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CraftrValue multiplication(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CraftrValue division(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CraftrValue modulo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CraftrValue exponentiate(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CraftrValue increment() throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CraftrValue decrement() throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isGreaterThan(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isLessThan(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isGreaterThanOrEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isLessThanOrEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return operand.getInternalType().equals("null");
    }

    @Override
    public CraftrValue assignTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    public static void init() {
        Evaluator.addEvaluator("NULL", p -> {
            if(p instanceof TokenItem) {
                TokenItem item = (TokenItem) p;
                String str = item.getContents().value;
                return (str.equals("null") ? new CraftrNullValue() : null);
            } else {
                Console.err.println("[ERROR] Number pattern with type " + p.getType() + ": " + p);
                return null;
            }
        });
    }
}
