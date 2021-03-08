package servlet;

import dao.FileDao;
import org.eclipse.jetty.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jianming
 * @create 2021-03-08-22:18
 */
public class FileInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        if (StringUtil.isBlank(type)) {
            return;
        } else if (type.equals("all")) {

        } else if (type.equals("one")) {
            String uuid = req.getParameter("uuid");
            FileDao.getFileinfo("uuidtest");
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
