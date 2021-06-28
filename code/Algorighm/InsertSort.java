import java.util.Random;

public class InsertSort {

    private static void insertSort(int[] arr) {
        int len = arr.length;
        for(int i = 1; i < len; i++){
            for(int j = i; j > 0; j-- ){
                if(arr[j - 1] > arr[j]){
                    swap(arr, j - 1, j);
                }else {
                    break;
                }
            }
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
        insertSort(arr);
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
