- docker container run || docker run 
  - 启动新容器
  - docker container run -it ubuntu /bin/bash 绘制前台启动一个 Ubuntu 容器，并运行 Bash

- Ctrl-PQ
  - 断开 Shell 和容器终端之间的链接，并在退出后保持容器在后台处于运行(UP) 状态

- docker container ls || docker ps
  - 列出所有在运行状态的容器
  - 加上 -a 参数，还能看到处于停止(Exited) 状态的容器

- docker container exec || docker exec
  - 允许用户在运行状态的容器中，启动一个新的进程
  - docker container exec -it <container-name or container-id> bash 命令会在容器内部启动一个 Bash Shell 进程，并连接到该 Shell,需要镜像包含Bash Shell

- docker container stop <container-name or container-id>
  - 停止运行中的容器，并将状态置为Exited(0)
  - 此命令通过发送 SIGTERM 信号给容器内 PID 为1的进程达到目的；如果进程没有在 10s 之内得到清理并停止运行，那么会接着发送 SIGKILL 信号来强制停止容器

- docker container start <container-name or container-id>
  - 重启处于停止状态的容器

- docker container rm <container-name or container-id>
  - 通过容器名称或者ID 来指定删除的容器
  - 推荐先 stop 命令停止容器，再使用rm 删除

- docker container inspect <container-name or container-id>
  - 显示容器的配置细节和运行时信息
