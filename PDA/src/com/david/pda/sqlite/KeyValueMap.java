package com.david.pda.sqlite;

import java.util.ArrayList;

public class KeyValueMap<K, V> {
	class Entry {
		K k;
		V v;

		public Entry(K k, V v) {
			this.k = k;
			this.v = v;
		}

		public K getK() {
			return k;
		}

		public V getV() {
			return v;
		}

		public void setK(K k) {
			this.k = k;
		}

		public void setV(V v) {
			this.v = v;
		}
	}

	private ArrayList<Entry> keyValue = new ArrayList<Entry>();

	public boolean put(K k, V v) {
		if (k == null || v == null)
			return false;
		Entry e = new Entry(k, v);
		keyValue.add(e);
		return true;
	}

	public K getKey(int id) {
		Entry e = keyValue.get(id);
		if (e == null)
			return null;
		else
			return e.getK();
	}

	public V getValue(int id) {
		Entry e = keyValue.get(id);
		if (e == null)
			return null;
		else
			return e.getV();
	}

	public int getSize() {
		return keyValue.size();
	}
}
