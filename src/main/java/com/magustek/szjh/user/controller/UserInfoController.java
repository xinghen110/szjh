package com.magustek.szjh.user.controller;

import com.alibaba.fastjson.JSON;
import com.magustek.szjh.config.InitConfigData;
import com.magustek.szjh.user.bean.CompanyModel;
import com.magustek.szjh.user.bean.UserInfo;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 用户信息
 * */
@SuppressWarnings("unchecked")
@Api("用户信息")
@Slf4j
@RestController
@RequestMapping(value = "/UserInfo", method = RequestMethod.POST, produces = ClassUtils.HTTP_HEADER)
public class UserInfoController {
    private BaseResponse resp;

    private InitConfigData initConfigData;

    public UserInfoController( InitConfigData initConfigData) {
        this.initConfigData = initConfigData;
        resp = new BaseResponse();
        log.info("初始化 UserInfoController");
    }

    @ApiOperation(value="获取用户公司列表。")
    @RequestMapping("/getCompanyList")
    public String getCompanyList(HttpSession httpSession) {
        List<CompanyModel> companyList = (List<CompanyModel>)httpSession.getAttribute("CompanyList");
        log.warn("用户公司列表：{}", JSON.toJSONString(companyList));
        return resp.setStateCode(BaseResponse.SUCCESS).setData(companyList).setMsg("成功！").toJson();
    }

    //切换公司

    @RequestMapping(value = "/switchCompany")
    public String switchOrg(HttpSession httpSession, @RequestBody CompanyModel orgCode) throws Exception {
        log.info("切换公司...");
        UserInfo user = (UserInfo) httpSession.getAttribute("userInfo");

        //从缓存中获取用户的公司信息，并将组织机构编号、部门编号、职位编号设置到用户数据模型中
        List<CompanyModel> companyModelList = (List<CompanyModel>) httpSession.getAttribute("CompanyList");
        if (companyModelList == null || companyModelList.size() < 1) {
            throw new Exception("选择用户组织机构失败");
        }
        for (CompanyModel companyModel : companyModelList) {
            if (orgCode.getOrgcode().equals(companyModel.getOrgcode())) {
                //将客户当前选择的公司放入会话
                user.setCompanyModel(companyModel);
            }
        }
        //更新session
        httpSession.setAttribute("userInfo", user);
        //获得用户权限
        //List<AuthModel> userAuthList = oDataAuthService.getUserAuth(user);
        //session.setAttribute("authList", userAuthList);

        return resp.setStateCode(BaseResponse.SUCCESS).setData(user).setMsg("选择公司成功！").toJson();
    }
}
