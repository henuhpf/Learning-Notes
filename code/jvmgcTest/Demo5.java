public class Demo5 {
    // ���Ķ������survival���ռ䣬���������
    //java -Xms10M -Xmx10M -Xmn5M -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=10M -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log Demo1

    // jmap -dump:format=b,file=./dumpFile.hprof pid
    // jhat dumpFile.hprof
    // �����500KB����Ϊ������� ��  һЩ���п�
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(200000);
        byte[] array1 = new byte[1024 * 1024];
        array1 = new byte[1024 * 1024];
        array1 = new byte[1024 * 1024];
        array1 = null;
        byte[] array2 = new byte[2 * 1024 * 1024];
    }
}
