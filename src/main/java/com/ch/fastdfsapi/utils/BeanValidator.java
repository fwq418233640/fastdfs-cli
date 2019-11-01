package com.ch.fastdfsapi.utils;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * 对象验证器
 *
 * @author com.cwag.paas.common.valid
 */
@Slf4j
public class BeanValidator {

    /**
     * 验证某个bean的参数
     *
     * @param object 被校验的参数
     * @param <T>    泛型
     * @throws NullPointerException 如果参数校验不成功则抛出此异常
     */
    public static <T> void validate(T object) {

        if (object == null) {
            throw new NullPointerException("接口入参不能为空");
        }
        //获得验证器
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        //执行验证
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        //如果有验证信息，则取出来包装成异常返回
        if (constraintViolations == null || constraintViolations.size() == 0) {
            return;
        }
        throw new NullPointerException(convertErrorMsg(constraintViolations));
    }

    /**
     * 验证某个bean的参数
     *
     * @param list 泛型集合
     * @throws NullPointerException 如果参数校验不成功则抛出此异常
     */
    public static <T> void validate(List<T> list) {

        if (list == null || list.size() == 0) {
            throw new NullPointerException("接口入参不能为空");
        }
        for (T t : list) {
            try {
                validate(t);
            } catch (NullPointerException e) {
                throw new NullPointerException(String.format("%s,异常对象:%s", e.getMessage(), t));
            }
        }
    }

    /**
     * 转换异常信息
     *
     * @param set 不可重复集合
     * @return 消息提示
     */
    private static <T> String convertErrorMsg(Set<ConstraintViolation<T>> set) {
        StringBuilder result = new StringBuilder(16);
        for (ConstraintViolation<T> cv : set) {
            //这里循环获取错误信息，可以自定义格式
            if (result.length() != 0) {
                result.append(",");
            }
            result.append(cv.getMessageTemplate());
        }
        return result.toString();
    }
}