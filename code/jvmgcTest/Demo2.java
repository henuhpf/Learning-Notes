public class Demo2 {
    //java -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15 -XX:PretenureSizeThreshold=10M -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log Demo2
    public static void main(String[] args) {
        byte[] array1 = new byte[2 * 1024 * 1024];
        array1 = new byte[2 * 1024 * 1024];
        array1 = new byte[2 * 1024 * 1024];
        array1 = null;
        byte[] array2 = new byte[128 * 1024];
        byte[] array3 = new byte[2 * 1024 * 1024];
        array3 = new byte[2 * 1024 * 1024];
        array3 = new byte[2 * 1024 * 1024];
        array3 = new byte[128 * 1024];
        array3 = null;
        byte[] array4 = new byte[2 * 1024 * 1024];
    }
    /**
Java HotSpot(TM) 64-Bit Server VM (25.291-b10) for windows-amd64 JRE (1.8.0_291-b10), built on Apr  9 2021 00:02:00 by "java_re" with MS VC++ 15.9 (VS2017)
Memory: 4k page, physical 12504344k(6587336k free), swap 19464100k(6265444k free)
CommandLine flags: -XX:InitialHeapSize=20971520 -XX:MaxHeapSize=20971520 -XX:MaxNewSize=10485760 -XX:MaxTenuringThreshold=15 -XX:NewSize=10485760 -XX:OldPLABSize=16 -XX:PretenureSizeThreshold=10485760 -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:SurvivorRatio=8 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:-UseLargePagesIndividualAllocation -XX:+UseParNewGC 
0.137: [GC (Allocation Failure) 0.137: [ParNew: 7420K->786K(9216K), 0.0010311 secs] 7420K->786K(19456K), 0.0011899 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.139: [GC (Allocation Failure) 0.139: [ParNew: 7091K->0K(9216K), 0.0028847 secs] 7091K->768K(19456K), 0.0029460 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
Heap
 par new generation   total 9216K, used 2212K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
  eden space 8192K,  27% used [0x00000000fec00000, 0x00000000fee290e0, 0x00000000ff400000)
  from space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400000, 0x00000000ff500000)
  to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
 concurrent mark-sweep generation total 10240K, used 768K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
 Metaspace       used 2581K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 282K, capacity 386K, committed 512K, reserved 1048576K

     */
}
