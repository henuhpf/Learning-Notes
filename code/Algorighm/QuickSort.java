import java.util.Arrays;
import java.util.Random;

public class QuickSort {

    private static void quickSort(int[] arr, int l, int r){
        if(l > r){
            return;
        }
        Random random = new Random();
        int m = l + random.nextInt(r - l + 1);
        int i = l, j = r;
        swap(arr, r, m);
        m = r;
        int middle = arr[m];
        while(i < j){
            while(arr[i] <= middle && i < j){
                i++;
            }
            while(arr[j] >= middle && i < j){
                j--;
            }
            if(i < j ){
                swap(arr, i, j);
            }
        }
        swap(arr, j, m);
        quickSort(arr, l, j - 1);
        quickSort(arr, j + 1, r);
    }
    public static void swap(int[] arr, int i, int j){
        int tmp = arr[j];
        arr[j] = arr[i];
        arr[i] = tmp;
    }
    public static void main(String[] args) {
        int[] arr = new int[10];
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(20);
        }
        quickSort(arr, 0, arr.length - 1);
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
