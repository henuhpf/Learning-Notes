import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Random;

public class ClassLoaderMain {
    public static void main(String[] args) {
        try {
            int[] arr = new int[10];
            HashMap<Integer, Integer> map = new HashMap<>();
            Random random = new Random();
            for (int i = 0; i < arr.length; i++) {
                arr[i] = random.nextInt(20);
                map.put(arr[i], map.getOrDefault(arr[i], 0) + 1);
            }
            System.out.println(map);
            MyClassLoader myClassLoader = new MyClassLoader("F:" + File.separator + "workspace-java" + File.separator + "ideaCommunityWorkspace" + File.separator);
            Class<?> aClass = myClassLoader.loadClass("algorithm.HeapSort");
            Object obj = aClass.newInstance();

            Method buildMaxHeap = aClass.getDeclaredMethod("heapSort",  int[].class, int.class, int.class);
            buildMaxHeap.invoke(obj, arr, 0, arr.length - 1);

            for (int i : arr) {
                map.put(i, map.getOrDefault(i, 0) - 1);
                System.out.print(i + " ");
            }
            System.out.println();
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
