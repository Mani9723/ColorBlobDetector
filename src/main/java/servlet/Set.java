package servlet;

import java.util.AbstractCollection;
import java.util.Iterator;

/**
 * Set class represents a collection of roots and their corresponding values
 * in a set. Each root represents Trees that hold data.
 * It is generic so the use cases are not limiting.
 *
 * @param <T> Generic Type
 */
public class Set<T> extends AbstractCollection<T>
{

	/**
	 * Holds the number of data in a set.
	 */
	private int numElem;
	/**
	 * Holds head pointer of a particular set.
	 */
	private Node<T> head;
	/**
	 * Holds tail pointer of a particular set.
	 */
	private Node<T> tail;

	/**
	 * Creates an empty set.
	 */
	public Set() {
		numElem = 0;
		head = tail = null;
	}

	/**
	 * Adds an item to the set.
	 *
	 * Throws NullPointerException if the item is null.
	 *
	 * @param item Item to add
	 * @return  True is added successfully
	 */
	public boolean add(T item) {
		if(item == null)
			throw new NullPointerException("Null element");

		Node<T> newNode = new Node<>(item);
		if(numElem == 0){
			this.head = this.tail = newNode;
		}else{
			this.tail.next = newNode;
			this.tail = newNode;
		}
		numElem++;
		return true;
	}

	/**
	 * Appends an entire set to another set.
	 *
	 * Throws NullPointerException is the set is null.
	 *
	 * @param other Another Set
	 * @return True is successfully added
	 */
	public boolean addAll(Set<T> other) {
		if(other == null)
			throw new NullPointerException("Null Set");
		if(head == null && tail == null){
			this.head = other.head;
			this.tail = other.tail;
		}else {
			this.tail.next = other.head;
			this.tail = other.tail;
		}
		this.numElem += other.numElem;
		return true;
	}

	/**
	 * Deletes the set.
	 */
	public void clear() {
		this.head = this.tail = null;
		numElem = 0;
	}

	/**
	 * Returns the number of elements in the set.
	 * @return Number of Elements
	 */
	public int size() {
		return numElem;
	}

	/**
	 * Creates an Iterator for the set.
	 * So the user may traverse the set safely.
	 *
	 * @return {@code Iterator<T>}
	 */
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			Node<T> current = head;

			public boolean hasNext() {
				return current != null;
			}

			public T next() {
				T value = current.data;
				current = current.next;
				return value;
			}
		};
	}

	/**
	 * Private Node class.
	 *
	 * @param <E> Generic Type
	 */
	private class Node<E>
	{
		/**
		 * Holds the node data.
		 */
		private E data;

		/**
		 * Pointer to the next Node.
		 */
		private Node<E> next;

		/**
		 * Creates a new Node with a data
		 * @param data Value to be added to a node
		 */
		private Node(E data){
			this.data = data;
		}
	}


	 //Main
	public static void main(String[] args) {
//		Set<Integer> set = new Set<>();
//		for(int i = 0; i<10;i++){
//			set.add(i+1);
//		}
//
//		for(Integer num : set){
//			System.out.println(num);
//		}
//
//		Set<Integer> set2 = new Set<>();
//		for(int i = 10; i < 20; i++){
//			set2.add(i+1);
//		}
//		System.out.println(set.addAll(set2));
//
//		System.out.println(set);
//		System.out.println("----------");
//		for(Integer num : set){
//			System.out.println(num);
//		}
//
//		set.clear();
//
//		try {
//			System.out.println(set.iterator().next());
//		}catch (NullPointerException e){
//			System.out.println("oopsie poopise");
//		}
//
//
//		Set<String> strSet = new Set<>();
//		strSet.add("Hello ");
//		strSet.add("Everybody ");
//		strSet.add("Data Structures ");
//		strSet.add("class ");
//		strSet.add("is ");
//		strSet.add("very ");
//		strSet.add("difficult ");
//
//		for(String string : strSet){
//			System.out.print(string);
//		}
//		System.out.println();
//
//		Set<String> strings = new Set<>();
//		strings.add("However ");
//		strings.add("study ");
//		strings.add("and ");
//		strings.add("it ");
//		strings.add("will ");
//		strings.add("not ");
//		strings.add("be ");
//		strings.add("so ");
//		strings.add("difficult ");
//
//		for(String string : strings){
//			System.out.print(string);
//		}
//		System.out.println();
//
//		System.out.println(strSet.addAll(strings));
//
//		System.out.println(strSet);
//		Iterator<String> stringIterator = strSet.iterator();
//
//		while(stringIterator.hasNext()){
//			System.out.print(stringIterator.next());
//		}
//		System.out.println();
//
//		Iterator<String> stringIterator2 = strings.iterator();
//
//		while(stringIterator2.hasNext()){
//			System.out.print(stringIterator2.next());
//		}
//		System.out.println();
	}
}
