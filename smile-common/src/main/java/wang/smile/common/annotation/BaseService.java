package wang.smile.common.annotation;

import java.lang.annotation.*;

/**
 * 初始化继承BaseService的service
 * @Author wangsy
 * @Date 2018-05-25
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BaseService {
}
