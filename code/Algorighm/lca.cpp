// 求树中两个节点的公共祖先
#include<bits/stdc++.h>
using namespace std;
#define ll long long
const int mod = 1e9 + 7;
const int N = 1e5 + 5;

int dp[N][25], deep[N], n;

vector<int> q[N];

void dfs(int u, int fa, int dep) {
    deep[u] = dep;
    dp[u][0] = fa;

    for(auto v : q[u]) {
        if(v == fa) continue;
        dfs(v, u, dep + 1);
    }
}


void ST() {
    for(int j = 1; j <= 20; j++) {
        for(int i = 1; i <= n; i++) {
            dp[i][j] = dp[dp[i][j-1]][j-1];
        }
    }
}

int LCA(int x, int y) {
    if(deep[x] < deep[y]) swap(x, y);
    int dis = deep[x] - deep[y];
    for(int i = 0; i <= 20; i++) {
        if(dis & (1 << i)) x = dp[x][i];
    }

    if(x == y) return x;

    for(int i = 20; i >= 0; i--) {
        if(dp[x][i] != dp[y][i]) {
            x = dp[x][i];
            y = dp[y][i];
        }
    }
    return dp[x][0];
}

int main()
{
//    #ifndef ONLINE_JUDGE
//    freopen("c.txt", "r", stdin);
//    #endif // ONLINE_JUDGE
    int  u, v;
    scanf("%d", &n);
    for(int i = 1; i < n; i++) {
        scanf("%d %d", &u, &v);
        q[u].push_back(v);
        q[v].push_back(u);
    }
    dfs(1, 0, 1);
    ST();
    while(1) {
        scanf("%d %d", &u, &v);
        printf("%d %d Lca : %d\n", u, v, LCA(u, v));
    }
    return 0;
}


