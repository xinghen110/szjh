package com.magustek.szjh.config;

import com.magustek.szjh.user.bean.UserAuthSet;
import com.magustek.szjh.user.bean.UserInfo;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.base.BaseResponse;
import com.magustek.szjh.utils.constant.AllowResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * 过滤器，用来做权限校验
 * */
@Slf4j
public class SecurityFilter extends GenericFilterBean{
    private List<AntPathRequestMatcher> resourceList;
    private List<String> superUser= Arrays.asList("yangjiawei","shihao1");
    //初始化匿名访问资源列表
    SecurityFilter(){
        resourceList = new ArrayList<>(AllowResource.resource.length);
        Arrays.stream(AllowResource.resource).forEach(resource-> resourceList.add(new AntPathRequestMatcher(resource)));
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        BaseResponse response = new BaseResponse()
                .setStateCode(BaseResponse.FORBIDDEN)
                .setMsg("用户权限不足!");

        boolean flag = false;

        log.info("进入认证授权拦截器...");
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;
        log.info("========请求url:"+req.getScheme()+"://"+req.getServerName()+
                ":"+req.getServerPort()+req.getServletPath()+"========");

        //匿名资源放行
        List<AntPathRequestMatcher> allowList = resourceList.stream().filter(r -> r.matches(req)).collect(Collectors.toList());

        if(ClassUtils.isEmpty(allowList)){
            UserInfo userInfo = (UserInfo)req.getSession().getAttribute("userInfo");
            if(userInfo == null){
                response.setStateCode(BaseResponse.NOTLOGIN).setMsg("用户未登陆或session已经过期");
            }else{
                //超级管理员
                if(superUser.contains(userInfo.getLoginname().toLowerCase())){
                    flag = true;
                    log.info("超级管理员放行");
                }else{
                    //获取权限列表
                    UserAuthSet[] authList = userInfo.getAuthList();
                    //校验权限
                    List<UserAuthSet> collect = Arrays.stream(authList)
                            .filter(auth -> auth.getUrl().equalsIgnoreCase(req.getServletPath()))
                            .collect(Collectors.toList());
                    flag = !ClassUtils.isEmpty(collect);
                }
            }
        }else{
            flag = true;
        }

        if(flag){
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            PrintWriter writer = resp.getWriter();
            writer.print(response.toJson());
            writer.flush();
            writer.close();
        }
    }
}
