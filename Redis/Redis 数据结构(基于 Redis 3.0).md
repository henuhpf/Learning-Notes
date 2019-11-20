### Redis 数据结构(基于 Redis 3.0)

----

##### 简单动态字符串 (Simple Dynamic String) 

---

- 数据结构 ( sds.h/sdshdr )

```c
struct sdshdr {
    // 记录 buf 数组中已使用的字节数量，等于SDS 保存字符串的长度
    int len;
    // 记录buf 数组中未使用字节的数量
    int free;
    // 字节数组，用于保存字符串
    char buf[];
}
```

- 与 C 中字符串的区别
  - 记录字符串的长度，可以***常数复杂度获取字符串长度***
  - ***杜绝缓冲区溢出***，C中要自己为字符串分配空间，如果拼接字符串之后的大小大于分配空间，则会修改超出字符串地址空间的值；SDS 对字符串修改时，会自动将 SDS空间扩展至所需的大小。
  - ***减少修改字符串时带来的内存重分配次数***
    - SDS实现了**空间预分配和惰性空间释放**两种优化策略
    - **空间预分配 (优化 SDS 的字符串增长操作)：对 SDS 做修改时，程序不仅会位 SDS 分配修改所必须要的空间，还会为 SDS 分配额外的未使用空间。**如果对SDS修改后， len < 1MB,会分配和len属性相同大小的未使用空间，如 len 修改成13字节，则free=13,buf数组总长度 = 13 + 13 + 1 = 27，额外的一字节用于存空字符；len > 1MB,则会分配1MB的未使用空间，如len=30M，buf数组长度为30MB+1MB+1byte。通过这种预分配策略，SDS 将连续增长 N 次字符串所需内存重分配次数从必定 N 次降低为最多 N 次。
    - **惰性空间释放(优化 SDS 的字符串缩短操作) ：当缩短 SDS保存的字符串时，程序并不立即使用内存重分配来回收缩短后多出来的字节，而是使用free属性将这些字节的数量记录起来，等待将来使用。**同时， SDS 也提供了相应的 API，可以让我们真正地释放 SDS 的未使用空间，不用担心惰性空间释放会造成内存浪费。
  - ***二进制安全*** : C字符串不能包含空字符，Redis中以处理二进制的方式处理 SDS 存放在 buf 数组中的数据，使用 len 判断字符串是否结束，使Redis 可以保存任意格式的二进制数据。
  - ***兼容部分 C 字符串函数***：SDS遵循 C 字符串以空字符结尾的惯例，总会在为 buf 数组分配空间时多分配一个字节来容纳空字符，使得保存文本数据的 SDS 可以重用一部分 <string.h> 库定义的函数。



##### 链表

---

- 链表在 Redis 中的应用非常广泛
  - 列表键的底层实现之一就是链表。当一个列表键包含了数量比较多的元素，又或者列表中包含的元素都是比较长的字符串时， Redis 就会使用链表作为链表键的底层实现。
  - 发布与订阅、慢查询、监视器等功能也用到了链表。
  - Redis 服务器还使用链表来保存多个客户端的状态信息，以及用链表来构件客户端输出缓冲区(output buffer)。
- 数据结构 ( adlist.h/listNode 、adlist.h/list)

```c
typedef struct listNode{
    // 前置节点
    struct listNode *prev;
    // 后置节点
    struct listNode *next;
	//节点的值
    void *value;
}listNode;
typedef struct list{
    // 表头节点
    listNode *head;
    // 表尾节点
    listNode *tail;
    // 链表所包含的节点数量
    unsigned long len;
    // 节点值复制函数,dup函数用于复制链表节点所保存的值
    void *(*dup) (void *ptr);
    // 节点值释放函数, free 函数用于释放链表节点所保存的值
    void (*free) (void *ptr);
    // 节点值对比函数, match 函数用于对比链表节点锁保存的值和另一个输入值是否相等
    int (*match) (void *ptr, void *key);
}list;
```

- ***Redis 链表特性***
  - 双端链表 ：每个节点都带有 prev 和 next 指针。
  - 无环 ：表头节点的 prev 指针和表尾节点的 next 指针都指向 NULL， 对链表的访问以 NULL 为终点。
  - 带表头节点和表尾节点 ： 通过 list 结构的 head 指针和 tail 指针，获取表头和表尾节点的复杂度为 O(1)。
  - 带链表长度计数器 ：程序获取链表节点数量只需访问 len属性，复杂度为 O(1)。
  - 多态 ：链表节点使用 void* 指针来保存节点值，并且可以通过 list 结构的 dup、free、match 三个属性为节点值设置类型特定函数，所以链表可以用于保存各种不同类型的值。



##### 字典

---

- 又称 符号表( symbol table )、关联数组 ( associative array ) 或 映射 (map)，是一种保存键值对的抽象数据结构。
- 字典在 Redis 中的应用
  - Redis 的数据库就是使用字典作为底层实现的，对数据库的增删改查操作也是构建在字典的操作之上的。
  - 字典是哈希键的底层实现之一，当一个哈希键包含的键值对比较多，又或者键值对中的元素都是比较长的字符串时，Redis 就会使用字典作为哈希键的底层实现。
- Redis的字典使用 ***哈希表*** 作为底层实现，一个哈希表里面可以有多个哈希表节点，每个哈希表节点就保存了字典中的一个键值对。
- 哈希表数据结构 ( dict.h/dictht 、dict.h/dictEntry )

```c
typedef struct dictht {
    // 哈希表数组
    dictEntry **table;
    // 哈希表大小
    unsigned long size;
    // 哈希表大小掩码，用于计算索引值，总是等于 size - 1
    undigned long sizemask;
    // 哈希表已有的节点的数量
    unsigned long used;
} dictht;
// 哈希表节点
typedef struct dictEntry {
    // 键
    void *key;
    // 值，值可以是一个指针或者是一个 uint64_t 整数，或者是 int64_t 整数
    union{
        void *val;
        uint64_t u64;
        int64_t s64;
    } v;
    // 指向下一个哈希表节点，形成链表。这个指针可以将多个哈希值相同的键值对连接在一起，解决键冲突问题
    struct dictEntry *next;
} dictEntry;
```

- 字典数据结构 ( dict.h/dict )

```c
typedef struct dict {
    // type 属性和 privdata 属性是针对不同类型的键值对，为创建多态字典而设置
    // 类型特定函数，一个指向 dictType 结构的指针
    // 每个 dictType 结构保存了一簇用于操作特定类型键值对的函数，Redis会为用途不同的字典设置不同的类型特定参数
    dictType *type;
    // 私有数据，保存了需要传给那些特定函数的可选参数
    void *privdata;
   
    // 哈希表
    dictht ht[2];
    // rehash 索引, 当 rehash 不在进行时，值为 -1
    int trehashidx;
} dict;
// 保存了一簇用于操作特定类型键值对的函数
typedef struct dictType {
    // 计算哈希值的函数
    unsigned int (*hashFunction) (const void *key);
    // 复制键的函数
    void *(*keyDup) (void *privdata, const void *key);
    // 复制值的函数
    void *(*valDup) (void *privdata, const void *obj);
    // 对比键的函数
    int (*keyCompare) (void *privdata, const void *key1, const void *key2);
    // 销毁键的函数
    void (*keyDestructor) (void *privdata, void *key);
    // 销毁值的函数
    void (*valDestructor) (void *privdata, void *obj);
}dictType;
```
- ht 是一个包含了两个哈希表的数组，一般情况下，字典只使用 ht[0] 哈希表，ht[1] 哈希表只会在对 ht[0] 进行rehash时使用

- 哈希算法
  - 1、使用字典设置的哈希函数，计算键 key 的哈希值
  
    ```c
    hash = dict->type->hashFunction(key);
    ```
  
  - 2、使用哈希表的 sizemask 属性和哈希值，计算出索引值
  
    ```c
    index = hash & dict->ht[x].sizemask;// 根据情况不同,x可以是0或者1
    ```
  
    
  
- 链冲突采用头插法解决

- ***rehash ：为了让哈希表的负载因子维持在一个合理的范围之内，当哈希表保存的键值对太多或者太少时，程序需要对哈希表的大小进行相应的扩展或者收缩；扩展和收缩的工作可以通过执行 rehash(重新散列) 操作来完成。***

- 负载因子 = 哈希表已保存节点数量 / 哈希表大小 

  ```load_facotr = ht[0].used / ht[0].size;```

- 渐进式 rehash : rehash 不是一次性、集中式地完成的，而是分多次、渐进式地完成；如果哈希表中保存了上千万，甚至上亿个键值对，一次性 rehash 的话，可能会导致服务器在一段时间内停止服务。

  - 为字典的 ht[1] 哈希表分配空间，这个空间大小取决于要执行的操作，以及 ht[0] 当前包含的键值对数量( ht[0].used 的值)
    - 如果执行的是扩展操作，ht[1] 的大小为**第一个大于等于 ht[0].used * 2 的 2^n**
    - 如果执行的是收缩操作，ht[1] 的大小为**第一个大于等于 ht[0].used 的 2^n**
  - 将保存在 ht[0] 中的所有键值对 rehash 到 ht[1] 上面
    - 在字典中维持一个***索引计数器变量 rehashidx***，并将它的值设为0，表示 rehash 工作正式开始
    - 在 rehash 进行期间，每次对字典执行增删改查操作时，程序除了执行指定的操作以外，还会顺带将 ht[0] 哈希表在 rehashidx 索引上的所有键值对 rehash 到 ht[1] 上，当 rehash 工作完成后，程序将 rehashidx 属性的值增一。(**rehashidx 最大值位 ht[0]的最大索引**)
    - 随着字典操作的不断执行，最终 ht[0] 上的所有键值对都会 rehash 到 ht[1]，此时程序将 rehashidx属性的值设为 -1，表示 rehash 操作已完成。
  - 当 ht[0] 包含的所有键值对都迁移到了 ht[1] 之后 ( ht[0] 变成空表 )，释放 ht[0], 将 ht[1] 设置为 ht[0], 并在 ht[1] 新创建一个空白哈希表，为下一次 rehash 做准备。

- rehash 执行条件
  - 扩展 ：服务器目前没有执行 BGSAVE 命令或者 BGREWRITEAOF 命令，且负载因子大于等于1；服务器郑州执行 BGSAVE 命令或者 BGREWRITEAOF 命令，且负载因子大于等于5。***扩展因子不同是因为在执行 BGSAVE命令或者 BGREWRITEAOF 命令的过程中，Redis需要创建当前服务器进程的子进程，而大多数操作系统采用 写时复制 ( Copy-On-Write) 技术来优化子进程使用效率，所以尽可能避免在子进程存在期间进行哈希表扩容操作。***
  - 收缩 ：负载因子小于 0.1时，自动开始对 哈希表执行收缩操作。

##### 跳跃表 ( skiplist )

---

- 跳跃表是一种**有序**数据结构，它通过在每个节点中维持多个指向其他节点的指针，从而达到快速访问节点的目的。
- 支持平均 O(logN) 、最坏 O(N) 复杂度的节点查找，还可以通过顺序性操作批量处理节点。
- 大部门情况下，跳跃树的效率可以和平衡树媲美，并且跳跃表的实现更为简单。
- Redis 中的应用
  - 使用 跳跃表 作为有序集合键的底层实现之一；如果一个有序集合包含的元素数量比较多，又或者有序集合中元素的成员是比较长的字符串时，Redis 就会使用跳跃表来作为有序集合键的底层实现。
  - 在集群节点中用作内部数据结构
- 数据结构 (redis.h/redisObject、 redis.h/zskiplistNode 、redis.h/zskiplist)

```c
typedef struct redisObject {
    unsigned type:4;
    unsigned encoding:4;
    unsigned lru:REDIS_LRU_BITS; /* lru time (relative to server.lruclock) */
    int refcount;
    void *ptr;
} robj;

typedef struct zskiplistNode {
    // 成员对象，指向一个字符串对象，字符串对象保存一个 SDS 值。
    robj *obj;
    // 分值，跳跃表中按分值从小到大排列
    double score;
    // 后退指针
    struct zskiplistNode *backward;
    // 层，层数越多。访问其他节点的速度就越快
    // 每次创建一个新跳跃表节点时，程序都根据 幂次定律 (power law，越大的数出现的概率越小)随机生成一个介于 1 和 32 之间的值作为 level 数组的大小，这个大小就是层的 “高度”；节点的第一层是level[0]，保存了原数组节点。
    struct zskiplistLevel {
        // 前进指针，用于访问表尾方向的其他节点
        struct zskiplistNode *forward;
        // 跨度，记录了前进指针所指向节点和当前节点的距离
        unsigned int span;
    } level[];
} zskiplistNode;
typedef struct zskiplist {
    // 表头和表尾节点
    struct zskiplistNode *header, *tail;
    // 表中节点的数量
    unsigned long length;
    // 表中层数最大的节点的层数
    int level;
} zskiplist;
```

- 在同一个跳跃表中，多个节点可以包含相同的分值，但每个节点的成员对象必须是唯一的。
- 跳跃表中的节点按照分值大小进行排序，当分值相同时，节点按照成员对象的大小进行排序。



##### 整数集合 ( intset )

---

- 整数集合是集合键的底层实现之一，当一个集合只包含整数值元素，并且这个集合的元素数量不多时，Redis就会使用整数集合作为集合键的底层实现。
- 数据结构 ( intset.h/intset )

```c
#define INTSET_ENC_INT16 (sizeof(int16_t))
#define INTSET_ENC_INT32 (sizeof(int32_t))
#define INTSET_ENC_INT64 (sizeof(int64_t))
typedef struct intset {
    // 编码方式，INTSET_ENC_INT16,INTSET_ENC_INT32,INTSET_ENCINT64
    uint32_t encoding;
    // 集合包含的元素数量
    uint32_t length;
    // 保存元素的数组，每个元素按值的大小从大到小排列，且不包含重复项,保存的元素类型取决于 encoding属性的值
    int8_t contents[];
} intset;
```

- 升级 ：当添加一个新元素到整数集合，并且新元素的类型比整数集合现有的所有元素的类型都要长时，整数集合要先进行升级 ，然后才能将新元素添加到整数集合中。
- 升级的步骤
  - 根据新元素的类型，扩展整数集合底层数组 ( content )的空间大小，并为新元素分配空间
  - 将底层数组现有的元素都转换成与新元素相同的类型，并将类型转换后的元素放置到正确的位上，而且在放置元素等待过程中，需要继续维持底层数组的 有序性质不变
  - 将新元素添加到底层数组里面
- 升级之后新元素的摆放位置 : 因为引发升级的新元素的长度总是比整数集合现有所有元素的长度都大， 所以这个**新元素的值要么就大于所有现有元素， 要么就小于所有现有元素**：
  - 在新元素小于所有现有元素的情况下， 新元素会被放置在底层数组的最开头 ( 索引 0 )
  - 在新元素大于所有现有元素的情况下， 新元素会被放置在底层数组的最末尾 ( 索引 length-1 )
- 升级的好处 :
  - 提升整数集合的灵活性 ：因为整数集合可以通过自动升级底层数组来适应新元素， 所以我们可以随意地将 int16_t 、 int32_t 或者 int64_t 类型的整数添加到集合中， 而不必担心出现类型错误， 这种做法非常灵活。
  - 节约内存 ：整数集合现在的做法既可以让集合能同时保存三种不同类型的值， 又可以确保升级操作只会在有需要的时候进行， 这可以尽量节省内存。
- 降级 ：**整数集合不支持降级操作，一旦对数组进行了升级，编码就会一直保持升级后的状态**



##### 压缩列表 ( ziplist )

---

- 压缩列表是 列表键和哈希键的底层实现之一；当 列表键/哈希键 只包含少量 列表项/键值对 ，并且每个 列表项/键值对的键和值 要么就是小整数值，要么就是长度比较短的字符串，那么 Redis 就会使用压缩列表来做列表键的底层实现。
- 压缩列表是 Redis 为了节约内存而开发的，是由一系列特殊编码的连续内存块组成的顺序型数据结构。一个压缩列表可以包含任意多个节点 ( entry )，每个节点可以保存一个字节数组或者一个整数值。
- 在 Redis 源码 ziplist.c 中，只发现了如下定义

```c
typedef struct zlentry {
    unsigned int prevrawlensize, prevrawlen;
    unsigned int lensize, len;
    unsigned int headersize;
    unsigned char encoding;
    unsigned char *p;
} zlentry;
```

- 在《Redis 设计与实现》中压缩列表中各个组成部分

| 属性 | 类型 | 长度 | 用途 |
| --- | --- | --- | :-: |
| zlbytes | uint32_t | 4字节 | 记录整个压缩列表占用的内存字节数 |
| zltail | uint32_t | 4字节 | 记录列表表尾节点举例压缩列表的起始地址有多少字节；通过这个偏移量，无需遍历整个压缩列表就可以确定表尾节点的地址 |
| zllen | uint16_t | 2字节 | 记录了压缩列表的节点数量；当这个属性值小于 UINT16_MAX(65535)时，是真是数量，等于这个值时，需要遍历整个压缩列表才能得出节点的真实数量 |
| entryX | 列表节点 | 不定 | 压缩列表包含的各个节点，节点的长度由节点保存的内容决定 |
| zlend | uint8_t | 1字节 | 特殊值 0xFF(十进制 255)，用于标记压缩列表的末端 |

| zlbytes   | zltail | zllen | entry1 | entry2 | ...  | entryN | zlend |
| --------- | ------ | ----- | ------ | ------ | ---- | ------ | ----- |
| p -> 0xd2 | 0xb3   | 0x5   |        |        |      | p+179  | 0xFF  |

- 如上是一个压缩列表示例

  - zlbytes 属性值 0xd2 (十进制210)，表示压缩列表的总长度为 210字节
  - zltail 属性值 0xb3 (十进制179)，表示如果有一个指向起始地址的指针p，p + 偏移量就是表尾节点 entry5的位置
  - zllen 0x5(十进制5)，表示压缩列表包含5个节点

- 节点的构成
| previous_entry_length   | encoding | content |
| --------- | ------ | ----- |
| 0x5 \|\| 0xFE00002766 | ... | ... |

- previous_entry_length:以字节为单位，记录了压缩列表中前一个列表的长度，**长度可以是1字节或者5字节**:
  - ***如果前一个节点的长度小于 254 字节，那么 previous_entry_length 属性的长度为 1 字节，前一节点的长度就保存在这1字节中***
  - ***如果前一节点的长度大于等于 254字节，那么previous_entry_length 属性长度为 5字节，其中属性的第一个字节会被设置为 0xFE (十进制 254)，之后的四字节则用于保存前一节点的长度。***
  - 如上面的表格，0x5表示前一节点长度为5字节；0xFE00002766 中 0xFE 表示这是一个5字节常的previous_entry_length 属性，之后的四字节 0x00002766(十进制 10086)则是前一字节的长度。
- encoding : 记录了节点 content属性所保存数据的类型以及长度
- content ：保存节点的值，节点值可以是一个字节数组或者整数，值的类型和长度由encoding 属性 决定。
- ***连锁更新：如果有多个连续的、长度介于 250字节到253字节之间的节点e1 至 eN, 记录这些节点的长度只需要1字节长的 previous_entry_length 属性，如果将一个大于等于254字节的新节点设置位压缩列表的表头节点，new -> e1 ->...->eN那么后序e1节点的previous_entry_length 属性从原来的1字节扩展成5字节长，此时 e1的长度就变成了介于 254字节至257字节之间，随后e2...eN会连续发送空间扩展操作，称之为 "连锁更新"***
- 添加新节点到压缩列表，或者从压缩列表中删除节点，可能会引发连锁更新操作，但这种操作出现的几率不高。(添加:new -> e1->e2->...->eN; 删除: big->small->e1->...->eN;)