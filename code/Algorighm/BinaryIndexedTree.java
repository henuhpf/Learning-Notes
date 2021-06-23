// 树状数组
public class BinaryIndexedTree {
    private int[] node;
    private int size;
    // 返回x二进制最低位1，其余全置零
    private int lowbit(int x) {
        return x & (-x);
    }
    // 将 原数组a[index] += d;
    public void add(int index,int d){
        while (index <= size){
            node[index] += d;
            index += lowbit(index);
        }
    }

    public int sum(int x){
        int ret = 0;
        while(x > 0){
            ret += node[x];
            x -= lowbit(x);
        }
        return ret;
    }

}
