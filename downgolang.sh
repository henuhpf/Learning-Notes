git clone https://github.com/golang/sys.git $GOPATH/src/github.com/golang/sys
git clone https://github.com/golang/net.git $GOPATH/src/github.com/golang/net
git clone https://github.com/golang/text.git $GOPATH/src/github.com/golang/text
git clone https://github.com/golang/lint.git $GOPATH/src/github.com/golang/lint
git clone https://github.com/golang/tools.git $GOPATH/src/github.com/golang/tools
git clone https://github.com/golang/crypto.git $GOPATH/src/github.com/golang/crypto

ln -s $GOPATH/src/github.com/golang/ $GOPATH/src/golang.org/x
