- Docker Compose 能在 Docker 节点上， 以单引擎模式进行多容器应用的部署和管理。
- docker-compose up 
  - 命令用于部署一个 Compose 应用。
  - 默认情况下该命令会读取名为 docker-compose.yml 或 docker-compose.yaml 的文件，用户也可以使用 -f 命令指定其他文件名。
  - 通常情况下， 会使用 -d 参数令应用在后台启动。

- docker-compose stop
  - 命令会停止 Compose 应用相关的所有容器，但不会删除它们。
  - 被停职的应用可以通过 docker-compose restart 命令重新启动。

- docker-compose rm
  - 用于删除已停止的 Compose 应用。
  - 会删除网络和容器，但不会删除卷和镜像

- docker-compose restart
  - 重启已停止的 Compose 应用。
  - 如果用户在停止该容器后对它进行更改，更改的内容不会反应到重启后的应用中，这时需要重新部署应用使更改的内容生效。

- docker-compose ps
  - 用于列出 Compose 应用中的各个容器。
  - 输出内容包含 当前状态、容器运行的命令 以及 网络端口。

- docker-compose down
  - 停止并删除运行中的 Compose 应用
  - 会会删除容器和网络，但是不会删除卷和镜像。

- docker-compose top
  - 列出各个服务(容器)内运行的进程
  - PID编号是在 Docker 主机上 而不是在 容器内的进程ID
