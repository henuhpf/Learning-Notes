import java.util.HashMap;
import java.util.Random;

public class MergeSort {

    private static void mergeSort(int[] arr, int l, int r) {
        if(l >= r){
            return;
        }
        int mid = ((r - l) >> 1) + l;
        mergeSort(arr, l, mid);
        mergeSort(arr, mid + 1, r);
        merge(arr, l, mid, r);
    }
    private static void merge(int[] arr, int l, int m, int r){
        int[] tmp = new int[r - l + 1];
        int l1 = l, l2 = m + 1, i = 0;
        for(i = 0; i < r-l+1 && l1 <= m && l2 <= r; i++){
            if(arr[l1] <= arr[l2]){
                tmp[i] = arr[l1++];
            }else {
                tmp[i] = arr[l2++];
            }
        }
        while(l1 <= m){
            tmp[i++] = arr[l1++];
        }
        while(l2 <= r){
            tmp[i++] = arr[l2++];
        }
        for(int j = 0; j < i; j++){
            arr[l++] = tmp[j];
        }
    }
    public static void swap(int[] arr, int i, int j){
        int tmp = arr[j];
        arr[j] = arr[i];
        arr[i] = tmp;
    }
    public static void main(String[] args) {
        int[] arr = new int[10];
        HashMap<Integer, Integer> map = new HashMap<>();
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(20);
            map.put(arr[i], map.getOrDefault(arr[i], 0) + 1);
        }
        System.out.println(map);
        mergeSort(arr, 0, arr.length - 1);
        for (int i : arr) {
            map.put(i, map.getOrDefault(i, 0) - 1);
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.println(map);
    }
}
