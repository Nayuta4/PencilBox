package pencilbox.common.core;

import java.util.*;



/**
 * �̈�N���X
 * �����̃A�h���X�̏W��
 */
public class Area implements Set<Address> {
	
	private Set<Address> cellList;
	
	/**
	 * �R���X�g���N�^
	 */
	public Area() {
		cellList = new TreeSet<Address>();
	}
	public String toString() {
		return cellList.toString();	
	}
	/**
	 * �Z����̈�ɒǉ�����
	 * @param o
     * @return <tt>true</tt> if the collection changed as a result of the call.
	 */
	public boolean add(Address o) {
		return cellList.add(o);
	}
	/**
	 * �Z����̈�ɒǉ�����
	 * @param r
	 * @param c
     * @return <tt>true</tt> if this set did not already contain the specified
     *         element.
	 */
	public boolean add(int r, int c) {
		return cellList.add(new Address(r, c));
	}

	public int size() {
		return cellList.size(); 
	}
	public Iterator<Address> iterator() {
		return cellList.iterator(); 
	}
	public boolean contains(Object o) {
		return cellList.contains(o);
	}

	public boolean remove(Object o) {
		return cellList.remove(o);
	}
	
	/**
	 * �Z����̈悩���菜��
	 * @param r
	 * @param c
     * @return true if the set contained the specified element.
	 */
	public boolean remove(int r, int c) {
		return cellList.remove(new Address(r, c));
	}

	public boolean addAll(Collection<? extends Address> c) {
		return cellList.addAll( c );
	}

	public boolean containsAll(Collection<?> c) {
		return cellList.containsAll(c);
	}

	public boolean removeAll(Collection<?> c) {
		return cellList.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return cellList.retainAll(c);
	}

	public<T> T[] toArray(T[] a) {
		return cellList.toArray(a);
	}

	public void clear() {
		cellList.clear();
	}

	public boolean isEmpty() {
		return cellList.isEmpty();
	}
	public Object[] toArray() {
		return cellList.toArray();
	}

}
