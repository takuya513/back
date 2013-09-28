package quickSort;

import sort.Sort;
import tools.MyArrayUtil;
import tools.Sortable;
/*
 *
 */
public class QuickSort<E extends Comparable> implements Sort<E>{
	protected E[] array;
	public QuickSort(){
	}

	public  void sort(E[] array){
		this.array = array;
		quickSort(0,array.length - 1);
	}
	@SuppressWarnings("unchecked")
	public int partition(int left,int right){
		int i = left - 1, j = right;

		E pivot  = array[right];
		while(true){
			do{

				i++;
			}while(array[i].compareTo(pivot) < 0);
			do{
				j--;
				if(j < left) break;
			}while(pivot.compareTo(array[j]) < 0);
			if(i >= j) break;
			swap(i,j);
		}

		swap(i,right);
		return i;
	}

	public void swap(int i,int j){
		E temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	public void quickSort(int left,int right){
		int i;
		if(right > left) {
			i = partition(left,right);
			quickSort(left, i - 1);
			quickSort(i + 1 , right);
		}
	}


}

