myStudy
=======
package sort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import quickSort.QuickSort;
import tools.MyArrayUtil;

public class QuickMergeSort<E extends Comparable> extends QuickSort<E> {
	ExecutorService executor;
	int threadsNum;

	public QuickMergeSort(){
		threadsNum = Runtime.getRuntime().availableProcessors();
		executor = Executors.newCachedThreadPool();

	}

	public void sort(E[] array){
		this.array = array;
		int pos = 0;
		int pos2 = array.length / threadsNum;
		final List<Callable<Object>> workers = new ArrayList<Callable<Object>>(threadsNum);
		for(int i = 2;i <= threadsNum;i++){
			workers.add(Executors.callable(new QuickSortWorker(pos,pos2)));
			pos = pos2 + 1;
			pos2 =  i * pos2;
		}

		//最後の区分だけ特別に処理する
		//System.out.println("left : "+tmpNoName2+"  right  :"+(array.length));
		workers.add(Executors.callable(new QuickSortWorker(pos,array.length-1)));

		try {
			executor.invokeAll(workers);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		//executor.shutdown();

		//MyArrayUtil.print(array);
		//System.out.println("mid : "+tmpNoName2+"  right : "+(array.length - 1));
		//merge(0,pos-1,array.length-1);

		int i = 2;
		pos = 0;  pos2 = (array.length / threadsNum) * i;
		while(true){
			while(true){
				workers.add(Executors.callable(new MergeSortWorker(pos,pos2)));
				if(pos2 == array.length) break;
				pos = pos2 + 1;
				pos2 = pos2 + i;

				if(pos2 > array.length){
					pos2 = pos2 - i;
					workers.add(Executors.callable(new MergeSortWorker(pos,pos2)));
					break;
				}
			}
			i = i * 2;
			if(i > threadsNum) break;
		}

		try {
			executor.invokeAll(workers);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		executor.shutdown();
	}

	class MergeSortWorker implements Runnable{
		int left,right,mid;
		LinkedList<E> buff;
		public MergeSortWorker(int left,int right){
			this.left = left;
			this.right = right;
			mid = (left + right) / 2;
			buff = new LinkedList<E>();
		}
		public void run(){
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
	}

	class QuickSortWorker implements Runnable {
		int left,right;
		public QuickSortWorker(int left,int right){
			this.left = left;
			this.right = right;
		}

		public void run() {
			quickSort(left,right);
		}
	}

	public synchronized void quickSort(int left,int right){
		super.quickSort(left, right);
	}


}
