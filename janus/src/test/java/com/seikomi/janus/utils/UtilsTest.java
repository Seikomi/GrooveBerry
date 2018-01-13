package com.seikomi.janus.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.seikomi.janus.utils.Utils.Pair;

public class UtilsTest {

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testIsInteger() {
		String stringTest = "";
		assertFalse(Utils.isInteger(stringTest));

		stringTest = "-";
		assertFalse(Utils.isInteger(stringTest));

		stringTest = "-1";
		assertTrue(Utils.isInteger(stringTest));

		stringTest = "1";
		assertTrue(Utils.isInteger(stringTest));
	}

	@Test
	public void testBytesToLong() {
		byte[] bytesTest = new byte[] { 0b1001100, 0b1001101, 0b1001100, 0b1001101, 0b1001100, 0b1001101, 0b1001100,
				0b1001101 };
		long longExpected = 5498134614965570637L;
		
		assertEquals(longExpected, Utils.bytesToLong(bytesTest));
		
	}

	@Test
	public void testLongToBytes() {
		long longTest = 5498134614965570637L;
		byte[] bytesExpexted = new byte[] { 0b1001100, 0b1001101, 0b1001100, 0b1001101, 0b1001100, 0b1001101, 0b1001100,
				0b1001101 };
		byte[] bytesActual = Utils.longToBytes(longTest);
		for (int i = 0; i < bytesExpexted.length ; i++) {
			assertEquals(bytesExpexted[i], bytesActual[i]);
		}
	}
	
	@Test
	public void testPairClass() {
		Pair<Boolean, String> pairTest = new Pair<Boolean, String>(true, "test");
		Pair<Boolean, String> pairEquals = new Pair<Boolean, String>(true, "test");
		Pair<Boolean, String> pairNotEqual = new Pair<Boolean, String>(false, "tset");
		String banane = "banane";
		
		assertEquals(pairEquals, pairTest);
		assertNotEquals(pairNotEqual, pairTest);
		assertNotEquals(banane, pairTest);
		assertNotEquals(pairTest, banane);
		
		assertEquals(pairTest.getLeft().hashCode() ^ pairTest.getRight().hashCode(), pairTest.hashCode());
	}

}
