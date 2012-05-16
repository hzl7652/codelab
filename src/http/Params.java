package http;

import java.util.Map.Entry;

public class Params<K,V> {

	private int size=0;
	private Entry<K,V>[] entrys = new Entry[10];
	
	public int size(){
		return size;
	}
	public void put(K key,V value){
		ensureCapity(size+1);
		entrys[size++] = new MyEntry<K,V>(key, value);
	}
	public Entry<K,V> get(int index){
		checkRange(index);
		return entrys[index];
	}
	private void checkRange(int index){
		if(index<0 && index>= size){
			throw new IndexOutOfBoundsException("index:"+index+",size:"+size);
		}
	}
	private void ensureCapity(int newSize){
		if(newSize> entrys.length){
			Entry<K,V>[] newentrys = new Entry[2*entrys.length];
			System.arraycopy(entrys, 0, newentrys, 0, entrys.length);
			entrys = newentrys;
		}
	}

}

class MyEntry<K,V> implements Entry<K, V>{
	
	private K key;
	private V value;
	public MyEntry(K key,V value){
		this.key = key;
		this.value = value;
	}
	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		// TODO Auto-generated method stub
		return value;
	}
	@Override
	public V setValue(V value) {
		V oldValue = this.value;
		this.value = value;
		return oldValue;
	}
	
}
