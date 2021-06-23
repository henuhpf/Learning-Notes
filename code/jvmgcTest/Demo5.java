public class Demo5 {
    // 存活的对象大于survival区空间，晋升老年代
    //java -Xms10M -Xmx10M -Xmn5M -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=10M -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log Demo1

    // jmap -dump:format=b,file=./dumpFile.hprof pid
    // jhat dumpFile.hprof
    // 多出的500KB对象为类加载器 和  一些运行库
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(200000);
        byte[] array1 = new byte[1024 * 1024];
        array1 = new byte[1024 * 1024];
        array1 = new byte[1024 * 1024];
        array1 = null;
        byte[] array2 = new byte[2 * 1024 * 1024];
    }
}
