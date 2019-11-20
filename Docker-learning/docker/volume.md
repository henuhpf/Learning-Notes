- 容器中持久化数据的方式推荐采用卷
- Linux 系统中，卷存储的目录是/var/lib/docker/<storage-driver>/ 之下；
- Windows中，卷存储位于C\ProgramData\Docker\windowsfilter\ 目录之下

- docker volume create myvol 
  - 创建名为 myvol 的卷

- docker volume ls
  - 查看卷
  - 查看详情使用 docker volume inspect myvol

- 删除卷
  - docker volume prune 删除未装入到某个容器或者服务的所有卷
  - docker volume rm 允许删除指定卷
  - 两种删除命令都不能删除正在被容器或者服务使用的卷

- docker container run -dit --name voltainer --mount source=myvol,target=/vol alpine
  - 创建一个新的容器，并挂载一个名为 myvol 的卷
  - 如果指定了已存在的卷，Docker 会使用该卷
  - 如果指定的卷不存在， Docker 会创建该卷