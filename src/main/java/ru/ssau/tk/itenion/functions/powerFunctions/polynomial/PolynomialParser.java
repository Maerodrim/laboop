package ru.ssau.tk.itenion.functions.powerFunctions.polynomial;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PolynomialParser {

    private final static int INDEX_SIGN_COEFF = 1;

    private final static String NEGATIVE_SIGN = "-";
    private final static String MONOMIAL_TEMPLATE =
            "([+-])?(?:(?:(\\d*)(x)(?:\\^(\\d+)))|(?:(\\d*\\.\\d*)(x)(?:\\^(\\d+)))|(?:(\\d*)(x)())|(?:(\\d*\\.\\d*)(x)())|(?:(\\d*\\.\\d*)()())|(?:(\\d+)()()))";
    //1            2    3        4           5            6        7           8     9 10       11         12 13      14          1516      17   1819

    /**
     * Получение многочлена одной переменной из строки, записанной в
     * стандартной математической форме.
     * Переменная многочлена должна быть "x".
     */
    public Polynomial parse(String rawPolynomial) {
        return new Polynomial(parseToMap(rawPolynomial));
    }

    public Map<Integer, Double> parseToMap(String rawPolynomial) {
        String source = normalizeSourceString(rawPolynomial);
        Map<Integer, Double> result = new HashMap<>();
        Pattern monomial = Pattern.compile(MONOMIAL_TEMPLATE);
        Matcher m = monomial.matcher(source);
        while ((!m.hitEnd() && (m.find()))) {
            boolean isNegative = NEGATIVE_SIGN.equals(m.group(INDEX_SIGN_COEFF));
            int indexActual = 0;
            for (int i = 2; i < 18; i += 3) {
                if (!Objects.isNull(m.group(i))) {
                    indexActual = i;
                }
            }
            double currentCoefficient = calcCoeff(isNegative, m.group(indexActual));
            int currentDegree = calcDegree(m.group(indexActual + 2),
                    m.group(indexActual + 1));
            result.put(currentDegree, currentCoefficient);
        }
        return result;
    }

    /**
     * Вычисление коэффициента одночлена
     */
    private double calcCoeff(boolean isNegative, String coefficient) {
        double result = (!coefficient.isEmpty()) ? Double.parseDouble(coefficient) : 1;
        return (isNegative) ? -result : result;
    }

    /**
     * Вычисление степени одночлена
     */
    private int calcDegree(String degree, String symbolVariable) {
        int result = (!symbolVariable.isEmpty()) ? 1 : 0;
        return (!degree.isEmpty()) ? Integer.parseInt(degree) : result;
    }

    /**
     * Удаление из строки всех пробелов и
     * преобразование всех символов в нижний регистр
     */
    private String normalizeSourceString(String source) {
        final String EMPTY_STRING = "";
        final String WHITESPACE_TEMPLATE = "\\s+";
        String result = source.replaceAll(WHITESPACE_TEMPLATE, EMPTY_STRING);
        return result.toLowerCase();
    }
}