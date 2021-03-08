package servlet;


import constant.ServerConstant;
import org.eclipse.jetty.server.Request;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * @MultipartConfig 使用MultipartConfig注解标注改servlet能够接受文件上传的请求
 * @author jianming
 * @create 2021-03-08-17:04
 */
public class FileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 用于通过文件的uuid来获取文件流
        System.out.println("调用了get方法");
        ServletOutputStream outputStream = resp.getOutputStream();
        // 用outputStream将文件流输出
        PrintWriter out = resp.getWriter();
        out.println("HelloServlet! get");
    }


    private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement("D:\\IDEA_Workspace\\contract\\file");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 上传文件
        request.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);
        // 获取文件内容
        Part file = request.getPart("file");
        String disposition = file.getHeader("Content-Disposition");
        String suffix = disposition.substring(disposition.lastIndexOf("."),disposition.length()-1);
        // 保存到本地
        //随机的生存一个32的字符串
        String uuid = UUID.randomUUID().toString();
        String filename = uuid+suffix;
        //获取上传的文件名
        InputStream is = file.getInputStream();
        //动态获取服务器的路径
        String serverpath = ServerConstant.FILE_PATH;
        FileOutputStream fos = new FileOutputStream(serverpath+"/"+filename);
        byte[] bty = new byte[1024];
        int length =0;
        while((length=is.read(bty))!=-1){
            fos.write(bty,0,length);
        }
        fos.close();
        is.close();
        // TODO 保存uuid对应的文件


        // 返回uuid
        PrintWriter out = response.getWriter();
        out.println(uuid);
    }

    /**
     * 方法先到service，再到get/post，返回时service最后返回
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        super.service(req, res);
    }

    @Override
    public void init() throws ServletException {
        System.out.println("初始化");
        super.init();
    }
}
