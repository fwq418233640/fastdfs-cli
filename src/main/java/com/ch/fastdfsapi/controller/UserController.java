package com.ch.fastdfsapi.controller;

import cn.hutool.core.util.IdUtil;
import com.ch.fastdfsapi.dao.UserRepository;
import com.ch.fastdfsapi.model.entity.User;
import com.ch.fastdfsapi.utils.Msg;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * 用户
 *
 * @author ch
 */
@RestController
public class UserController {

    @Autowired
    private Environment environment;

    @Autowired
    private UserRepository userRepository;

    /**
     * 登陆
     *
     * @param user 参数
     * @return token
     */
    @ApiOperation("登陆")
    @PostMapping("/login")
    public Msg<Object> login(@RequestBody User user) {

        if (user == null || StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return Msg.fail("账号密码错误");
        }

        String username = environment.getProperty("com.ikingtech.fastdfs-client.admin.username");
        String password = environment.getProperty("com.ikingtech.fastdfs-client.admin.password");
        String token = IdUtil.randomUUID();
        if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
            Optional<User> optional = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
            User admin;
            if (optional.isPresent()) {
                admin = optional.get();
            } else {
                // 若差不多与配置文件配置的 admin 用户
                // 则数据库所有用户 重新创建
                userRepository.deleteAll();
                admin = new User();
                admin.setId(IdUtil.simpleUUID());
                admin.setMailbox(environment.getProperty("com.ikingtech.fastdfs-client.admin.emilbox"));
                admin.setUsername(username);
                admin.setPassword(password);
            }

            admin.setToken(token);
            userRepository.save(admin);
            return Msg.sucess(token);
        } else {
            return Msg.fail("账号密码错误");
        }
    }
}
