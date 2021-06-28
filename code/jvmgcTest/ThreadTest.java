import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThreadTest {
    /**
     * 多个线程， 出现OOM异常时，只退出出现异常的线程, 主线程出现异常，也不会退出其他已经启动的线程
     * java -Xms10M -Xmx10M gctest.ThreadTest
     * @param args
     */
    public static void main(String[] args) {
        new Thread(()->{
            List<byte[]> list = new ArrayList<>();
            while(true){
                System.out.println(new Date().toString() + Thread.currentThread() + "==");
                byte[] b =  new byte[1024 * 1024];
                list.add(b);
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(()->{
            List<byte[]> list = new ArrayList<>();
            while(true){
                System.out.println(new Date().toString() + Thread.currentThread() + "==");
                byte[] b =  new byte[1024 * 1024];
                list.add(b);
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        List<byte[]> list = new ArrayList<>();
        while(true){
            System.out.println(new Date().toString() + Thread.currentThread() + "==");
            byte[] b =  new byte[1024 * 1024];
            list.add(b);
        }
    }
}
