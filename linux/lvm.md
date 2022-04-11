# LVM 安装 Arch 常用操作

### 物理卷 - PV

```bash
// 创建 pv
pvcreate mypv /dev/sda
pvcreate mypv /dev/sda2
// 查看 pv
pvs
pvdisplay

```

### 卷组 - VG

```bash
// 创建 vg
vgcreate -l +100%FREE myvg

// 扩大 vg
vgextend myvg /dev/sda /dev/sdb /dev/sdc{1,3}

// 查看 vg
vgs
vgdisplay
```

### 逻辑卷 - LV

- 新增

```bash
// 使用指定大小空间创建 myvg
lvcreate -L 10G myvg -n mylv

// 使用剩余100%空闲空间创建 myvg
lvcreate -l +100%FREE myvg -n mylv

// 指定在某个 PV 中创建 lv, 如在较小的 SSD 硬盘上创建 根文件系统，并在较慢的 机械硬盘 上创建 家目录卷
lvcreate -L 10G myvg -n mylv /dev/sdc1

// 查看 lv
lvs
lvdisplay
```

- 扩展
```bash
// 扩展逻辑卷操作可在挂载时进行
// 1. 增加逻辑卷大小， 此步骤操作完成时， 使用lvs可以看到空间新增成功， 但是使用 fdisk -l 或 df -h 命令查看时, 发现文件系统中的分区大小并没有变化
lvextend -L +2G /dev/myvg/mylv 
lvextend -l +100%FREE /dev/myvg/mylv

// 2. 刷新文件系统, 此时可以看到文件系统中的分区大小也随之改变了
resize2fs /dev/myvg/mylv

// 3. 检测文件系统, 查询文件系统是否有问题
e2fsck -f /dev/myvg/mylv
```

- 缩减逻辑卷
```bash

// 缩减逻辑卷不能在挂载时进行, 需要先卸载掉该分区
// 1. 确定要缩减为多大? 前提是:至少能容纳原有的所有数据
// 2. 卸载当前正在挂载中的LV并检测文件系统
umount /dev/mapper/myvg-mylv # umount /dev/myvg/mylv
e2fsck -f /dev/myvg/mylv
// 3. 缩减逻辑边界
resize2fs /dev/myvg/mylv 5G
// 4. 缩减物理边界
lvreduce -L 5G /dev/myvg/mylv
// 5. 检测文件系统
e2fsck -f /dev/myvg/mylv
```

- 移除逻辑卷
```bash

// 1. 确定要移除的PV, 如 移除 /dev/sdc1 
// 2. 使用 pvmove 将此 PV 上的数据转移到其它 PV, 如果不指定目标 PV 则会转移到同卷组其他 PV 上
pvmove /dev/sdc1
pvmove /dev/sdc1 /dev/sda # 待确认
// 3. 使用 vgreduce 从卷组中将此 PV 移除, 但没有删除 /dev/sdc1 的 PV
vgreduce myvg /dev/sdc1
// 4. 使用 pvremove 删除物理卷，真正删除 pv 的物理卷
pvremove /dev/sdc1

```

### 格式化逻辑卷

```bash
// ext4 格式
mkfs.ext4 /dev/mapper/myvg-mylv

```
