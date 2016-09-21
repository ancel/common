package com.work.common.utils.formula;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

enum Operation {
	
	ADD('+', 1), 
	SUBTRACT('-', 1), 
	MULTIPLY('*', 2), 
	DIVIDE('/', 2),
	FRONT_BRACKET('(', 100), // 前括号
	BACK_BRACKET(')', 100);// 后括号
	private static final Logger LOGGER = LoggerFactory.getLogger(Operation.class);
	private char op;
	private int level;

	private Operation(char op, int level) {
		this.op = op;
		this.level = level;
	}

	public char getOp() {
		return op;
	}

	public void setOp(char op) {
		this.op = op;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public static Operation getOperation(char op) {
		switch (op) {
		case '+':
			return ADD;
		case '-':
			return SUBTRACT;
		case '*':
			return MULTIPLY;
		case '/':
			return DIVIDE;
		case '(':
			return FRONT_BRACKET;
		case ')':
			return BACK_BRACKET;
		default:
			return null;
		}
	}
	
	public static BigDecimal cal(BigDecimal numA,BigDecimal numB,char op){
		switch (op) {
		case '+':
			return numA.add(numB);
		case '-':
			return numA.subtract(numB);
		case '*':
			return numA.multiply(numB);
		case '/':
			return numA.divide(numB);
		default:
			LOGGER.error("不正确的操作符:"+op);
			System.exit(1);
		}
		return null;
	}
}