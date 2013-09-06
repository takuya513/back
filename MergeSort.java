package mergeSort;

import java.util.ArrayList;

import sort.Sort;

public class MergeSort<E extends Comparable> implements Sort{
	E[] array;
	ArrayList<E> buff;
	public MergeSort(E[] array){
		this.array = array;
		buff = new ArrayList<E>();
	}

	public void sort(int left,int right){
		int mid = (left + right) / 2;

		if(right <= left)
			return;

		sort(left,mid);
		sort(mid + 1, right);

		merge(left,mid,right);
	}

	public void merge(int left,int mid,int right){
		//buff.clear();  //buff—p‚ÌƒŠƒXƒg‚ð‰Šú‰»‚µ‚Ä‚¨‚­.C³
		int i = left,j = mid + 1,k= 0;

		while(i <= mid && j <= right) {
			if(array[i].compareTo(array[j]) < 0){
				buff.add(array[i]); i++;
			}else{
				buff.add(array[j]); j++;
			}
		}

		while(i <= mid) { buff.add(array[i]); i++;}
		while(j <= right) { buff.add(array[j]); j++;}
		for(i = left;i <= right; i++){ array[i] = buff.remove(0);}
	}

//	public static void main(String arg[]){
//		ArrayList<Integer> a = new ArrayList<Integer>();
//		for(int i = 0;i < 5;i++){
//			a.add(i);
//		}
//
//		for(int i = 0;i < 5;i++){
//			System.out.println(a.remove(0));
//		}
//	}

}
