#bin/bash
# 使用bing首页的每日图片或本地图片设置壁纸

# get 请求获取 bing 图片 json 数据描述的 url
# format 指定格式， idx 指定 n 天前图片， n 指定一次获取n张
# https://cn.bing.com/HPImageArchive.aspx?format=js&idx=2&n=1&nc=1611061006874&pid=hp

# 定义本地图片路径
local_pic=~/Pictures/Wallpaper

# 参数判断及变量定义
[[ $# -gt 5 ]] && echo "wrong parameters!" && exit 90
[[ $# -gt 0 ]] && param=($@)
echo ${param}

for i in ${param[@]}
do 
    case $i in
        '-i'|'--interval')
        interval=$2
        shift 2
        ;;
        '-d'|'--day')
        day=$2
        [[ $day -gt 7 || $day -lt 0 ]] && echo "only 0~7 days allowed" && exit 90
        shift 2
        ;;
        'local'|'remote')
        mothod=$1
        shift 1
        ;;
        [0-9]*)
        continue
        ;;
        '--help')
            echo "Usage: chbg local|remote [-d day] [-i interval]"
            echo "   eg: chbg local -i 120          本地图片设置桌面壁纸，壁纸切换时间为2分钟"
            echo "   eg: chbg remote -d 3 -i 120    获取 bing 3天内的壁纸进行设置，壁纸切换时间为2分钟"
            echo ""
            echo "   -d     --day                   指定多少天之内的图片，在remote时使用，默认4天"
            echo "   -i     --interval              设置壁纸切换的时间间隔，默认180秒"
            echo "   remote                         选择从 bing 网站获取每日图片"
            echo "   local                          使用本地图片设置桌面壁纸"
            echo "   --help                         打印此帮助信息"
        ;;
        *)
        echo "error parameters"
        exit 90
        ;;
    esac
done

# 清除之前的进程
function onlyone(){
    num=`ps -x | grep /chbg | wc -l`
    if [[ $num -gt 1 ]]; then
        ps -x | grep /chbg | grep -v grep | head -n -1 | awk '{print "kill",$1}' | shift
    fi
}

# 判断本地还是bing图片设置壁纸
if [[ ${method:=local} == 'local' ]];then
{
    onlyone
    while :
    do
        feh -D 0.3 -z --bg-fill $local_pic
        sleep ${interval:=180}
    done
} &
elif [[ $method == 'remote' ]];then
{
    onlyone
    # 进入桌面时没有加载到数据， 先默认设置本地图片
    feh -D 0.3 -z --bg-fill $local_pic

    # 定义数据存储图片的url、图片描述
    declare -A url_list
    declare -A desc_list

    for i in {0..${day:=4}}
    do
        get="https://cn.bing.com/HPImageArchive.aspx?format=js&idx=${i}&n=1&pid=hp"
        # jq 对数据进行 json 格式转换
        raw_info = `curl -s $get`
        url = `echo $raw_info | jq -r '.images[].url'`
        url_list[$i] = "https://cn.bing.com/$url"
        desc_list[$i] = `echo $raw_info | jq -r '.images[].copyright'`
    done

    # 随机选取并设置壁纸
    if [[ ${#url_list[@]} -gt 0 ]]; then
        while :
        do
            i = $((RANDOM%$((day+1))))
            # feh 支持 url设置壁纸
            feh -D 0.3 --bg-fill ${url_list[$i]}
            # 通过发送消息到 dunst 展示图片信息
            notify-desktop ${desc_list[$i]} &>/dev/null
            sleep ${interval:=180}
        done
    fi
} &
fi