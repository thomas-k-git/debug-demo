JMV otions: -Xlog:gc*,gc+humongous=debug,gc+jni=debug:file=logs/gc-backend.log:time,uptime,level,tags:filecount=5,filesize=10m
-Xmx6700M
-XX:G1HeapRegionSize=512M
-XX:G1ReservePercent=15