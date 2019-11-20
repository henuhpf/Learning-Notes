#### Dockerfile文件解析
- 指令不区分大小写，但一般使用大写增加可读性
- 注释行以 # 开头
- [GitHub](https://github.com/henuhpf/psweb)

```dockerfile
# 以 alpine 镜像作为当前镜像的基础
FROM alpine
# 执行维护这(maintainer) 位 "xxx@mail.com"
LABEL maintainer="xxx@mail.com"
# 安装 Node.js 和 NPM
RUN apk add --update nodejs nodejs-npm
# 将应用的代码复制到镜像当中
COPY . /src
# 设置新的工作目录
WORKDIR /src
# 安装依赖包
RUN npm install
# 记录应用的网络端口
EXPOSE 8080
# 将 app.js 设置为默认运行的应用
ENTRYPOINT ["node", "./app.js"]
```

- 每个 Dockerfile 文件的第一行都是 FROM 命令。
  - FROM 指令指定的镜像，会作为当前镜像的一个基础镜像层
  - 当前应用的剩余内容会作为新增镜像层添加到基础镜像层之上
  - 当前镜像层 ("FROM alpine")

- 接下来，Dockerfile 中通过标签 (LABEL) 指定了当前镜像的维护者
  - 每个标签是一个键值对 (Key-Value), 在一个镜像当中可以通过增加标签的方式来为镜像添加自定义元数据

- RUN apk add .. 
  - RUN 命令会在 FROM 指定的 alpine 基础镜像层之上，新建一个镜像层来存储这些安装内容
  - 当前镜像层 ("FROM alpine" -> "RUN apk add ..")

- COPY . /src
  - 将应用相关文件从构件上下文复制到了当前镜像中，并且新建一个镜像层来存储
  - 当前镜像层 ("FROM alpine" -> "RUN apk add .." -> "COPY . /src")

- WORKDIR 
  - 位 Dockerfile 中尚未执行的指令设置工作目录
  - 该目录与镜像相关，并且会作为元数据记录到镜像配置中，但不会创建新的镜像层

- RUN npm install
  - 根据 package.json 中的配置信息，安装当前应用的相关依赖包
  - npm 会在前面设置的工作目录中执行，并且在镜像中新建镜像层来保存相应的依赖文件。
  - 当前镜像层 ("FROM alpine" -> "RUN apk add .." -> "COPY . /src" -> "RUN npm install") 

- EXPOSE 8080
  - 当前应用需要通过 TCP 端口 8080 对外提供一个 Web 服务，所以通过此命令来完成相应端口的设置
  - 这个配置信息会作为镜像的元数据保存下来，不会产生新的镜像层

- 最后，通过ENTRYPOINT 指令来指定当前镜像的入口程序；
ENTRYPOINT 指定的配置信息也是通过镜像元数据的形式保存，不会产生新镜像层

#### 构建具体镜像以及推送到仓库

- docker image build -t web:latest . 
  - 构建镜像，在执行命令时，保证当前目录包含 Docker 和应用程序的代码

- docker image push 命令默认的推送地址是 Docker Hub
  - 推送镜像之前，需使用 "docker login" 登录 Docker Hub

- 因为当前用户没有 docker.io/web:latest 镜像仓库的权限,所以只能尝试推送到 <Your_DockerID>/web:latest
- docker image tag web:latest <Your_DockerID>/web:latest
  - 为当前镜像打一个标签
  - docker image tag <current-tag> <new-tag>

- docker image push <Your_DockerID>/web:latest

#### 区分是否新建镜像层

- 如果指令的作用是向镜像中增添新的文件或程序，那么这条指令就会新建镜像层
- 如果只是告诉 Docker 如何完成构建或者如何运行应用程序，就只会增加镜像的元数据
- docker image history<container-id> 查看构建镜像的过程中都执行了什么命令
```shell
[root@iZuf6f3ivz9sb8fh3g5vjhZ psweb]# docker image history web:latest
IMAGE               CREATED             CREATED BY                                      SIZE                COMMENT
bf208f8542ab        2 minutes ago       /bin/sh -c #(nop)  ENTRYPOINT ["node" "./app…   0B                  
b41664816326        2 minutes ago       /bin/sh -c #(nop)  EXPOSE 8080                  0B                  
6f56a0d2df64        2 minutes ago       /bin/sh -c npm install                          20.6MB              
7897180e4da1        3 minutes ago       /bin/sh -c #(nop) WORKDIR /src                  0B                  
5059869c3297        3 minutes ago       /bin/sh -c #(nop) COPY dir:061555705bf8e560e…   22kB                
6089b70424d3        3 minutes ago       /bin/sh -c apk add --update nodejs nodejs-npm   45.3MB              
307884cbbfc7        11 minutes ago      /bin/sh -c #(nop)  LABEL maintainer=nigelpou…   0B                  
965ea09ff2eb        11 days ago         /bin/sh -c #(nop)  CMD ["/bin/sh"]              0B                  
<missing>           11 days ago         /bin/sh -c #(nop) ADD file:fe1f09249227e2da2…   5.55MB 
```
  - 后面Size 列对应的数值不为零的指令，新建了镜像层
- docker image inspect <container-id>
```shell
...
"RootFS": {
            "Type": "layers",
            "Layers": [
                "sha256:77cae8ab23bf486355d1b3191259705374f4a11d483b24964d2f729dd8c076a0",
                "sha256:693f93a3cae5cf68d20981daf386202e862fec4b8733ee32e29c5febd54732dc",
                "sha256:c0fd1da9e3a695404c317b86ea7f739e39e53b1a37fade016114faa549759f1d",
                "sha256:a8f83575da90edcef21a51aef96b56d39c71d1af0ae1d29db15e4551959c7194"
            ]
        },

...
```
  - "Layers" 中确认有多少层被创建了

#### 多阶段构建

- [GitHub](https://github.com/henuhpf/atsea-sample-shop-app)
- 生产环境中应将镜像缩小到仅包含运行应用所需的内容
- 不同的 Dockerfile写法对镜像的大小会产生显著的影响
  - 每一个 RUN 指令会新建一个镜像层，因此可以通过使用 && 连接多个命令以及使用反斜杠(\) 换行的方法，将多个命令包含在一个 RUN 指令中

- 多阶段构建方式使用一个 Dockerfile，其中包含多个 FROM 指令， 每一个 FROM 指令都是一个新的构建阶段， 且可以方便地复制之前阶段的构件

```dockerfile
FROM node:latest AS storefront
WORKDIR /usr/src/atsea/app/react-app
COPY react-app .
RUN npm install
RUN npm run build

FROM maven:latest AS appserver
WORKDIR /usr/src/atsea
COPY pom.xml .
RUN mvn -B -f pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve
COPY . .
RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml package -DskipTests

FROM java:8-jdk-alpine AS production
RUN adduser -Dh /home/gordon gordon
WORKDIR /static
COPY --from=storefront /usr/src/atsea/app/react-app/build/ .
WORKDIR /app
COPY --from=appserver /usr/src/atsea/target/AtSea-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "/app/AtSea-0.0.1-SNAPSHOT.jar"]
CMD ["--spring.profiles.active=postgres"]
```
- 3个FROM指令，每个构成一个单独的构建阶段，各个阶段在内部从 0 开始编号，可以在后面跟上 AS 起一个别名
  - 阶段 0 叫做 storefront
  - 阶段 1 叫做 appserver
  - 阶段 2 叫做 production

- storefront 阶段拉取了超600MB的镜像，设置了工作目录，复制一些应用代码，然后使用2 个 RUN 指令执行 npm 操作，生成了3个镜像层，结束后得到一个包含许多构建工具和少量应用代码的镜像
- appserver 阶段拉取了超过700M的镜像， 这个阶段生成了4个镜像层，得到一个包含许多构建工具和少量应用代码的镜像
- production 阶段拉取大小约 150MB 的java:8-jdk-alpine 镜像，设置一个工作目录，从 storefront 阶段生成的镜像中复制一些应用代码，然后设置一个不同的工作目录，从 appserver 阶段生成的镜像中复制应用相关的代码。最后，production设置当前应用程序为容器启动时的主程序

- ***COPY --from*** 指令，从之前阶段构建的镜像中仅复制生产环境相关的应用代码，而不会复制生产环境不需要的构件