package pencilbox.common.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;



/**
 * リンククラス
 * ひとつながりの線がひとつのLinkインスタンスとなる
 */
public class Link implements Set {
	
	private static int NEXT_ID = 1;
	private Set jointSet;
	private int id;

	public static void resetID() {
		NEXT_ID = 1;
	}
	/**
	 * コンストラクタ
	 */
	public Link() {
		jointSet = new TreeSet();
		id = NEXT_ID++;
		if (NEXT_ID == Integer.MAX_VALUE) NEXT_ID = 1;
	}

	/**
	 *  Link番号取得
	 */
	public int getID() {
		return id;
	}

	/**
	 * Link番号設定
	 */
	public void setID(int i) {
		id = i;
	}

	public int size() {
		return jointSet.size();
	}

	public void clear() {
		jointSet.clear();
	}

	public boolean isEmpty() {
		return jointSet.isEmpty();
	}

	public Object[] toArray() {
		return jointSet.toArray();
	}
	/**
	 * リンクに線分を追加する
	 */
	public boolean add(Object o) {
		return jointSet.add(o);
	}
	/**
	 * リンクに線分を追加する
	 * @param d
	 * @param r
	 * @param c
     * @return <tt>true</tt> if this set did not already contain the specified
     *         element.
	 */
	public boolean add(int d, int r, int c) {
		return jointSet.add(new SideAddress(d,r,c));
	}

	public boolean contains(Object o) {
		return jointSet.contains(o);
	}

	public boolean remove(Object o) {
		return jointSet.remove(o);
	}

	public boolean addAll(Collection c) {
		return jointSet.addAll( ((Link) c).jointSet);
	}

	public boolean containsAll(Collection c) {
		return jointSet.containsAll(c);
	}

	public boolean removeAll(Collection c) {
		return jointSet.removeAll(c);
	}

	public boolean retainAll(Collection c) {
		return jointSet.retainAll(c);
	}

	public Iterator iterator() {
		return jointSet.iterator();
	}

	public Object[] toArray(Object[] a) {
		return jointSet.toArray(a);
	}
	public String toString() {
		return jointSet.toString();	
	}


}
	
