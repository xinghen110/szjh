package com.magustek.szjh.config;

import com.magustek.szjh.user.bean.CompanyModel;
import com.magustek.szjh.user.bean.UserInfo;
import com.magustek.szjh.user.service.UserInfoService;
import com.magustek.szjh.utils.base.BaseResponse;
import com.magustek.szjh.utils.ClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * spring security配置类，用于进行用户登录、注销等功能的配置
 *
 * */

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Value("${user.login.page}")
    private String LOGIN_PAGE;

    @Value("${user.login.process}")
    private String LOGIN_PROCESSOR;

    @Value("${user.logout.process}")
    private String LOGOUT_PROCESSOR;

    private UserInfoService userInfoService;

    public SecurityConfig(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public void configure(WebSecurity web){

        //注册匿名访问资源，允许用户匿名访问
        //web.ignoring().antMatchers("/b/**"); //static 文件夹内b文件夹（静态资源）所有资源均允许匿名访问
        web.ignoring().antMatchers("/static/**");   //static 文件夹内b文件夹（静态资源）所有资源均允许匿名访问
        web.ignoring().antMatchers("/manage/**");   //tomcat管理资源允许匿名访问
        web.ignoring().antMatchers("/swagger-ui.html/**");     //restapi允许匿名访问
        web.ignoring().antMatchers("/swagger-resources/**");     //restapi允许匿名访问
        web.ignoring().antMatchers("/webjars/**");     //restapi允许匿名访问
        web.ignoring().antMatchers("/v2/**");     //restapi允许匿名访问
        web.ignoring().antMatchers("/**/*.html",
                "/**/*.htm",
                "/**/*.gif",
                "/**/*.jpeg",
                "/**/*.jpg",
                "/**/*.js",
                "/**/*.css",
                "/**/*.*",
                "/**/*.font");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        //解决不允许显示在iframe的问题
        http.headers().frameOptions().disable();

        //禁用csrf校验
        http.csrf().disable();

        //所有资源均需登录方可使用
        http.authorizeRequests().anyRequest().authenticated();

        http.authorizeRequests().antMatchers("/static/**").permitAll();

        //用户登录
        http.formLogin()
            .loginPage(LOGIN_PAGE)   //全局默认login页面
            .loginProcessingUrl(LOGIN_PROCESSOR)  //login页面提交的url
            //登录成功后的处理
            .successHandler((request, response, auth) -> {
                String name = auth.getName();

                UserInfo userInfo = userInfoService.userLogin(name, "" , "O001");
                userInfo.setPassword("");

                @SuppressWarnings("unchecked")
                List<CompanyModel> companyList = (List<CompanyModel>)request.getSession().getAttribute("CompanyList");

                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setStateCode(BaseResponse.SUCCESS).setMsg("登录成功").setData(userInfo);
                if(companyList!=null && companyList.size()>1){
                    baseResponse.changeStateCode(BaseResponse.ONEMORECOMPANY);
                }
                if(companyList!=null && companyList.size()==1){
                    userInfo.setCompanyModel(companyList.get(0));
                }
                //设置session，供以后使用
                request.getSession().setAttribute("userInfo", userInfo);

                response.setContentType(ClassUtils.HTTP_HEADER);
                PrintWriter writer = response.getWriter();
                writer.write(baseResponse.toJson());
                writer.flush();
                writer.close();
            })
            //登录失败后的处理
            .failureHandler((request, response, e) -> {
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setStateCode(BaseResponse.ERROR).setMsg("登录失败:"+e.getMessage());

                response.setContentType(ClassUtils.HTTP_HEADER);
                PrintWriter writer = response.getWriter();
                writer.write(baseResponse.toJson());
                writer.flush();
                writer.close();
            }).permitAll();
        //用户注销
        http.logout()
            .logoutUrl(LOGOUT_PROCESSOR).logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(){
                @Override
                public void onLogoutSuccess(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication auth) throws IOException {
                    //移除session
                    request.getSession().removeAttribute("userInfo");

                    BaseResponse baseResponse = new BaseResponse();
                    baseResponse.setStateCode(BaseResponse.SUCCESS).setMsg("注销成功");

                    response.setContentType(ClassUtils.HTTP_HEADER);
                    PrintWriter writer = response.getWriter();
                    writer.write(baseResponse.toJson());
                    writer.flush();
                    writer.close();
                }
            }).permitAll();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        //注册用户登录服务
        auth.userDetailsService(userInfoService).passwordEncoder(new Md5PasswordEncoder());
    }

}
