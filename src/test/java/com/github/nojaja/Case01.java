package com.github.nojaja;


import java.util.ArrayList;
import java.util.HashMap;

public class Case01 {

	static int loop_count = 500000;

	public static void main(String[] args) throws Exception {

		try {

			Util.printFreeMemory();
			
			Case01 case01 = new Case01();
			String dir = System.getProperty("user.dir");
	        System.out.println("Projectのトップレベルのパス： " + dir);
			//HashMap inputdata = Util.readJson(dir+"/MOCK_DATA.json");
			HashMap inputdata = Util.readJson(dir+"/amedas-rain-1h-recent.json");
			//String inputdatatext=JSON.encode(inputdata); 
			System.out.println("=========================================");
			///////////////
			{
				Runtime.getRuntime().gc();
				ArrayList pool = new ArrayList();
				Util.begin("");
				for (int i = 0; i < loop_count; i++) {
					pool.add(new FlyweightHashMap(inputdata));
					//DifferenceHashMap copydata1 = new DifferenceHashMap(inputdata);
					//String inputdatatext=JSON.encode(copydata1); 
				}
				Util.end();
				
				Util.printFreeMemory();
			}
			///////////////
			
			System.out.println("=========================================");
			///////////////
			/*
			{
				Util.begin();
				DifferenceHashMap test = new DifferenceHashMap(inputdata);
				HashMap div_frame_hedder = (HashMap) test.get("data");
				String customClass1 = (String) div_frame_hedder.get("customClass1");
				System.out.println(customClass1); 
				div_frame_hedder.put("customClass1","hoge");

				HashMap div_frame_hedder2 = (HashMap) test.get("div_frame_hedder");
				String customClass12 = (String) div_frame_hedder2.get("customClass1");
				System.out.println(customClass12); 
				
				Util.end();
				Util.printFreeMemory();
			}
			*/
			///////////////		
			
			System.out.println("=========================================");

			{
				Runtime.getRuntime().gc();
				ArrayList pool = new ArrayList();
				Util.begin("");
				for (int i = 0; i < loop_count; i++) {
					pool.add(inputdata.clone());
				}
				/*
				HashMap test = (HashMap) inputdata.clone();
				System.out.println(test.get("div_frame_hedder")); 
				test.put("div_frame_hedder","hoge");
				System.out.println(test.get("div_frame_hedder")); 
				*/
				
				//String inputdatatext=JSON.encode(copydata); 
				Util.end();
				Util.printFreeMemory();
			}
			///////////////

			System.out.println("=========================================");
			///////////////


		} catch (RuntimeException ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		} finally {
			// TODO: handle finally clause
			System.out.println("end");
		}


	}



}
