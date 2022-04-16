#! /bin/bash
git_username=$(git config user.name)
repository_name=$(basename "$PWD")

git remote remove origin
# git remote add github git@github.com:$git_username/$repository_name.git
# git remote add gitee git@gitee.com:$git_username/$repository_name.git
git remote add github https://github.com/$git_username/$repository_name.git
git remote add gitee https://gitee.com/$git_username/$repository_name.git

echo "`git remote -v`"
