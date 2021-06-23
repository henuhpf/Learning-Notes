// 主席树,求区间第 K 大
#include<bits/stdc++.h>
using namespace std;
const int N = 1e5 + 5;

struct Trie {
    int rt[N * 30], sum[N * 30], ls[N * 30], rs[N * 30], tot = 0;
    
    void init() {
        tot = 0;
    }

    void build(int &o, int l, int r) {
        o = ++tot;
        sum[o] = 0;
        if(l == r) return ;
        int mid = (l + r) >> 1;
        build(ls[o], l, mid);
        build(rs[o], mid + 1, r);
    }

    void update(int &o, int l, int r, int last, int v) {
        o = ++tot;
        sum[o] = sum[last] + 1;
        ls[o] = ls[last];
        rs[o] = rs[last];
        if(l == r) return ;
        int mid = (l + r) >> 1;
        if(v <= mid) update(ls[o], l, mid, ls[last], v);
        else update(rs[o], mid + 1, r, rs[last], v);
    }
    
    int query(int L, int R, int l, int r, int k) {
        if(l == r) return l;
        int num = sum[ls[R]] - sum[ls[L]];
        int mid = (l + r) >> 1;
        if(k <= num) return query(ls[L], ls[R], l, mid, k);
        else return query(rs[L], rs[R], mid + 1, r, k - num);
    }
}tree;

int a[N], b[N];

int main()
{   
    
    int T;
    scanf("%d", &T);
    while(T--) {
        int n, m;
        scanf("%d %d", &n, &m);
        tree.init();
        for(int i = 1; i <= n; i++) scanf("%d", &a[i]), b[i] = a[i];
        sort(b + 1, b + n + 1);
        int cnt = unique(b + 1, b + n + 1) - b - 1;
        tree.build(tree.rt[0], 1, n);
        for(int i = 1; i <= n; i++) {
            a[i] = lower_bound(b + 1, b + cnt + 1, a[i]) - b;
            tree.update(tree.rt[i], 1, n, tree.rt[i-1], a[i]);
        }
        while(m--) {
            int l, r, k;
            scanf("%d %d %d", &l, &r, &k);
            printf("%d\n", b[tree.query(tree.rt[l-1], tree.rt[r], 1, n, k)]);
        }

    }
    return 0;
}