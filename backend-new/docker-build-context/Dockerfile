# 使用官方OpenJDK镜像
FROM openjdk:17-jdk-slim

WORKDIR /app

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 安装Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 设置国内Maven仓库镜像
RUN mkdir -p ~/.m2 && \
    echo '<?xml version="1.0" encoding="UTF-8"?>' > ~/.m2/settings.xml && \
    echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"' >> ~/.m2/settings.xml && \
    echo '          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"' >> ~/.m2/settings.xml && \
    echo '          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0' >> ~/.m2/settings.xml && \
    echo '                              http://maven.apache.org/xsd/settings-1.0.0.xsd">' >> ~/.m2/settings.xml && \
    echo '  <mirrors>' >> ~/.m2/settings.xml && \
    echo '    <mirror>' >> ~/.m2/settings.xml && \
    echo '      <id>aliyunmaven</id>' >> ~/.m2/settings.xml && \
    echo '      <mirrorOf>*</mirrorOf>' >> ~/.m2/settings.xml && \
    echo '      <name>阿里云公共仓库</name>' >> ~/.m2/settings.xml && \
    echo '      <url>https://maven.aliyun.com/repository/public</url>' >> ~/.m2/settings.xml && \
    echo '    </mirror>' >> ~/.m2/settings.xml && \
    echo '  </mirrors>' >> ~/.m2/settings.xml && \
    echo '</settings>' >> ~/.m2/settings.xml

# 复制项目文件
COPY pom.xml .
COPY src src

# 构建应用
RUN mvn clean package -DskipTests

# 运行应用
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/gwy-exam-backend-1.0.0.jar"]