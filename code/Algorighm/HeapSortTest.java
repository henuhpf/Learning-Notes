import java.util.HashMap;
import java.util.Random;

public class HeapSortTest {
    public  void buildMaxHeap(int[] arr){
        int len = arr.length;
        int mid = len/2 - 1;
        while(mid >= 0){
            down(arr, mid, len);
            mid--;
        }
    }
    private  void up(int[] arr, int index){
        int parent = (index - 1) / 2;
        while(arr[index] > arr[(index - 1) / 2]){
            swap(arr, parent, index);
            index = (index - 1) / 2;
        }
    }

    private  void down(int[] arr, int index, int len){
        while(2 * index + 1 < len){
            int i = index * 2 + 1;
            if(i + 1 < len && arr[i + 1] > arr[i]){
                i ++;
            }
            if(arr[index] >= arr[i]){
                break;
            }
            swap(arr, i, index);
            index = i;
        }
    }
    private  void heapSort(int[] arr, int l, int r) {
        if(arr == null || arr.length == 1){
            return;
        }
        buildMaxHeap(arr);
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
        int len = arr.length;
        while(len > 1){
            swap(arr, 0, --len);
            down(arr, 0, len);
        }
    }
    private  void swap(int[] arr, int i, int j){
        int tmp = arr[j];
        arr[j] = arr[i];
        arr[i] = tmp;
    }
    public static void main(String[] args) {
        HeapSortTest heapSort = new HeapSortTest();
        int[] arr = new int[10];
        HashMap<Integer, Integer> map = new HashMap<>();
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(20);
            map.put(arr[i], map.getOrDefault(arr[i], 0) + 1);
        }
        System.out.println(map);
        heapSort.heapSort(arr, 0, arr.length - 1);
        for (int i : arr) {
            map.put(i, map.getOrDefault(i, 0) - 1);
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.println(map);
    }
}
