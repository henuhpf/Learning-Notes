import java.util.Random;

/**
 * 希尔排序， 将插入排序的间隔范围扩大
 * 若数组为逆序排列，插入排序每次只能将逆序数-1，希尔每次交换后相对于插入排序逆序数会减少更多
 */
public class ShellSort {

    private static void shellSort(int[] arr) {
        int len = arr.length;
        int step = len / 2;
        step = step > 0 ? step : 1;
        while(step != 0){
            for(int i = step; i < len; i++){
                for(int j = i; j >= step; j -= step ){
                    if(arr[j - step] > arr[j]){
                        swap(arr, j - step, j);
                    }else {
                        break;
                    }
                }
            }
            step /= 2;
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
        shellSort(arr);
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
