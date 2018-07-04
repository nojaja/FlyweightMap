package com.github.nojaja;

import java.io.Serializable;
import java.util.*;
import java.lang.Cloneable;


public class FlyweightArrayList<E>  extends ArrayList<E>
implements List<E>, RandomAccess, Cloneable, java.io.Serializable {

	private List<E> sourceList;

	//////////////////////////////////////
	@SuppressWarnings("unused")
	private FlyweightArrayList(int initialCapacity) {
		super(initialCapacity);
	}

	private FlyweightArrayList() {
		super();
	}

	private FlyweightArrayList(Collection<? extends E> c) {
		super(c);
	}

	///////////////////////////////////////

	public FlyweightArrayList(List<E> source) {
		//		super(source.size());
		//		this.sourceList = source;
		//		for (MyObject o : srcList) {
		//		for (int p = 0; p < masterList.size(); p++) {
		//			super.add(null);
		//		}
		//				
		super(source);
		this.sourceList = source;
	}


	///////////////////////////////////////

	@Override
	public E get(int index) {
		Object result = super.get(index);

		if(result instanceof Map){
			result = new FlyweightHashMap((Map)result);
			super.set(index, (E) result);
		}else if(result instanceof List){
			result = new FlyweightArrayList((List)result);
			super.set(index, (E) result);
		}
		return (E)result;
	}

}
