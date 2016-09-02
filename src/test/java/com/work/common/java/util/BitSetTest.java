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
		
	}
}