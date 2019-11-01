package com.ch.fastdfsapi.dao;

import com.ch.fastdfsapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户测试
 *
 * @author ch
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名和密码查询
     *
     * @param password 密码
     * @param userName 用户名
     * @return {@link User}对象
     */
    Optional<User> findByUsernameAndPassword(String userName, String password);


    /**
     * 根据 token 查询
     *
     * @param token token
     * @return {@link User}对象
     */
    Optional<User> findByToken(String token);
}
