package com.magustek.szjh.user.bean;

import com.magustek.szjh.utils.ClassUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户权限
 * */
@Getter
@Setter
@NoArgsConstructor
public class UserAuthSet implements Serializable {
    private String Loginname;           //登录用户名
    private String Username;            //真实用户名
    private String Usersource;          //用户来源
    private String OrgCode;             //组织机构编号
    private String DeptCode;            //部门编号
    private String ActorCode;           //所属岗位
    private String Id;                  //权限对象ID
    private String Pid;                 //权限对象ID
    private String Name;                //权限名称
    private String Type;                //权限类型
    private String Url;                 //url

    public static UserAuthSet[] paresMap(List<Map<String, Object>> result){
        if(ClassUtils.isEmpty(result)){
            return null;
        }
        List<UserAuthSet> list = new ArrayList<>(result.size());

        result.forEach(map->{
            UserAuthSet auth;
            if( map!=null){
                auth = new UserAuthSet();
                auth.setLoginname((String) map.get("Loginname"));
                auth.setUsername((String) map.get("Username"));

                auth.setUsersource((String) map.get("Usersource"));
                auth.setOrgCode((String) map.get("OrgCode"));
                auth.setDeptCode((String) map.get("DeptCode"));
                auth.setActorCode((String) map.get("ActorCode"));
                auth.setId((String) map.get("Id"));
                auth.setPid((String) map.get("Pid"));
                auth.setName((String) map.get("Name"));
                auth.setType((String) map.get("Type"));
                auth.setUrl((String) map.get("Url"));

                list.add(auth);
            }
        });
        return list.toArray(new UserAuthSet[list.size()]);
    }
}
