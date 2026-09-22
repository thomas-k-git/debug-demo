JMV otions: -Xlog:gc*,gc+humongous=debug,gc+jni=debug:file=logs/gc-backend.log:time,uptime,level,tags:filecount=5,filesize=10m
-Xlog:gc*,gc+humongous=debug,gc+jni=debug:file=logs/gc-backend.log:time,uptime,level,tags:filecount=5,filesize=10m
-Xmx6700M
-XX:G1HeapRegionSize=210M
-XX:G1ReservePercent=15

-> JVM 21 - G1 region pinning
-> JVM 25 - fixed!

with ~24 cores