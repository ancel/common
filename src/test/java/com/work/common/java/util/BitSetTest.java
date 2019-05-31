package com.work.common.java.util;
import java.util.BitSet;

public class BitSetTest {

	public static void main(String[] args) {
		int bitIndex = 127;
		BitSet bitSet = new BitSet();
		
		for (long  index: bitSet.toLongArray()) {
			System.out.println(index);
		}
		System.out.println("--------------");
		bitSet.set(bitIndex);
		System.out.println(bitIndex >> 6);
		System.out.println(1L << bitIndex);
		System.out.println("--------------");
		bitIndex = 126;
		System.out.println("--------------");
//		bitSet.set(bitIndex);
		System.out.println(bitIndex >> 6);
		System.out.println(1L << bitIndex);
		System.out.println("--------------");
		
		for (long  index: bitSet.toLongArray()) {
			System.out.println(index);
		}
		
		System.out.println(bitSet.get(126));
		
		long a = -9223372036854775808L;
		long b = 4611686018427387904L;
		a |=b;
		System.out.println(a);
		
		System.out.println(3>>6);
		System.out.println(2>>6);
		System.out.println(124|99);
		
		System.out.println(0|0);
		System.out.println(0|1);
		System.out.println(1|0);
		System.out.println(1|1);
		
		System.out.println("-----------");
		System.out.println(0&0);
		System.out.println(0&1);
		System.out.println(1&0);
		System.out.println(1&1);
		System.out.println(Long.MAX_VALUE);
		System.out.println(1L<<64);
		System.out.println(1L<<128);
		System.out.println(127&99);
	}
}