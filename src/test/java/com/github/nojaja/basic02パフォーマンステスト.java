package com.github.nojaja;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class basic02パフォーマンステスト {

	static int loop_count = 500000;
	@Test
	public void 大量に生成しても早いこと() throws Exception {
		System.out.println("=========================================");
		Util.getMethodName();
		String dir = System.getProperty("user.dir");
		//HashMap inputdata = Util.readJson(dir+"/MOCK_DATA.json");
		HashMap inputdata = Util.readJson(dir+"/testData_CustInqDispMobile.json");

		Runtime.getRuntime().gc();
		ArrayList pool = new ArrayList();
		Util.begin("");
		for (int i = 0; i < loop_count; i++) {
			pool.add(new FlyweightLinkedHashMap(inputdata));
		}
		Util.end();
		
		Util.printFreeMemory();
		System.out.println("=========================================");
		
		//fail("Not yet implemented");
	}
	
	@Test
	public void 大量にgetしても早いこと() throws Exception {
		System.out.println("=========================================");
		Util.getMethodName();
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
		System.out.println("=========================================");
		
		//fail("Not yet implemented");
	}
}
