/**
 * Copyright:   Copyright (c)2016
 * Company:     YvesHe
 * @version:    1.0
 * Create at:   2019年6月14日
 * Description:
 *
 * Author       YvesHe
 */
package com.yveshe.restful;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.yveshe.entity.Resource;
import com.yveshe.entity.User;
import com.yveshe.service.ResourceService;
import com.yveshe.service.UserService;

@Controller
@RequestMapping("/user")
public class UserEndpoint {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Object index() {
        User loginUser = new User();
        loginUser.setUsername("admin");
        Set<String> permissions = userService.findPermissions(loginUser.getUsername());
        List<Resource> menus = resourceService.findMenus(permissions);
        return menus;
    }

}
