FROM openjdk:11.0.9.1
ENV TZ=Asia/Shanghai

ADD target/minio-service-1.0.0.jar minio-service-1.0.0.jar

EXPOSE 8099
ENTRYPOINT java -Xmx3550m -Xms512m ${JAVA_OPTS} -Dlog4j2.formatMsgNoLookups=true -verbose:gc -XX:+PrintGCDetails -Xloggc:/logs/gc.log -jar /minio-service-1.0.0.jar
