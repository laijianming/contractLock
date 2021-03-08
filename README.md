# contractLock
契约锁笔试项目
##客户端
客户端端口8080  
###客户端提供接口  
 url 方法 参数  
1、/file/upload  POST  MultipartFile file 文件上传  
2、/file/find GET 无 获取最新10条的上传文件信息  
3、/file/find/{uuid} GET uuid 根据uuid获取文件信息  
4、/file/download/{uuid} GET uuid 下载文件  
###客户端加密解密相关
####非对称加解密的使用
1、通过客户端的pri私钥对文件信息中的randomKey进行解密  
2、使用pri私钥对X-Signature进行加签操作
####对称加解密的使用    
1、从Server端获取到的文件流使用已解密的randomKey进行对称解密  
###客户端技术栈
springboot、rsa非对称加解密、aes对称加解密、HttpClient

##服务端
服务端端口9999
###服务端Servlet
FileServlet 处理 /file 的请求，doPost方法为文件存储， doGet方法为文件下载  
FileInfoServlet 处理 /find 的请求， doGet方法中以type参数来区分是查询10条最新数据还是根据uuid进行精确查询 
###服务端加密解密相关
####非对称加解密的使用
1、文件上传时，使用randomKey对文件进行对称加密后，再通过公钥对randomKey进行非对称加密，并保存到数据库中  
2、Filter鉴权，通过公钥对私钥加签的signature进行验证  
####对称加解密的使用  
1、文件保存到本地时使用randomKey进行一个对称加密存储
###服务端技术栈
Servlet、Jetty、Filter、AES、RSA、jdbc

##数据库
数据库使用的是 derby  
###建库及建表sql
CREATE DATABASE CONTRACT;  
CREATE TABLE fileinfo (  
    fileid INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)primary key,  
    fileUuid VARCHAR(50),  
    fileName VARCHAR(50),  
    randomKey VARCHAR(1500)  
);  
fileinfo表中包含文件的uuid、文件名、公钥加密后的randomKey  
TODO 文件的具体信息（创建时间、原始文件名等）可再建一张表与fileinfo进行关联

##运行方法
1、首先在服务器上搭建Derby数据库，并将contract库以及fileinfo表导入  
2、将Client使用maven打包成jar包，再通过 nohup java -jar client-0.0.1-SNAPSHOT.jar 运行Client端  
3、Server端通过插件中的jetty的run-war命令，可打包成jar包，再通过 nohup java -jar Server-1.0-SNAPSHOT.jar 运行Client端  





