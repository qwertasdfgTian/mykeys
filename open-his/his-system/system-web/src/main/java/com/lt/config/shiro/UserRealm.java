package com.lt.config.shiro;

import com.lt.domain.User;
import com.lt.service.UserService;
import com.lt.vo.ActiverUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy //由于shiro的容器和spring的冲突
    private UserService userService;
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    //做认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //得到用户登陆名
        String phone=token.getPrincipal().toString();
        //根据电话查询用户是否存在
        User user = userService.queryUserByPhone(phone);
        if(null!=user){//说明用户存在，但是密码可能不正确
            //组装存放到reids里面的对象
            ActiverUser activerUser=new ActiverUser();
            activerUser.setUser(user);
            //匹配密码
            SimpleAuthenticationInfo  info=new SimpleAuthenticationInfo(
                    activerUser,user.getPassword(), ByteSource.Util.bytes(user.getSalt()),this.getName()
            );
            return info;
        }else{
            return null;//代表用户不存在
        }
    }

    //做授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        ActiverUser activerUser= (ActiverUser) principalCollection.getPrimaryPrincipal();//身份得到的就是上一个方法的返回值的值 第一个参数activerUser
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        return info;
    }

}
