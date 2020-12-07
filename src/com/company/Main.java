package com.company;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println(calculate(opn(d.readLine())));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static String opn(String sIn) throws Exception {
        StringBuilder sbOut = new StringBuilder();
        Stack<Character> a = new Stack<>();
        char cTmp;
        for (char cIn :
                sIn.toCharArray()) {
            if (isOp(cIn)) {
                while (!a.isEmpty()) {
                    cTmp = a.peek();
                    if (isOp(cTmp) && (opPrior(cIn) <= opPrior(cTmp))) {
                        sbOut.append(" ").append(cTmp).append(" ");
                        a.pop();
                    } else {
                        sbOut.append(" ");
                        break;
                    }
                }
                sbOut.append(" ");
                a.add(cIn);
            } else if ('(' == cIn) {
                a.add(cIn);
            } else if (')' == cIn) {
                cTmp = a.pop();
                while ('(' != cTmp) {
                    if (a.isEmpty()) {
                        throw new Exception("Ошибка разбора скобок. Проверьте правильность выражения.");
                    }
                    sbOut.append(" ").append(cTmp);
                    cTmp = a.pop();
                }
            } else {
                sbOut.append(cIn);
            }
        }
        while (!a.isEmpty()) {
            sbOut.append(" ").append(a.peek());
            a.pop();
        }
        return sbOut.toString();
    }

    private static boolean isOp(char c) {
        return switch (c) {
            case '-', '+', '*', '/', '^' -> true;
            default -> false;
        };
    }

    private static int opPrior(char op) {
        return switch (op) {
            case '^' -> 3;
            case '*', '/', '%' -> 2;
            default -> 1;
        };
    }

    private static double calc(double dA, double dB, char op) {
        return switch (op) {
            case '+' -> dA + dB;
            case '-' -> dA - dB;
            case '/' -> dA / dB;
            case '*' -> dA * dB;
            case '%' -> dA % dB;
            case '^' ->Math.pow(dA, dB);
            default -> throw new IllegalStateException("Unexpected value: " + op);
        };
    }

    private static double calculate(String sIn) throws Exception {
        double dA, dB;
        String sTmp;
        Deque<Double> stack = new ArrayDeque<>();
        StringTokenizer st = new StringTokenizer(sIn);
        while (st.hasMoreTokens()) {
            try {
                sTmp = st.nextToken().trim();
                if (1 == sTmp.length() && isOp(sTmp.charAt(0))) {
                    if (stack.size() < 2) {
                        throw new Exception("Неверное количество данных в стеке для операции " + sTmp);
                    }
                    dB = stack.pop();
                    dA = stack.pop();
                    dA = calc(dA, dB, sTmp.charAt(0));
                } else {
                    dA = Double.parseDouble(sTmp);
                }
                stack.push(dA);
            } catch (Exception e) {
                throw new Exception("Недопустимый символ в выражении");
            }
        }
        if (stack.size() > 1) {
            throw new Exception("Количество операторов не соответствует количеству операндов");
        }
        return stack.pop();
    }
}