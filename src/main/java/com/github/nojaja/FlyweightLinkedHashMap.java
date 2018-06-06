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
	private Set<K> removeKs = new LinkedHashSet<K>();

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

		if(removeKs.contains(key)) return null;

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
    public int size() {
        return this.keySet().size();
    }
	
	@Override
	public void clear() {
		super.clear();
		sourceMap = new LinkedHashMap<K,V>();
	}

	@Override
	public V put(K key, V value) {
		removeKs.remove(key);
		return super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		removeKs.removeAll(m.keySet());
		super.putAll(m);
	}

	@Override
	public V remove(Object key) {
		if(super.containsKey(key) || sourceMap.containsKey(key)){
			removeKs.add((K) key);
		}
		return super.remove(key);
	}

	@Override
	public boolean containsKey(Object key) {
		if(removeKs.contains(key)) return false ;//if remove key then false
		if(super.containsKey(key)) return true;
		if(sourceMap.containsKey(key)) return true;
		return false;
	}

	@Override
	public Set<K> keySet() {
		Set<K> sourceKs = sourceMap.keySet();
		Set<K> ks = new LinkedHashSet<K>();
		ks.addAll(sourceKs);
		ks.addAll(super.keySet());
		ks.removeAll(this.removeKs);
		return (Set<K>) ks;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K,V>> sourceEs = sourceMap.entrySet();
		Set<Map.Entry<K, V>> es = new LinkedHashSet<Map.Entry<K, V>>();
		es.addAll(sourceEs);
		es.addAll(super.entrySet());
		return (Set<Map.Entry<K, V>>) es;
	}

	@Override
	public String toString() {
		StringBuilder sb = null;
		for (java.util.Map.Entry<K, V> entry : this.sourceMap.entrySet()) {
			V value = super.get(entry.getKey());
			if (value == null) {
				value = entry.getValue();
			}
			if (sb == null) {
				sb = new StringBuilder("{");
			} else {
				sb.append(", ");
			}
			sb.append(entry.getKey()).append("=").append(value);
		}
		if (sb == null) {
			return "{}";
		} else {
			sb.append("}");
		}
		return sb.toString();
	}
}
