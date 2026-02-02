# 使用官方OpenJDK 11运行时环境作为基础镜像
FROM openjdk:11-jre-slim

# 设置工作目录
WORKDIR /app

# 复制构建好的jar文件到容器中
# 如果没有预先构建的jar文件，则复制源代码并构建
COPY src /app/src
COPY pom.xml /app/

# 构建应用
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# 暴露端口
EXPOSE 8080

# 运行应用
ENTRYPOINT ["java", "-jar", "target/*.jar"]