package com.lt.controller.system;

import cn.hutool.core.date.DateUtil;
import com.lt.constants.Constants;
import com.lt.constants.HttpStatus;
import com.lt.domain.LoginInfo;
import com.lt.domain.Menu;
import com.lt.domain.SimpleUser;
import com.lt.dto.LoginBodyDto;
import com.lt.service.LoginInfoService;
import com.lt.service.MenuService;
import com.lt.utils.AddressUtils;
import com.lt.utils.IpUtils;
import com.lt.utils.ShiroSecurityUtils;
import com.lt.vo.ActiverUser;
import com.lt.vo.AjaxResult;
import com.lt.vo.MenuTreeVo;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
public class LoginController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private LoginInfoService loginInfoService;

    /**
     * 登录方法
     *
     * @return 结果
     */
    @PostMapping("login/doLogin")
    //@Validated注解是来配合springboot校验来使用的
    public AjaxResult login(@RequestBody @Validated LoginBodyDto loginBodyDto, HttpServletRequest request) {
        AjaxResult ajax = AjaxResult.success();
        String username = loginBodyDto.getUsername();
        String password = loginBodyDto.getPassword();
        //构造用户名和密码的token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登陆信息
        LoginInfo loginInfo = createLoginInfo(request);
        loginInfo.setLoginAccount(loginBodyDto.getUsername());
        try {
            subject.login(token);
            //得到会话的token==也就是redis里面存的
            Serializable webToken = subject.getSession().getId();
            ajax.put(Constants.TOKEN,webToken);
            loginInfo.setLoginStatus(Constants.LOGIN_SUCCESS);
            loginInfo.setUserName(ShiroSecurityUtils.getCurrentUserName());
            loginInfo.setMsg("登陆成功");
        } catch (Exception e) {
            log.error("用户名或密码不正确", e);
            ajax = AjaxResult.error(HttpStatus.ERROR, "用户名或密码不正确");
            loginInfo.setLoginStatus(Constants.LOGIN_ERROR);
            loginInfo.setMsg("用户名或密码不正确");
        }
        loginInfoService.insertLoginInfo(loginInfo);
        return ajax;
    }

    /**
     * 得到用户的登陆信息
     * @param request
     * @return
     */
    private LoginInfo createLoginInfo(HttpServletRequest request) {
        LoginInfo loginInfo=new LoginInfo();
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr(request);
        String address = AddressUtils.getRealAddressByIP(ip);
        loginInfo.setIpAddr(ip);
        loginInfo.setLoginLocation(address);
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        loginInfo.setOs(os);
        loginInfo.setBrowser(browser);
        loginInfo.setLoginTime(DateUtil.date());
        loginInfo.setLoginType(Constants.LOGIN_TYPE_SYSTEM);
        return loginInfo;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("login/getInfo")
    public AjaxResult getInfo()
    {
        Subject subject = SecurityUtils.getSubject();
        ActiverUser activerUser= (ActiverUser) subject.getPrincipal();
        AjaxResult ajax = AjaxResult.success();
        ajax.put("username", activerUser.getUser().getUserName());
        ajax.put("picture", activerUser.getUser().getPicture());
        ajax.put("roles", activerUser.getRoles());
        ajax.put("permissions", activerUser.getPermissions());
        return ajax;
    }

    /**
     * 用户退出
     */
    @PostMapping("login/logout")
    public AjaxResult logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return AjaxResult.success("用户退出成功");
    }

    /**
     * 获取应该显示的菜单信息
     *
     * @return 菜单信息
     */
    @GetMapping("login/getMenus")
    public AjaxResult getMeuns()
    {
        Subject subject = SecurityUtils.getSubject();
        ActiverUser activerUser= (ActiverUser) subject.getPrincipal();
        //比较是否是管理员
        boolean isAdmin=activerUser.getUser().getUserType().equals(Constants.USER_ADMIN);
        SimpleUser simpleUser=null;
        //不是管理员的情况给simpleUser赋值
        if(!isAdmin){
            simpleUser=new SimpleUser(activerUser.getUser().getUserId(),activerUser.getUser().getUserName());
        }
        List<Menu> menus = menuService.selectMenuTree(isAdmin,simpleUser);
        List<MenuTreeVo> menuVos=new ArrayList<>();
        for (Menu menu : menus) {
            menuVos.add(new MenuTreeVo(menu.getMenuId().toString(),menu.getPath()));
        }
        return AjaxResult.success(menuVos);
    }
}
