package filter;

import constant.ServerConstant;
import org.eclipse.jetty.util.StringUtil;
import rsaEncript.RSASign;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jianming
 * @create 2021-03-09-1:59
 */
public class SignFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        ServletOutputStream out = res.getOutputStream();
        // 验证签名
        String sid = req.getHeader("X-SID");
        String signature = req.getHeader("X-Signature");
        if (StringUtil.isBlank(sid) || StringUtil.isBlank(signature)) {
            // 没有签名，直接返回403
            out.print("403 请求不合法");
        } else {
            // 有签名的情况，使用公钥验证签名
            try {
                if (RSASign.verify(ServerConstant.pub, signature, sid)) {
                    System.out.println("验证签名通过");
                    // 签名通过
                    chain.doFilter(request,response);
                } else {
                    // 签名不通过
                    out.print("403 请求不合法");
                }
            } catch (Exception e) {
                out.print("403 请求不合法");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {

    }
}
