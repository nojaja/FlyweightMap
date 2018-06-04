package com.github.nojaja;

import java.io.Serializable;
import java.util.*;
import java.lang.Cloneable;


public class FlyweightLinkedHashMap<K,V>  extends LinkedHashMap<K,V> implements Map<K, V>,Cloneable,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7968588409646934881L;
	private Map<K, V> sourceMap;

	//////////////////////////////////////
	@SuppressWarnings("unused")
	private FlyweightLinkedHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	@SuppressWarnings("unused")
	private FlyweightLinkedHashMap(int initialCapacity) {
		super(initialCapacity);
	}

	@SuppressWarnings("unused")
	private FlyweightLinkedHashMap() {
		super();
	}

	/*
	private DifferenceHashMap(Map<? extends K, ? extends V> m) {
		super(m);
	}
	 */
	///////////////////////////////////////

	public FlyweightLinkedHashMap(Map<K, V> source,int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		sourceMap = source;
	}

	public FlyweightLinkedHashMap(Map<K, V> source,int initialCapacity) {
		super(initialCapacity);
		sourceMap = source;
	}

	public FlyweightLinkedHashMap(Map<K, V> source) {
		super();
		sourceMap = source;
	}

	public FlyweightLinkedHashMap(Map<K, V> source,Map<? extends K, ? extends V> m) {
		super(m);
		sourceMap = source;
	}


	public V get(Object key) {
		Object result = super.get(key);

		if(result!=null){
			return (V)result;
		}
		result = (V) sourceMap.get(key);

		if(result instanceof Map){
			result = new FlyweightLinkedHashMap<K,V>((Map<K, V>)result);
			V put = super.put((K) key, (V)result);
		}else if(result instanceof List){
			result = new FlyweightArrayList((List)result);
			V put = super.put((K) key, (V)result);
		}
		return (V)result;
	}

	@Override
	public void clear() {
		super.clear();

	}

	@Override
	public boolean containsKey(Object key) {
		boolean result = super.containsKey(key);
		if (!result) {
			result = sourceMap.containsKey(key);
		}
		return result;
	}

	public Set<K> keySet() {
		Set<K> ks= sourceMap.keySet();
		ks.addAll(super.keySet());
		return ks;
	}

	public Set<Map.Entry<K,V>> entrySet() {
		Set<Map.Entry<K,V>> srces = sourceMap.entrySet();
		Set<java.util.Map.Entry<K, V>> es = new HashSet<java.util.Map.Entry<K, V>>();
		es.addAll(srces);
		es.addAll(super.entrySet());
		return (Set<java.util.Map.Entry<K, V>>) es;
	}
}
