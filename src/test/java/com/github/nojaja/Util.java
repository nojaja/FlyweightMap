package com.github.nojaja;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import net.arnx.jsonic.JSON;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import com.sun.management.HotSpotDiagnosticMXBean;

public class Util {
	private static final String HOTSPOT_BEAN_NAME =
	         "com.sun.management:type=HotSpotDiagnostic";
	    // field to store the hotspot diagnostic MBean 
	private static volatile HotSpotDiagnosticMXBean hotspotMBean;
	    
	public static void dumpHeap(String fileName, boolean live) {
        // initialize hotspot diagnostic MBean
        initHotspotMBean();
        try {
            hotspotMBean.dumpHeap(fileName, live);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }
    // initialize the hotspot diagnostic MBean field
    private static void initHotspotMBean() {
        if (hotspotMBean == null) {
            synchronized (Util.class) {
                if (hotspotMBean == null) {
                    hotspotMBean = getHotspotMBean();
                }
            }
        }
    }
    // get the hotspot diagnostic MBean from the
    // platform MBean server
    private static HotSpotDiagnosticMXBean getHotspotMBean() {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            HotSpotDiagnosticMXBean bean = 
                ManagementFactory.newPlatformMXBeanProxy(server,
                HOTSPOT_BEAN_NAME, HotSpotDiagnosticMXBean.class);
            return bean;
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }
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
	
	public static final long printFreeMemory() {

		/*
        // default heap dump file name
        String fileName = "heap.bin";
        // by default dump only the live objects
        boolean live = true;
        
		// dump the heap
        dumpHeap(fileName, live);
        */
		long total = Runtime.getRuntime().totalMemory();
		System.out.println("total => " + total / 1024 + "KB");
		long free = Runtime.getRuntime().freeMemory();
		long used = total - free;
		long max = Runtime.getRuntime().maxMemory();
		System.out.println("free  => " + free / 1024 + "KB");
		System.out.println("used  => " + used / 1024 + "KB");
		System.out.println("max   => " + max / 1024 + "KB");
		return used;
	}
}
