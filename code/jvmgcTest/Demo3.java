public class Demo3 {
    // younggc 15 次 存活对象晋升老年代 （在第16次 gc时 年龄为 15的对象晋升）
    // 此次晋升的时候 额外的500多KB对象也晋升到了老年代
    //java -Xms30M -Xmx30M -Xmn15M -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15 -XX:PretenureSizeThreshold=10M -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc3.log Demo3
    public static void main(String[] args) {
        byte[] array1 = new byte[100 * 1024];
        byte[] array2 = new byte[9 * 1024 * 1024];
        array2 = null;
        for(int i = 0 ; i < 16; i++){
            array2 = new byte[9 * 1024 * 1024];
            array2 = null;
        }
       
    }
/**
Java HotSpot(TM) 64-Bit Server VM (25.291-b10) for windows-amd64 JRE (1.8.0_291-b10), built on Apr  9 2021 00:02:00 by "java_re" with MS VC++ 15.9 (VS2017)
Memory: 4k page, physical 12504344k(6579700k free), swap 19464100k(6267600k free)
CommandLine flags: -XX:InitialHeapSize=31457280 -XX:MaxHeapSize=31457280 -XX:MaxNewSize=15728640 -XX:MaxTenuringThreshold=15 -XX:NewSize=15728640 -XX:OldPLABSize=16 -XX:PretenureSizeThreshold=10485760 -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:SurvivorRatio=8 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:-UseLargePagesIndividualAllocation -XX:+UseParNewGC 
0.116: [GC (Allocation Failure) 0.116: [ParNew: 10541K->754K(13824K), 0.0010856 secs] 10541K->754K(29184K), 0.0012370 secs] [Times: user=0.06 sys=0.00, real=0.00 secs] 
0.118: [GC (Allocation Failure) 0.118: [ParNew: 9970K->926K(13824K), 0.0010341 secs] 9970K->926K(29184K), 0.0010979 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.120: [GC (Allocation Failure) 0.120: [ParNew: 10142K->1084K(13824K), 0.0007559 secs] 10142K->1084K(29184K), 0.0008070 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.122: [GC (Allocation Failure) 0.122: [ParNew: 10300K->1093K(13824K), 0.0008150 secs] 10300K->1093K(29184K), 0.0008919 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.123: [GC (Allocation Failure) 0.123: [ParNew: 10309K->1285K(13824K), 0.0007217 secs] 10309K->1285K(29184K), 0.0007709 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.125: [GC (Allocation Failure) 0.125: [ParNew: 10501K->995K(13824K), 0.0007448 secs] 10501K->995K(29184K), 0.0007884 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.126: [GC (Allocation Failure) 0.126: [ParNew: 10211K->1032K(13824K), 0.0007148 secs] 10211K->1032K(29184K), 0.0007667 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.128: [GC (Allocation Failure) 0.128: [ParNew: 10248K->1116K(13824K), 0.0007194 secs] 10248K->1116K(29184K), 0.0007738 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.129: [GC (Allocation Failure) 0.129: [ParNew: 10332K->994K(13824K), 0.0006988 secs] 10332K->994K(29184K), 0.0007476 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.131: [GC (Allocation Failure) 0.131: [ParNew: 10210K->1118K(13824K), 0.0008175 secs] 10210K->1118K(29184K), 0.0008738 secs] [Times: user=0.06 sys=0.00, real=0.00 secs] 
0.133: [GC (Allocation Failure) 0.133: [ParNew: 10334K->1236K(13824K), 0.0007119 secs] 10334K->1236K(29184K), 0.0008065 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.134: [GC (Allocation Failure) 0.134: [ParNew: 10452K->971K(13824K), 0.0008018 secs] 10452K->971K(29184K), 0.0008570 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.136: [GC (Allocation Failure) 0.136: [ParNew: 10187K->1058K(13824K), 0.0006638 secs] 10187K->1058K(29184K), 0.0007156 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.137: [GC (Allocation Failure) 0.137: [ParNew: 10274K->1102K(13824K), 0.0006709 secs] 10274K->1102K(29184K), 0.0007219 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.139: [GC (Allocation Failure) 0.139: [ParNew: 10318K->1244K(13824K), 0.0006818 secs] 10318K->1244K(29184K), 0.0007212 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.140: [GC (Allocation Failure) 0.140: [ParNew: 10460K->0K(13824K), 0.0028445 secs] 10460K->740K(29184K), 0.0028835 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
Heap
 par new generation   total 13824K, used 9339K [0x00000000fe200000, 0x00000000ff100000, 0x00000000ff100000)
  eden space 12288K,  76% used [0x00000000fe200000, 0x00000000feb1ed08, 0x00000000fee00000)
  from space 1536K,   0% used [0x00000000fee00000, 0x00000000fee00000, 0x00000000fef80000)
  to   space 1536K,   0% used [0x00000000fef80000, 0x00000000fef80000, 0x00000000ff100000)
 concurrent mark-sweep generation total 15360K, used 740K [0x00000000ff100000, 0x0000000100000000, 0x0000000100000000)
 Metaspace       used 2581K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 282K, capacity 386K, committed 512K, reserved 1048576K

 */
}
