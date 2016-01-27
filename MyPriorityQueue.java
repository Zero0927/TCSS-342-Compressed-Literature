//Siyuan Zhou
import java.util.Arrays;


public class MyPriorityQueue <T extends Comparable<T>>{

	private Object[] heap;//array to contain elements
	private int maxSize;//the max size of the array heap
	private int elementSize;//actual number of elements in the heap
	public MyPriorityQueue(int maxSize) {
		heap=new Object[maxSize];
		Arrays.fill(heap, null);
		this.maxSize=maxSize;
		this.elementSize=0;
	}
	public int getSize(){
		return elementSize;
	}
	
	public boolean insert(T element){
		if(this.elementSize==maxSize)
			return false;
		heap[this.elementSize]=element;
		for(int k=this.elementSize;k>0;){
			int parent=parent(k);
			@SuppressWarnings("unchecked")
			T cur=(T)heap[k],pr=(T)heap[parent];
			//if current node is smaller than parent, then exchange them
			if(cur.compareTo(pr)<0){
				exchange(k, parent);
				k=parent;
			}else{
				break;
			}
		}
		++this.elementSize;
		return true;
	}
	public T popMin(){
		if(this.elementSize==0)
			return null;
		@SuppressWarnings("unchecked")
		T pop=(T)heap[0];//head one is the smallest
		heap[0]=heap[--this.elementSize];//take the last one to the head
		if(elementSize==0)
			return pop;
		for(int k=0;rightChild(k)<this.elementSize;){
			int next;
			if(isSmaller(k,leftChild(k))&&isSmaller(k,rightChild(k)))
				break;
			next=isSmaller(leftChild(k),rightChild(k))?leftChild(k):rightChild(k);
			exchange(k, next);
			k=next;
		}
		//the last element's parent may only has one child
		if(elementSize%2==0&&isSmaller(elementSize-1, parent(elementSize-1))){
			exchange(elementSize-1, parent(elementSize-1));
		}
		return pop;
	}
	//exchange two elements.
	private void exchange(int aIndex,int bIndex){
		Object tmp=heap[aIndex];
		heap[aIndex]=heap[bIndex];
		heap[bIndex]=tmp;
	}
	//compare the two elements.Their index are aIndex and bIndex in the heap
	@SuppressWarnings("unchecked")
	private boolean isSmaller(int aIndex,int bIndex){
		return ((T)heap[aIndex]).compareTo((T)heap[bIndex])<=0;
	}
	//return the index of an element's parent
	private static int parent(int index){
		return index==0?index:(index-1)/2;
	}
	//return the index of left child of an element in the heap
	private static int leftChild(int parent){
		return 2*parent+1;
	}
	//return the index of right child of an element in the heap
	private static int rightChild(int parent){
		return 2*parent+2;
	}

}
