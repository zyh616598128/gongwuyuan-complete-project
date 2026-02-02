# 第一阶段：构建应用
FROM openjdk:17-jdk-slim

WORKDIR /app

# 安装Maven
RUN apt-get update && \
    apt-get install -y wget curl && \
    wget https://downloads.apache.org/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz && \
    tar -xvf apache-maven-3.8.6-bin.tar.gz && \
    mv apache-maven-3.8.6 /opt/maven && \
    rm apache-maven-3.8.6-bin.tar.gz

# 设置环境变量
ENV MAVEN_HOME=/opt/maven
ENV PATH=$PATH:$MAVEN_HOME/bin

# 复制项目文件
COPY pom.xml .
COPY src src

# 构建应用
RUN mvn clean package -DskipTests

# 运行应用
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/gwy-exam-backend-1.0.0.jar"]