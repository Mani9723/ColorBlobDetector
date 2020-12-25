package servlet;

import java.util.ArrayList;

/**
 * Disjoint sets class that represents a collection of sets.
 * It uses {@link Set} class as a foundation to represent sets
 * that are unique with no overlapping values.
 *
 * @param <T> Generic Type
 */
public class DisjointSets<T>
{
	/**
	 * Holds the roots based on union by size
	 */
	private int[] s;
	/**
	 * Holds the acutal data of the sets
	 */
	private ArrayList<Set<T>> sets;

	/**
	 * Creates a disjoint set.
	 * The roots are all by themselves and the data are
	 * all individual sets.
	 * @param data  Sets
	 */
	public DisjointSets(ArrayList<T> data)
	{
		Set<T> tempSet;
		sets = new ArrayList<>(data.size());
		s = new int[data.size()];
		for(int i = 0; i < s.length; i++){
			s[i] = -1;
			tempSet = new Set<>();
			tempSet.add(data.get(i));
			sets.add(tempSet);
		}
	}

	/**
	 * Unions two sets together.
	 * The two values provided must be roots.
	 *
	 * If two non-roots are provided then,
	 * IllegalArgumentsException is thrown.
	 *
	 * @param root1 Root
	 * @param root2 Root
	 * @return  New Root of the unioned set
	 */
	public int union(int root1, int root2)
	{
		int newRoot;

		validateRoots(root1,root2);
		if( s[root2] < s[root1] ) {             // root2 is deeper
			s[root2] += s[root1];
			newRoot = s[root1] = root2;        // Make root2 new root
			addAll(root2,root1);
		}else if(root1==root2){
			newRoot = root1;
		} else {
			s[root1]--;
			newRoot = s[root2] = root1;        // Make root1 new root
			addAll(root1,root2);
		}
		return newRoot;
	}

	/**
	 * Finds the root of a particular set.
	 * Compresses the path between a set and its root.
	 *
	 * @param x Root Index
	 * @return  Root
	 */
	public int find(int x)
	{
		if(x >= s.length)
			throw new IllegalArgumentException("Out of bounds");
		if(s[x] < 0 )
			return x;

		while(s[x] >= 0)    // Compress path
			x = s[x];
		return x;
	}

	/**
	 * Returns the set at a given root
	 *
	 * @param root Root set
	 * @return  {@code Set<T>}
	 */
	public Set<T> get(int root)
	{
		return sets.get(root);
	}

	/**
	 * Validates that the two roots are in fact roots and not non-roots
	 * @param root1 First Root
	 * @param root2 Second Root
	 */
	private void validateRoots(int root1, int root2)
	{
		if(s[root1] >= 0 || s[root2] >= 0)
			throw new IllegalArgumentException("Must be roots");
	}

	/**
	 * Adds the sets of the two roots together to form one combined set
	 * of unique values.
	 * @param root1 Root of set 1
	 * @param root2 Root of set 2
	 */
	private void addAll(int root1, int root2)
	{
		sets.get(root1).addAll(sets.get(root2));
		sets.get(root2).clear();
	}


	//main method
	public static void main(String[] args) {
//		ArrayList<Integer> arr = new ArrayList<>();
//		for(int i = 0; i < 25; i++)
//			arr.add(i);
//
//		DisjointSets<Integer> ds = new DisjointSets<>(arr);
//		try {
//			ds.union(ds.find(0), ds.find(0));
//			ds.union(ds.find(1), ds.find(0));
//			ds.union(ds.find(2), ds.find(1));
//			ds.union(ds.find(3), ds.find(2));
//			ds.union(ds.find(4), ds.find(3));
//			ds.union(ds.find(5), ds.find(0));
//			ds.union(ds.find(6), ds.find(6));
//			ds.union(ds.find(7), ds.find(6));
//			ds.union(ds.find(8), ds.find(7));
//			ds.union(ds.find(9), ds.find(8));
//			ds.union(ds.find(10), ds.find(10));
//			int root1, root2;
//			java.util.Scanner key = new java.util.Scanner(System.in);
//			for(int i = 0; i < 30;i++){
//				root1 = key.nextInt();
//				root2 = key.nextInt();
//				System.out.println();
//				ds.union(ds.find(root1),ds.find(root2));
//			}
//
//		}catch (IllegalArgumentException e){
//			System.out.println("sdf");
//		}
//		System.out.println(ds.get(1));
//		System.out.println(ds.get(7));
//		System.out.println(ds.get(17));
//		for(int i = 0; i < ds.s.length; i++)
//			System.out.print(ds.s[i] + " ");
//		System.out.println();
//

//		System.out.println(ds.find(0)); //should be 0
//		System.out.println(ds.find(1)); //should be 1
//		System.out.println(ds.union(0, 1)); //should be 0
//		System.out.println(ds.find(0)); //should be 0
//		System.out.println(ds.find(1)); //should be 0
//		System.out.println("-----");
//		System.out.println(ds.find(0)); //should be 0
//		System.out.println(ds.find(2)); //should be 2
//		System.out.println(ds.union(0, 2)); //should be 0
//		System.out.println(ds.find(0)); //should be 0
//		System.out.println(ds.find(2)); //should be 0
//		System.out.println("-----");
//		//Note: AbstractCollection provides toString() method using the iterator
//		//see: https://docs.oracle.com/javase/8/docs/api/java/util/AbstractCollection.html#toString--
//		//so your iterator in Set needs to work for this to print out correctly
//		System.out.println(ds.get(0)); //should be [0, 1, 2]
//		System.out.println(ds.get(1)); //should be []
//		System.out.println(ds.get(3)); //should be [3]
	}
}
