package com.github.nojaja;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.arnx.jsonic.JSON;


public class basic01動作テスト {

	@Test
	public void clearテスト() {
		System.out.println("=========================================");
		Util.getMethodName();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("aaa", "bbb");
		data.put("ccc", "ccc");
		
		FlyweightLinkedHashMap<String, Object> fmap = new FlyweightLinkedHashMap<String, Object>(data);
		
		fmap.clear();
		assertFalse("キーを更新したあと、clearしたら消えてること",fmap.containsKey("ccc"));
		assertTrue("キーを更新したあと、clearしても親は残ってること",data.containsKey("ccc"));
		
		fmap.put("aaa", "zzz");
		fmap.clear();
		assertFalse("キーを更新したあと、clearしたら消えてること",fmap.containsKey("aaa"));
		assertTrue("キーを更新したあと、clearしても親は残ってること",data.containsKey("aaa"));

		assertEquals("clearしたら、0件になってること",0, fmap.size());

	}
	
	@Test
	public void sizeテスト() {
		System.out.println("=========================================");
		Util.getMethodName();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("aaa", "bbb");
		data.put("ccc", "ccc");
		
		FlyweightLinkedHashMap<String, Object> fmap = new FlyweightLinkedHashMap<String, Object>(data);

		assertFalse("作成直後でも空でないこと",fmap.isEmpty());
		
		fmap.put("bbb", "bbb");
		assertEquals("sizeで合計件数が取れること",3,fmap.size());
		assertEquals("親の件数が変わってないこと",2,data.size());
		
		fmap.put("aaa", "zzz");
		assertEquals("キーを更新したあと、sizeで合計件数が取れること",3,fmap.size());

		fmap.clear();
		assertEquals("clearしたら、0件になってること",0, fmap.size());

	}
	@Test
	public void removeテスト() {
		System.out.println("=========================================");
		Util.getMethodName();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("aaa", "bbb");
		data.put("ccc", "ccc");
		data.put("ddd", "ddd");
		data.put("eee", "eee");
		
		FlyweightLinkedHashMap<String, Object> fmap = new FlyweightLinkedHashMap<String, Object>(data);
		fmap.put("aaa", "zzz");
		fmap.put("fff", "fff");
		fmap.remove("aaa");
		assertFalse("キーを更新したあと、削除したら消えてること",fmap.containsKey("aaa"));
		assertEquals("sizeでの件数があってること",4,fmap.size());
		assertTrue("キーを更新したあと、削除しても親は残ってること",data.containsKey("aaa"));

		fmap.put("aaa", "zzz");
		assertTrue("消したキーをもう一度追加したら見えること",fmap.containsKey("aaa"));
		assertEquals("sizeでの件数があってること",5,fmap.size());
		
		fmap.put("bbb", "bbb");
		fmap.remove("bbb");
		assertFalse("キーを追加して削除したら消えてること",fmap.containsKey("bbb"));
		assertEquals("sizeでの件数があってること",5,fmap.size());

		data.put("bbb", "bbb");
		assertFalse("消したキーを親が追加しても消えていること",fmap.containsKey("bbb"));
		assertEquals("sizeでの件数があってること",5,fmap.size());
		

		data.remove("ccc");
		assertFalse("親がキーを削除したら消えてること",fmap.containsKey("ccc"));
		assertEquals("sizeでの件数があってること",4,fmap.size());

	}
	
	@Test
	public void KeySetテスト() {
		System.out.println("=========================================");
		Util.getMethodName();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("aaa", "bbb");
		data.put("bbb", "bbb");
		
		FlyweightLinkedHashMap<String, Object> fmap = new FlyweightLinkedHashMap<String, Object>(data);
		fmap.put("aaa", "zzz");
		fmap.put("ccc", "zzz");
		Set<String> keys = fmap.keySet();
		assertEquals(3, keys.size());
		
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			System.out.println(iterator.next());
		}

	}
	@Test
	public void entrySetテスト() throws Exception {
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
		HashMap<String, Object> inputdata = (HashMap<String, Object>) Util.readJson(dir+"/testData_CustInqDispMobile.json");

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
			HashMap<String,String> option = (HashMap<String,String>) options.get(0);
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
	
	@Test
	public void toStringテスト() throws Exception {
		System.out.println("=========================================");
		Util.getMethodName();
		String dir = System.getProperty("user.dir");
		HashMap<String, Object> inputdata = (HashMap<String, Object>) Util.readJson(dir+"/MOCK_DATA.json");

		inputdata.put("aaa", "bbb");
		inputdata.put("bbb", "bbb");
		
		FlyweightLinkedHashMap<String, Object> test = new FlyweightLinkedHashMap<String, Object>(inputdata);
		System.out.println(inputdata.toString());
		System.out.println(test.toString());
		
		assertEquals(inputdata.toString(), test.toString());
	}
	
}
