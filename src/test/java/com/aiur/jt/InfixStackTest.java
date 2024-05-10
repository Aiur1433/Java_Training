package com.aiur.jt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class InfixStackTest {

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @DisplayName("判断一个字符串是数字")
  @ParameterizedTest
  @CsvSource({
    "8,true",
    "125,true",
    "8+,false",
    "865;32,false",
    "test,false",
    "++-/-,false",
    "+,false"
  })
  void testIsNumber(String number, boolean exp) {
    assertEquals(exp, InfixStack.getInstance().isNumber(number));
  }

  @DisplayName("判断一个字符串是运算符")
  @ParameterizedTest
  @CsvSource({
    "8,false",
    "125,false",
    "8+,false",
    "865;32,false",
    "test,false",
    "++-/-,false",
    "+,true",
    "(,true"
  })
  void testIsOperator(String str, boolean exp) {
    assertEquals(exp, InfixStack.getInstance().isOperator(str));
  }

  @DisplayName("基础运算")
  @ParameterizedTest
  @CsvSource({"1,1,+,2", "1,1,-,0", "21,12,*,252", "100,10,/,10"})
  void testSingleCalc(int a, int b, String operator, int exp) {
    assertEquals(exp, InfixStack.getInstance().singleCalc(a, b, operator));
  }

  @DisplayName("分割算式")
  @ParameterizedTest
  @CsvSource({"1+1,3", "2*(5+6)/2-1,11", "((22+11)*3),9", "(123+ 22 )* ( 423 +42),11"})
  void testSpiltStr(String str, int exp) {
    assertEquals(exp, InfixStack.getInstance().spiltStr(str).size());
  }

  @DisplayName("中缀表达式转后缀表达式")
  @ParameterizedTest
  @CsvSource({
    "1+1,1 1 + ",
    "3*(5+6)/2-1,3 5 6 + * 2 / 1 - ",
    "5+((22+11)*3),5 22 11 + 3 * + ",
    "(123+ 22 )* ( 423 +42),123 22 + 423 42 + * ",
    "(4*(3 + (1+1) /2 )+3)*5,4 3 1 1 + 2 / + * 3 + 5 * "
  })
  void testInfix2Postfix(String in, String exp) {
    InfixStack is = InfixStack.getInstance();
    assertEquals(exp, String.join(" ", is.infix2Postfix(is.spiltStr(in))));
  }

  // 正常流
  // 1、基本的四则运算
  // 2、带括号的四则运算
  // 异常流
  // 1、不是一条算式
  // 2、不合法的算式
  @DisplayName("根据字符串计算公式结果")
  @ParameterizedTest
  @CsvSource({"2*5+6/2-1,12", "2*(5+6)/2-1,10", "abc,err:[abc]", "11-*23,err:[11-*23]"})
  void inputStrCalculate(String str, String exp) {
    assertEquals(exp, InfixStack.getInstance().calc(str));
  }
}
