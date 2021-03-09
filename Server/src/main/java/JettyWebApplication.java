import constant.ServerConstant;
import filter.SignFilter;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlet.FileInfoServlet;
import servlet.FileServlet;

import javax.servlet.DispatcherType;
import java.util.EnumSet;


/**
 * @author jianming
 * @create 2021-03-08-17:24
 */
public class JettyWebApplication {

    public static void main(String[] args) throws Exception {

        Server server = new Server(ServerConstant.PORT);
        // 创建servlet容器
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // 添加servlet映射
        context.addServlet(new ServletHolder(new FileServlet()), "/file");

        // 添加 查询servlet
        context.addServlet(new ServletHolder(new FileInfoServlet()), "/find");

        // 添加过滤器 用于请求验证
        context.addFilter(new FilterHolder(new SignFilter()), "/*", EnumSet.of(DispatcherType.REQUEST));

        context.setClassLoader(Thread.currentThread().getContextClassLoader());

//        MyJettyHandler myJettyHandler = new MyJettyHandler();
//        myJettyHandler.setServer(server);
//        myJettyHandler.start();

        // 启动jetty服务
        server.start();
        server.join();
    }


}
