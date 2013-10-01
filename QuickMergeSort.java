package sort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import quickSort.QuickSort;
import tools.MyArrayUtil;
import tools.MyData;
import tools.MyInteger;

public class QuickMergeSort<E extends Comparable> extends QuickSort<E> {
	ExecutorService executor;
	int threadsNum;

	public QuickMergeSort(){
		//threadsNum = Runtime.getRuntime().availableProcessors();
		threadsNum = 8;
		executor = Executors.newCachedThreadPool();

	}

	public void p(String st,int i,String st2 ,int j){
		System.out.print("            "+st+" : "+i);
		System.out.println("      "+st2+" : "+j);
	}
	public void p(String st,int i){
		System.out.println("            "+st+" : "+i);
	}

	public void p(String st){
		System.out.println("            "+st+":");
	}

	public void sort(E[] array){
		this.array = array;
		int k = array.length / threadsNum,pos_1 = 0, pos = 0,pos2 = k - 1;
		int lastLengthOfRestSection = -1;  //-1というのは初期値として設定している
		int specialPos = 0;
		final List<Callable<Object>> workers = new ArrayList<Callable<Object>>(threadsNum);
		while(pos2 < array.length){
			workers.add(Executors.callable(new QuickSortWorker(pos,pos2)));
			pos = pos2 + 1;
			pos2 =  k + pos2;
		}

		//最後の区分だけ特別に処理する
		//p("pos",pos,"k",k);
		workers.add(Executors.callable(new QuickSortWorker(pos,array.length-1)));
		specialPos = pos;
		//ここからマージ処理を行う
		try {
			executor.invokeAll(workers);

			int i = 2;
			while(true){
				workers.clear();
				pos = 0; pos2 = k * i - 1 ;

				if(pos2 > array.length - 1){
					//p("sard if");
					executor.shutdown();
					//System.out.print("pos :"+0);
					//p("pos_1",pos_1,"pos2",(array.length - 1));
					//MyArrayUtil.print(array);
					merge(0,pos_1-1,array.length - 1);
					//p("after");
					//MyArrayUtil.print(array);
					break;
				}

				while(true){
					//System.out.println("************ "+(i*k)+"************");
					//System.out.println("pos : "+pos+"  pos2 : "+pos2);
					//MyArrayUtil.print(array);
					workers.add(Executors.callable(new MergeSortWorker(pos,pos2)));
					if(pos2 == array.length-1) {
						//p("first if");
						break;
					}
					pos = pos2 + 1;
					pos_1 = pos;
					//p("before pos2",pos2);
					//p("before pos2",pos2,"i",i);

					pos2 = pos2 + k*i;
					//p("after pos2",pos2);


//					p("(array.length - pos)",(array.length - pos),"k * (i / 2))",k * (i / 2));
					if(pos2 >= array.length){
						if((array.length - pos) == lastLengthOfRestSection)	{
							//p("second if");
							break;  //注 繰り返しソートの防止,確認済み
						}


						//p("pos_2",pos_2,"pos",pos);
						if(specialPos == 0){  //最初のあまり分のとき
						//if(i == 2){
							//p("first plex");
							workers.add(Executors.callable(new MergeSortWorker(pos,array.length - 1)));
							specialPos = pos;
						}else{
							//p("i > 2 plex");
						workers.add(Executors.callable(new MergeSortWorker(pos,specialPos-1,array.length - 1)));
						specialPos = pos;
						}
						lastLengthOfRestSection = array.length - pos;
						break;
					}
				}
				i = i * 2;
				executor.invokeAll(workers);


			}

		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
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

		public MergeSortWorker(int left,int mid,int right){
			this.left = left;
			this.right = right;
			this.mid = mid;
			buff = new LinkedList<E>();
		}
		public void run(){
			int i = left,j = mid + 1;

			while(i <= mid && j <= right) {
//				MyArrayUtil.print(array, i);
//				MyArrayUtil.print(array, j);

				if(array[i].compareTo(array[j]) < 0){
					buff.add(array[i]); i++;
				}else{
					buff.add(array[j]); j++;
				}
			}

			while(i <= mid) { buff.add(array[i]); i++;}
			while(j <= right) { buff.add(array[j]); j++;}
			for(i = left;i <= right; i++){array[i] = buff.remove(0);}

		}
	}

	public void merge(int left,int mid,int right){
		//buff.clear();  //buff用のリストを初期化しておく.修正
		LinkedList<E> buff = new LinkedList<E>();
		int i = left,j = mid + 1;

		while(i <= mid && j <= right) {
//			MyArrayUtil.print(array, i);
//			MyArrayUtil.print(array, j);
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
