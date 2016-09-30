package com.work.common.utils.Math;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Arith {
    
    /**
     * 两个Double数相加
     * @param v1
     * @param v2
     * @return Double
     */
    public static double add(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.add(b2).doubleValue();
    }
    
    /**
     * 两个Double数相减
     * @param v1
     * @param v2
     * @return Double
     */
    public static double sub(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }
    
    /**
     * 两个Double数相乘
     * @param v1
     * @param v2
     * @return Double
     */
    public static double mul(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2).doubleValue();
    }
    
    /**
     * 两个Double数相除
     * @param v1
     * @param v2
     * @return Double
     */
    public static double div(Double v1,Double v2,int scale, RoundingMode roundingMode){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2,scale,roundingMode).doubleValue();
    }
    
    /**  
     * 对double数据进行取精度.  
     * @param value  double数据.  
     * @param scale  精度位数(保留的小数位数).  
     * @param roundingMode  精度取值方式.  
     * @return 精度计算后的数据.  
     */  
    public static double round(Double value, int scale, 
             int roundingMode) {   
        BigDecimal bd = new BigDecimal(value.toString());   
        bd = bd.setScale(scale, roundingMode);   
        return bd.doubleValue();   
    }  
    
}