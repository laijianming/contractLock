package servlet;

import dao.FileDao;
import entity.FileInfo;
import org.eclipse.jetty.util.StringUtil;
import uitls.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author jianming
 * @create 2021-03-08-22:18
 */
public class FileInfoServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        if (StringUtil.isBlank(type)) {
            return;
        }
        PrintWriter out = response.getWriter();

        if (type.equals("new")) {
            // 查询最新的10条uuid数据
            List<FileInfo> tenFileinfo = FileDao.getTenFileinfo();
            if (tenFileinfo == null || tenFileinfo.isEmpty()) {
                out.write("占无数据");
            } else {
                out.write(JsonUtils.objectToJson(tenFileinfo));
            }
        } else if (type.equals("one")) {
            String uuid = request.getParameter("uuid");
            FileInfo fileinfo = FileDao.getFileinfo(uuid);
            if (fileinfo == null) {
                out.write(JsonUtils.objectToJson(new FileInfo()));
            } else {
                out.write(JsonUtils.objectToJson(fileinfo));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        super.service(req, res);
    }
}
