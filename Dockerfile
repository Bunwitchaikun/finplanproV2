# Stage 1: Build stage - สร้าง .jar file
FROM maven:3.8.5-openjdk-17 AS build

# สร้าง WORKDIR
WORKDIR /app

# คัดลอกเฉพาะ pom.xml เพื่อใช้ประโยชน์จาก Docker layer caching
COPY pom.xml .

# ดาวน์โหลด dependencies ทั้งหมดมาเก็บไว้ก่อน
# RUN mvn dependency:go-offline
# หมายเหตุ: ขอข้ามขั้นตอนนี้ไปก่อนเพื่อความเรียบง่าย แต่เป็น Practice ที่ดีในโปรเจกต์จริง

# คัดลอก source code ทั้งหมด
COPY src ./src

# รัน clean package เพื่อสร้าง .jar (ข้ามเทสเพราะเราจะรันใน stage แยก)
RUN mvn clean package -DskipTests

# Stage 2: Run stage - รันแอปพลิเคชัน
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# คัดลอก .jar จาก build stage มายัง run stage
COPY --from=build /app/target/*.jar app.jar

# เปิด Port ที่แอปพลิเคชันจะรัน
EXPOSE 8083

# คำสั่งสำหรับรันแอปพลิเคชัน
ENTRYPOINT ["java", "-jar", "app.jar"]
