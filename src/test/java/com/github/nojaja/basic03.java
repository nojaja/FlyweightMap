package com.github.nojaja;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class basic03 {

	static int loop_count = 500;
	@Test
	public void cloneした場合より早いこと() throws Exception {
		System.out.println("=========================================");
		Util.getMethodName();
		String dir = System.getProperty("user.dir");
		HashMap inputdata = Util.readJson(dir+"/testData_CustInqDispMobile.json");

		Runtime.getRuntime().gc();
		{
			ArrayList pool = new ArrayList();
			Util.begin("DifferenceLinkedHashMap");
			for (int i = 0; i < loop_count; i++) {
				pool.add(new FlyweightLinkedHashMap(inputdata));
			}
		}
		Long t1 = Util.end();
		Util.printFreeMemory();
		Runtime.getRuntime().gc();
		System.out.println("=========================================");
		{
			ArrayList pool = new ArrayList();
			Util.begin("clone");
			for (int i = 0; i < loop_count; i++) {
				pool.add(inputdata.clone());
			}
		}
		Long t2 = Util.end();
		Util.printFreeMemory();
		System.out.println("=========================================");
		///////////////		
		assertTrue(t1<t2);
		//fail("Not yet implemented");
	}

}
