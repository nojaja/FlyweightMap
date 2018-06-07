package com.github.nojaja;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class basic02パフォーマンステスト {

	static int loop_count = 500;
	@Test
	public void 大量に生成してもcloneした場合より早いこと() throws Exception {
		System.out.println("=========================================");
		Util.getMethodName();
		String dir = System.getProperty("user.dir");
		//HashMap inputdata = Util.readJson(dir+"/MOCK_DATA.json");
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
		Long m1 = Util.printFreeMemory();
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
		Long m2 = Util.printFreeMemory();
		System.out.println("=========================================");
		///////////////		
		assertTrue(t1<t2);
		assertTrue(m1<m2);

		//fail("Not yet implemented");
	}

	@Test
	public void 大量にgetしても早いこと() throws Exception {
		String dir = System.getProperty("user.dir");
		System.out.println("=========================================");
		Util.getMethodName();
		{
			HashMap inputdata = new HashMap<>();
			inputdata.put("key", "hoge");

			Runtime.getRuntime().gc();
			Util.begin("HashMap");
			for (int i = 0; i < loop_count; i++) {
				inputdata.get("key");
			}
			Util.end();

			Runtime.getRuntime().gc();
			Util.begin("DifferenceLinkedHashMap");
			FlyweightLinkedHashMap dm = new FlyweightLinkedHashMap(inputdata);
			for (int i = 0; i < loop_count; i++) {
				dm.get("key");
			}
			Util.end();

			Util.printFreeMemory();
		}
		System.out.println("=========================================");

		{
			HashMap inputdata = Util.readJson(dir+"/testData_CustInqDispMobile.json");
			inputdata.put("key", "hoge");

			Runtime.getRuntime().gc();
			Util.begin("HashMap");
			for (int i = 0; i < loop_count; i++) {
				inputdata.get("key");
			}
			Util.end();

			Runtime.getRuntime().gc();
			Util.begin("DifferenceLinkedHashMap");
			FlyweightLinkedHashMap dm = new FlyweightLinkedHashMap(inputdata);
			for (int i = 0; i < loop_count; i++) {
				dm.get("key");
			}
			Util.end();

			Util.printFreeMemory();
		}
		

		//fail("Not yet implemented");
	}
}
