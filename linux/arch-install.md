https://archlinuxstudio.github.io/ArchLinuxTutorial

https://wiki.archlinux.org

https://github.com/LucasWang474/ArcoLinux-Configurations

# wm wiki
https://awesomewm.org/

# 设置静态 IP
- 临时设置
```bash
ip address add 192.168.0.104/24 broadcast + dev enp0s3
ip address del 192.168.0.104/24 broadcast + dev enp0s3
ip route add default via 192.168.0.100 # ip route add 0.0.0.0/0 via 192.168.0.100
```
- 持久化
修改 /etc/netctl 下的文件
```bash
net start my_static_profile
net enable my_static_profile
```