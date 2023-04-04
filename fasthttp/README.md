# snow fasthttp
生成分布式id 这是一个练手项目, 如用于生产, 参见postgre的协议。 

这是一个go项目 

进入项目命令行 

go mod vendor 

go run main.go 

启动如下网页服务 

自增 
127.0.0.1:8486/sequence 

雪花 
127.0.0.1:8486/snow 