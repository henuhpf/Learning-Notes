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
