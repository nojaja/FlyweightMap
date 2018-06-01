package com.github.nojaja;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import net.arnx.jsonic.JSON;

public class Util {

	public static HashMap<?, ?> readJson(String path) throws Exception {
		HashMap<?, ?> result;
		File file = new File(path);
		if (!file.exists()) {
			throw new Exception("json [" + file.toPath().toString() + "] not exists");
		}

		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			result = JSON.decode(in);
			try{
				if (in != null) {
					in.close();
				}
			}catch (Exception localException1) {}


		} catch (Exception e){
			throw new Exception("json read error", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			}catch (Exception localException2) {}
		}
		return result;
	}

	public static String getMethodName() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        System.out.println(methodName);
        return methodName;
    }
	
	public static long starttime=System.nanoTime();

	public static final void begin(String msg) {
		System.out.println("="+msg+"========================================");
		starttime = System.nanoTime();
	}
	public static final long end() {
		long endtime = System.nanoTime();
		long time = (endtime-starttime);
		System.out.println("time:"+ time + "ns");
		return time;
	}
	
	public static final void printFreeMemory() {
		long total = Runtime.getRuntime().totalMemory();
		System.out.println("total => " + total / 1024 + "KB");
		long free = Runtime.getRuntime().freeMemory();
		long used = total - free;
		long max = Runtime.getRuntime().maxMemory();
		System.out.println("free  => " + free / 1024 + "KB");
		System.out.println("used  => " + (total - free) / 1024 + "KB");
		System.out.println("max   => " + max / 1024 + "KB");
	}
}
