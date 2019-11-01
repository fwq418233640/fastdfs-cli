package com.ch.fastdfsapi.filter;

import com.ch.fastdfsapi.dao.UserRepository;
import com.ch.fastdfsapi.model.entity.User;
import com.ch.fastdfsapi.utils.Msg;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Result 接口 安全过滤器
 *
 * @author ch
 */
@Configuration
@Slf4j
public class ResultApiFilter implements Filter {

    /**
     * 需要保护的接口
     */
    private static final List<String> SECURITY_LIST = new ArrayList<>();

    @Autowired
    private UserRepository repository;

    static {
        // pc登录
        SECURITY_LIST.add("/file/deleted");
        SECURITY_LIST.add("/file/find/page");
    }

    /**
     * 探测请求
     */
    private static final String OPTIONS = "OPTIONS";

    @Override
    public void init(FilterConfig filterConfig) {
        SpringBeanAutowiringSupport
                .processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        if (OPTIONS.equals(httpRequest.getMethod())) {
            httpResponse.setStatus(204);
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Headers",
                    "Content-Type, x-requested-with, webtoken, withCredentials,x-access-token");
            httpResponse.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,DELETE,PUT");
        }

        log.debug("请求地址 : {}", getIpAddress(httpRequest));

        ObjectMapper mapper = new ObjectMapper();
        // 从请求header中拿出 webToken
        String requestUrl = httpRequest.getRequestURL().toString();
        log.debug("请求路径 : {}", requestUrl);
        try {
            // 保护部分接口
            for (String url : SECURITY_LIST) {
                if (!requestUrl.contains(url)) {
                    chain.doFilter(request, response);
                    return;
                }
            }
            // 校验token值
            String webToken = httpRequest.getHeader("webToken");
            if (StringUtils.isEmpty(webToken)) {
                httpResponse.getWriter().write(mapper.writeValueAsString(Msg.fail("请重新登陆")));
            } else {
                Optional<User> optional = repository.findByToken(webToken);
                if (optional.isPresent()) {
                    chain.doFilter(request, response);
                } else {
                    httpResponse.getWriter().write(mapper.writeValueAsString(Msg.fail("请重新登陆")));
                }
            }
        } catch (Exception e) {
            log.error("filter : {}", e.toString());
            httpResponse.getWriter().write(mapper.writeValueAsString(Msg.fail(e.getMessage())));
        }
    }

    @Override
    public void destroy() {
    }


    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request 请求
     * @return ip 地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 探测 IP 常量
     */
    private static final String UNKNOWN = "unknown";
}
