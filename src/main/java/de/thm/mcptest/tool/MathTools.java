package de.thm.mcptest.tool;

import org.springframework.ai.tool.annotation.Tool;

public class MathTools {

    public MathTools() {
    }

    @Tool(description = "Adds two numbers")
    public int sumNumbers(int number1, int number2) {
        return number1 + number2;
    }

    @Tool(description = "Multiplies two numbers")
    public int multiplyNumbers(int number1, int number2) {
        return number1 * number2;
    }

    @Tool(description = "Divide two numbers")
    public double divideNumbers(double number1, double number2) {
        return number1 / number2;
    }
}