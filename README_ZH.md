
# CAFS 1.0: 陆地生态系统碳循环同化和预测系统

## 介绍
CAFS1.0是一个先进的陆地生态系统碳动力学近期迭代预测系统。该系统利用数据融合和深度学习技术集成了实时气象和生物特征观测数据。CAFS1.0在中国四个森林点成功应用，标志着生态预报和生态系统管理的突破。

## 系统架构
CAFS1.0由Vue.js前端和Java SpringBoot后端组成。
-**前端（目录：`front`）：**使用Vue.js开发的交互式用户界面，用于数据可视化和用户交互。
-**后端（目录：`Backend`）：**使用Java Spring Boot开发的服务器端应用程序，用于数据处理和系统逻辑。

## 先决条件
- Node.js and npm (前端)
- Java JDK 8 or higher (后端)

## 安装

### 前端编译安装
1.克隆存储库并导航到前端目录：   
   ```
   git clone https://github.com/damonwan1/CAFS1.0.git
   cd CAFS1.0/front
   ```
2. 安装所需的npm软件包：
   ```
   npm install
   ```
3. 运行Vue.js应用程序：
   ```
   npm run serve
   ```

### 后端编译安装
1. 导航到后端目录：
   ```
   cd ../backend
   ```
2. 构建Spring Boot应用程序（确保安装了Java JDK）：
   ```
   ./mvnw clean install
   ```
3. 运行Spring Boot应用程序：
   ```
   ./mvnw spring-boot:run
   ```

## 用法
提供有关如何使用该系统的说明和示例。如有必要，包括屏幕截图或代码片段。

## 贡献
欢迎对CAFS 1.0做出贡献。请阅读“CONTRIBUTING.md”，了解我们的捐款指南和提交提取请求的流程。

## 许可证
此项目以[MIT]获得许可-有关详细信息，请参阅“许可证”文件。

## 致谢
感谢在CAFS 1.0开发中发挥重要作用的合作者、资助机构和贡献者。