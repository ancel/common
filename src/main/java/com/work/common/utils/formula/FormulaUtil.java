package com.work.common.utils.formula;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

/**
 * 表达式工具
 * @author：wanghaibo 
 * @creattime：2016年9月21日 下午5:36:35 
 * 
 */  
public class FormulaUtil {


	/**
	 * 中缀表达式转后缀表达式
	 * @param inFormula
	 * @return
	 */
	public static List<String> inToPostFormula(String inFormula) {
		List<String> portFormulaList = new ArrayList<String>();
		StringBuilder eleBuilder = new StringBuilder();
		Stack<Operation> opStack = new Stack<Operation>();
		Operation operation;
		for (int j = 0; j < inFormula.length(); j++) {
			char ch = inFormula.charAt(j);
			switch (ch) {
			case '(':
				if (StringUtils.isNotBlank(eleBuilder.toString())) {
					portFormulaList.add(eleBuilder.toString());
					eleBuilder.delete(0, eleBuilder.toString().length());
				}
				opStack.push(Operation.getOperation(ch));
				break;
			case ')':
				if (StringUtils.isNotBlank(eleBuilder.toString())) {
					portFormulaList.add(eleBuilder.toString());
					eleBuilder.delete(0, eleBuilder.toString().length());
				}
				while (!opStack.isEmpty()) {
					operation = opStack.pop();
					if (operation.getOp() == '(') {
						break;
					} else {
						portFormulaList.add(String.valueOf(operation.getOp()));
					}
				}
				break;
			default:
				operation = Operation.getOperation(ch);
				if (null == operation) {
					eleBuilder.append(ch);
				} else {
					if (StringUtils.isNotBlank(eleBuilder.toString())) {
						portFormulaList.add(eleBuilder.toString());
						eleBuilder.delete(0, eleBuilder.toString().length());
					}
					while (!opStack.isEmpty()) {
						Operation opTop = opStack.pop();
						if (opTop.getOp() == '(') {
							opStack.push(opTop);
							break;
						} else {
							if (opTop.getLevel() < operation.getLevel()) {
								opStack.push(opTop);
								break;
							} else {
								portFormulaList.add(String.valueOf(opTop
										.getOp()));
							}
						}
					}
					opStack.push(operation);
				}
				break;
			}
		}
		if (eleBuilder.toString().length() > 0) {
			portFormulaList.add(String.valueOf(eleBuilder.toString()));
		}
		while (!opStack.isEmpty()) {
			portFormulaList.add(String.valueOf(opStack.pop().getOp()));
		}
		return portFormulaList;
	}
	
	/**
	 * 后缀表达式计算
	 * @param postFormulaList
	 * @return
	 */
	public static BigDecimal calPostFormula(List<String> postFormulaList){
		Stack<BigDecimal> numStack = new Stack<BigDecimal>();
		Operation operation;
		BigDecimal numA;
		BigDecimal numB;
		BigDecimal result;
		for (String str : postFormulaList) {
			if(str.length()==1){
				operation = Operation.getOperation(str.charAt(0));
				if (null == operation) {
					numStack.push(new BigDecimal(str));
				}else{
					numB = numStack.pop();
					numA = numStack.pop();
					result = Operation.cal(numA, numB, str.charAt(0));
					numStack.push(result);
				}
			}else{
				numStack.push(new BigDecimal(str));
			}
		}
		return numStack.pop();
	}
	
	public static void main(String[] args) {
		// 9 3 1-3*+ 10 2/+
		String forluma = "9+(3-1)*3+10/2";
		System.out.println(StringUtils.join(inToPostFormula(forluma), " "));
		System.out.println(calPostFormula(inToPostFormula(forluma)));

	}
}
