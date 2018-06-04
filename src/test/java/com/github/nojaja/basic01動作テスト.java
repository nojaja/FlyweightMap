package com.github.nojaja;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.arnx.jsonic.JSON;


public class basic01動作テスト {




	@Test
	public void entrySet() throws Exception {
		System.out.println("=========================================");
		Util.getMethodName();
		String dir = System.getProperty("user.dir");
		//HashMap inputdata = Util.readJson(dir+"/MOCK_DATA.json");
		HashMap<?, ?> inputdata = Util.readJson(dir+"/amedas-rain-1h-recent.json");

		FlyweightLinkedHashMap<String, Object> data1 = new FlyweightLinkedHashMap<String, Object>((Map)inputdata);

		Util.begin("keySet");
		try {
			// 遅い       
			for (String key : data1.keySet()) {
				data1.get(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		Util.end();

		Util.begin("entrySet");
		try {
			// 早い
			for (FlyweightLinkedHashMap.Entry<String, Object> entry : data1.entrySet()) {
				entry.getValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}

		Util.end();

		Util.printFreeMemory();
		System.out.println("=========================================");

	}

	@Test
	public void cloneした場合と同じデータになっていること() throws Exception {
		System.out.println("=========================================");
		Util.getMethodName();
		String dir = System.getProperty("user.dir");
		//HashMap inputdata = Util.readJson(dir+"/MOCK_DATA.json");
		HashMap<?, ?> inputdata = Util.readJson(dir+"/amedas-rain-1h-recent.json");
		Util.begin("");

		FlyweightLinkedHashMap data1 = new FlyweightLinkedHashMap(inputdata);
		HashMap data2 = (HashMap) inputdata.clone();

		String text1=JSON.encode(data1); 
		String text2=JSON.encode(data2); 

		assertTrue(text1.compareTo(text2)==0);
		Util.end();

		Util.printFreeMemory();
		System.out.println("=========================================");

	}
	@Test
	public void PutGetの動作確認() throws Exception {
		System.out.println("=========================================");
		Util.getMethodName();
		String dir = System.getProperty("user.dir");
		HashMap<?, ?> inputdata = Util.readJson(dir+"/testData_CustInqDispMobile.json");

		FlyweightLinkedHashMap test = new FlyweightLinkedHashMap(inputdata);
		HashMap div_frame_hedder = (HashMap) test.get("div_frame_hedder");
		String customClass1 = (String) div_frame_hedder.get("customClass1");

		System.out.println(customClass1); 
		div_frame_hedder.put("customClass1","hoge");

		HashMap div_frame_hedder2 = (HashMap) test.get("div_frame_hedder");
		String customClass12 = (String) div_frame_hedder2.get("customClass1");

		System.out.println(customClass12); 

		assertTrue(customClass12.equals("hoge"));
		Util.end();
		System.out.println("=========================================");
	}

	@Test
	public void Putでフォークされるか動作確認() throws Exception {
		System.out.println("=========================================");
		Util.getMethodName();
		String dir = System.getProperty("user.dir");
		HashMap<?, ?> inputdata = Util.readJson(dir+"/testData_CustInqDispMobile.json");

		FlyweightLinkedHashMap test = new FlyweightLinkedHashMap(inputdata);
		String customClass1;
		String customClass12;
		String customClass13;
		String customClass14;
		{
			HashMap div_frame_hedder = (HashMap) test.get("div_frame_hedder");
			customClass1 = (String) div_frame_hedder.get("customClass1");
			System.out.println("先変更前："+customClass1);
		}
		{
			HashMap div_frame_hedder = (HashMap) test.get("div_frame_hedder");
			div_frame_hedder.put("customClass1","hoge");
		}
		{
			HashMap div_frame_hedder2 = (HashMap) test.get("div_frame_hedder");
			customClass12 = (String) div_frame_hedder2.get("customClass1");
		}
		{
			HashMap div_frame_hedder3 = (HashMap) inputdata.get("div_frame_hedder");
			customClass13 = (String) div_frame_hedder3.get("customClass1");
		}

		System.out.println("先変更後："+customClass12); 
		System.out.println("元変更後："+customClass13); 

		assertTrue(customClass1==customClass13);

		assertTrue(customClass12.equals("hoge"));
		assertTrue(!customClass13.equals("hoge"));

		Util.end();
		System.out.println("=========================================");
	}

	@Test
	public void 親が変更された場合の動作確認() throws Exception {
		System.out.println("=========================================");
		Util.getMethodName();
		String dir = System.getProperty("user.dir");
		HashMap inputdata = Util.readJson(dir+"/testData_CustInqDispMobile.json");

		FlyweightLinkedHashMap test = new FlyweightLinkedHashMap(inputdata);
		HashMap div_frame_hedder = (HashMap) test.get("div_frame_hedder");
		String customClass1 = (String) div_frame_hedder.get("customClass1");

		System.out.println("先変更前："+customClass1); 
		HashMap div_frame_hedder3 = (HashMap) inputdata.get("div_frame_hedder");
		div_frame_hedder3.put("customClass1","hoge");

		HashMap div_frame_hedder2 = (HashMap) test.get("div_frame_hedder");
		String customClass12 = (String) div_frame_hedder2.get("customClass1");

		String customClass13 = (String) div_frame_hedder3.get("customClass1");

		System.out.println("元変更後："+customClass13); 
		System.out.println("先変更後："+customClass12); 

		assertTrue(customClass12.equals("hoge"));
		assertTrue(customClass13.equals("hoge"));

		Util.end();
		System.out.println("=========================================");
	}

	@Test
	public void 要素が配列の場合の動作確認() throws Exception {
		System.out.println("=========================================");
		Util.getMethodName();
		String dir = System.getProperty("user.dir");
		HashMap<?, ?> inputdata = Util.readJson(dir+"/testData_CustInqDispMobile.json");

		FlyweightLinkedHashMap test = new FlyweightLinkedHashMap(inputdata);
		{
			ArrayList options = (ArrayList) test.get("options");
			HashMap option = (HashMap) options.get(0);
			String value1 = (String) option.get("value");
			System.out.println("先変更前："+value1); 
		}
		{
			ArrayList options = (ArrayList) inputdata.get("options");
			HashMap option = (HashMap) options.get(0);
			String value1 = (String) option.get("value");
			System.out.println("元変更前："+value1); 
		}
		{
			ArrayList options = (ArrayList) test.get("options");
			HashMap option = (HashMap) options.get(0);
			option.put("value","hoge"); 
		}
		String value1;
		{
			ArrayList options = (ArrayList) test.get("options");
			HashMap option = (HashMap) options.get(0);
			value1 = (String) option.get("value");
			System.out.println("先変更後："+value1); 
		}
		String value2;
		{
			ArrayList options = (ArrayList) inputdata.get("options");
			HashMap option = (HashMap) options.get(0);
			value2 = (String) option.get("value");
			System.out.println("元変更後："+value2); 
		}

		assertTrue("元と先で値が変わっている事",!value1.equals(value2));
		Util.end();
		System.out.println("=========================================");
	}

}
