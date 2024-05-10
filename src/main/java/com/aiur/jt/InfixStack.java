package com.aiur.jt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfixStack {
  private static InfixStack INS;

  enum Operator {
    SUM("+", 2),
    RED("-", 2),
    MUL("*", 1),
    DIV("/", 1),
    LBK("(", 0),
    RBK(")", 3);
    private final String operator;
    private final int priority;

    Operator(String operator, int priority) {
      this.operator = operator;
      this.priority = priority;
    }

    String getOperator() {
      return operator;
    }

    int getPriority() {
      return priority;
    }

    static Operator get(String operator) {
      for (Operator op : Operator.values()) {
        if (op.getOperator().equals(operator)) {
          return op;
        }
      }
      return null;
    }
  }

  private static final String REGEX_NUMBER = "\\d+";
  private static final String REGEX_OPERATOR = "[\\(\\+\\-\\*\\/\\)]";
  private static final String REGEX_ERR = "[^0-9\\+\\-\\*/\\(\\)]+";

  private final Pattern pattern;

  private InfixStack() {
    pattern = Pattern.compile(REGEX_NUMBER + "|" + REGEX_OPERATOR);
  }

  public static InfixStack getInstance() {
    if (INS == null) {
      INS = new InfixStack();
    }
    return INS;
  }

  public String calc(String str) {
    // 0 排除非法串
    if (Pattern.matches(REGEX_ERR, str)) {
      return String.format("err:[%s]", str);
    }

    // 1 分割字符和数字
    List<String> list = spiltStr(str);

    // 2 中缀转后缀
    List<String> postfix = infix2Postfix(list);
    if (postfix == null) {
      return String.format("err:[%s]", str);
    }

    // 3 计算后缀
    // 遍历序列，数字放入栈，符号则取两个数字计算后放入栈
    Stack<Integer> stack = new Stack<>();
    postfix.forEach(
        s ->
            Optional.ofNullable(Operator.get(s))
                .ifPresentOrElse(
                    op -> stack.push(singleCalc(stack.pop(), stack.pop(), op.getOperator())),
                    () -> stack.push(Integer.valueOf(s))));

    return String.valueOf(stack.pop());
  }

  public boolean isNumber(String number) {
    return Pattern.matches(REGEX_NUMBER, number);
  }

  public boolean isOperator(String str) {
    return Pattern.matches(REGEX_OPERATOR, str);
  }

  public int singleCalc(int a, int b, String operator) {
    return switch (operator) {
      case "+" -> a + b;
      case "-" -> b - a;
      case "*" -> a * b;
      case "/" -> b / a;
      default -> 0;
    };
  }

  public List<String> infix2Postfix(List<String> infixList) {
    List<String> postfix = new ArrayList<>();
    Stack<Operator> operatorStack = new Stack<>();
    boolean afterNumber = false;
    for (String str : infixList) {
      // 遇到操作数时，将其输出到输出队列。
      if (isNumber(str)) {
        postfix.add(str);
        afterNumber = true;
      } else if (isOperator(str)) {
        if (!afterNumber && !"(".equals(str)) {
          // 没接在数字之后，说明两个连续符号，算式有问题。
          return null;
        }
        Optional.ofNullable(Operator.get(str))
            .ifPresent(
                op -> {
                  // 如果是又括号 弹出左括号和他之前的所有符号
                  if (op == Operator.RBK) {
                    Operator top = operatorStack.pop();
                    while (top != Operator.LBK) {
                      postfix.add(top.getOperator());
                      top = operatorStack.pop();
                    }
                  }
                  // 如果优先级低于或等于栈顶运算符，则弹出栈顶运算符并将其输出到输出队列。
                  else {
                    if (!operatorStack.isEmpty()
                        && op.getPriority() >= operatorStack.peek().getPriority()) {
                      if (operatorStack.peek() != Operator.LBK) {
                        postfix.add(operatorStack.pop().getOperator());
                      }
                    }
                    // 将该运算符压入运算符栈。
                    operatorStack.push(op);
                  }
                });
        if (!")".equals(str)) {
          afterNumber = false;
        }
      }
    }
    // 输出剩下的栈中符号
    while (!operatorStack.isEmpty()) {
      Operator top = operatorStack.pop();
      if (top != Operator.LBK) {
        postfix.add(top.getOperator());
      }
    }
    return postfix;
  }

  public List<String> spiltStr(String str) {
    Matcher matcher = pattern.matcher(str);
    List<String> list = new ArrayList<>();
    while (matcher.find()) {
      list.add(matcher.group());
    }
    return list;
  }
}
