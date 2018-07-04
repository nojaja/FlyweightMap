package com.github.nojaja;

import java.io.Serializable;
import java.util.*;
import java.lang.Cloneable;


public class FlyweightHashMap<K,V>  extends HashMap<K,V> implements Map<K, V>,Cloneable,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7968588409646934881L;
	private Map<K, V> sourceMap;
	private Set<K> removeKs = new HashSet<K>();

	//////////////////////////////////////
	@SuppressWarnings("unused")
	private FlyweightHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	@SuppressWarnings("unused")
	private FlyweightHashMap(int initialCapacity) {
		super(initialCapacity);
	}

	@SuppressWarnings("unused")
	private FlyweightHashMap() {
		super();
	}

	/*
	private DifferenceHashMap(Map<? extends K, ? extends V> m) {
		super(m);
	}
	 */
	///////////////////////////////////////

	public FlyweightHashMap(Map<K, V> source,int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		this.sourceMap = source;
	}

	public FlyweightHashMap(Map<K, V> source,int initialCapacity) {
		super(initialCapacity);
		this.sourceMap = source;
	}

	public FlyweightHashMap(Map<K, V> source) {
		super();
		this.sourceMap = source;
	}

	public FlyweightHashMap(Map<K, V> source,Map<? extends K, ? extends V> m) {
		super(m);
		this.sourceMap = source;
	}


	public V get(Object key) {
		Object result = super.get(key);
		if(result!=null){
			return (V)result;
		}

		if(this.removeKs.contains(key)) return null;

		result = (V) this.sourceMap.get(key);

		if(result instanceof Map){
			result = new FlyweightHashMap<K,V>((Map<K, V>)result);
			V put = super.put((K) key, (V)result);
		}else if(result instanceof List){
			result = new FlyweightArrayList((List)result);
			V put = super.put((K) key, (V)result);
		}
		return (V)result;
	}
	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}
	@Override
	public int size() {
		return this.keySet().size();
	}

	@Override
	public void clear() {
		super.clear();
		this.removeKs.clear();
		this.sourceMap = new HashMap<K,V>();
	}

	@Override
	public V put(K key, V value) {
		this.removeKs.remove(key);
		

		if(value instanceof Map){
			value = (V) new FlyweightHashMap<K,V>((Map<K, V>)value);
			return super.put((K) key, (V)value);
		}else if(value instanceof List){
			value = (V) new FlyweightArrayList((List)value);
			return super.put((K) key, (V)value);
		}
		return super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		this.removeKs.removeAll(m.keySet());
		super.putAll(m);
	}

	@Override
	public V remove(Object key) {
		if(super.containsKey(key) || this.sourceMap.containsKey(key)){
			this.removeKs.add((K) key);
		}
		return super.remove(key);
	}

	@Override
	public boolean containsKey(Object key) {
		if(this.removeKs.contains(key)) return false ;//if remove key then false
		if(super.containsKey(key)) return true;
		if(this.sourceMap.containsKey(key)) return true;
		return false;
	}
	

	@Override
    public boolean containsValue(Object value) {
		return super.containsValue(value);
    	//todo
    }

	@Override
	public Set<K> keySet() {
		Set<K> sourceKs = this.sourceMap.keySet();
		Set<K> ks = new HashSet<K>();
		ks.addAll(sourceKs);
		ks.addAll(super.keySet());
		ks.removeAll(this.removeKs);
		return (Set<K>) ks;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K,V>> sourceEs = this.sourceMap.entrySet();
		Set<Map.Entry<K, V>> es = new HashSet<Map.Entry<K, V>>();
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
