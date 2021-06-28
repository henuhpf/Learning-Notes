import java.util.Random;

public class SelectSort {

    private static void selectSort(int[] arr) {
        int l = 0, r = arr.length - 1;
        while(l < r){
            int minValue = Integer.MAX_VALUE;
            int minIndex = 0;
            for(int i = l; i <= r; i++){
                minValue = Math.min(minValue, arr[i]);
                minIndex = minValue == arr[i] ? i : minIndex;
            }
            swap(arr, minIndex, l);
            l++;
        }
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
        selectSort(arr);
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

}
