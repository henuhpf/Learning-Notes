
// 二分搜索树
public class BST<E extends Comparable<E>> {
    private class Node{
        public E e;
        public Node left,right;
        public Node(E e){
            this.e = e;
            left = null;
            right = null;
        }
    }
    private Node root;
    private int size;
    public BST(){
        root = null;
        size =0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void add(E e){
        root = add(root,e);
    }
    // 向以 node 为根的二分搜索树中插入元素 E
    private Node add(Node node,E e){
        if(node == null){
            node = new Node(e);
            size ++;
            return node;
        }
        if(e.compareTo(node.e) < 0){
            node.left = add(node.left,e);
        }else {
            node.right = add(node.right,e);
        }
        return node;
    }

    public boolean contains(E e) {
        return contains(root,e);
    }
    private boolean contains(Node node,E e){
        if(node == null){
            return false;
        }
        if(e.compareTo(node.e) == 0){
            return true;
        }else if(e.compareTo(node.e) < 0) {
            return contains(node.left,e);
        }else {
            return contains(node.right,e);
        }
    }

    public void preOrder(){
        preOrder(root);
    }
    private void preOrder(Node node){
        if(node == null){
            return;
        }
        System.out.println(node.e);
        preOrder(node.left);
        preOrder(node.right);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        generateBSTString(root,0,res);
        return res.toString();
    }
    private void generateBSTString(Node node,int depth,StringBuilder res){
        if(node == null) {
            res.append(generateDepthString(depth) + "null\n");
            return;
        }
        res.append(generateDepthString(depth) + node.e + "\n");
        generateBSTString(node.left, depth + 1,res);
        generateBSTString(node.right, depth + 1,res);
    }
    private String generateDepthString(int depth) {
        StringBuilder res = new StringBuilder();
        for(int i = 0;i < depth; i++) {
            res.append("--");
        }
        return res.toString();
    }

    public E minimux(){
        if(size == 0){
            throw new IllegalArgumentException("BST is empty!");
        }
        return minimux(root).e;
    }
    private Node minimux(Node node) {
        if(node.left == null) {
            return node;
        }
        return minimux(node.left);
    }

    public E removeMin(){
        E ret = minimux();
        removeMin(root);
        return ret;
    }
    private Node removeMin(Node node){
        if(node.left == null){
            Node rightNode = node.right;
            node.right = null;
            size --;
            return rightNode;
        }
        Node ret = removeMin(node.left);
        return ret;
    }

    public void remove(E e){
        root = remove(root,e);
    }
    private Node remove(Node node,E e){
        if(node == null){
            return null;
        }
        if(e.compareTo(node.e) < 0){
            node.left = remove(node.left,e);
            return node;
        }else if(e.compareTo(node.e) > 0){
            node.right = remove(node.right,e);
            return node;
        }else {
            if(node.left == null){
                Node rightNode = node.right;
                node.right = null;
                size --;
                return rightNode;
            }
            if(node.right == null){
                Node leftNode = node.left;
                node.left = null;
                size --;
                return leftNode;
            }
            // 待删除节点左右子树不为空
            Node successor = minimux(node.right);
            successor.right = removeMin(node.right);
            successor.left = node.left;
            node.left = node.right = null;
            return successor;
        }
    }


}
