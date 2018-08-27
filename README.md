# wang-smile-common
> `code-generator`: 代码生成工具
> 
> `smile-common`: 常用工具包
>
> `corder-sample`: 代码生成工具案例

------

### 代码生成工具的使用方法：
##### 1, 将`wang-smile-common`下载到本地，通过`Maven`进行`install`操作(将项目打包并将jar包安装到本地仓库)
##### 2, 项目的`pom.xml`文件中引入相关`jar`包, 添加以下依赖：
```xml
<!--工具包-->
<dependencise>
    <dependency>
        <artifactId>smile-common</artifactId>
        <groupId>wang.smile.common</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <!--代码生成包-->
    <dependency>
        <artifactId>code-generator</artifactId>
        <groupId>wang.smile.common</groupId>
        <version>0.0.1-SNAPSHOT</version>
        <scope>test</scope>
    </dependency>
</dependencise>
```

##### 3, 代码摸板以及相关数据库配置都放在`test`目录中进行，具体代码及配置参考`corder-sample`项目中的使用方法

![案例](https://github.com/smilewangsy/wang-smile-common/blob/master/20180805201915.png)

##### 4, 生成相应的`controller`, `service`, `dao`, `mapper`, `vo`, `valid`代码，并生成单表的`曾,删,改,查`代码

![案例](https://github.com/smilewangsy/wang-smile-common/blob/master/20180827100614.png)


##### 5, `controller`示例代码
```java
package com.coder.sample.controller;

import com.coder.sample.model.Merchant;
import com.coder.sample.dto.MerchantDto;
import com.coder.sample.vo.MerchantVo;
import com.coder.sample.valid.MerchantValid;
import com.coder.sample.service.MerchantService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import wang.smile.common.base.BaseConstants;
import wang.smile.common.base.BaseResult;

import java.util.List;

/**
 * @author wangsy
 * @date 2018/08/27
 */
@RestController
@RequestMapping("/v1/merchant")
@Api(value = "xx", description = "xx")
public class MerchantController {

    @Autowired
    private MerchantService services;

    @PostMapping
    @ApiOperation(value = "新增", httpMethod = "POST", response = MerchantController.class, notes = "新增")
    public BaseResult createModel(MerchantDto dto) {
        try {
            services.insertDto(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResult(BaseConstants.FAILED_CODE, BaseConstants.FAILED_MSG, "新增数据异常");
        }
        return new BaseResult(BaseConstants.SUCCESS_CODE, BaseConstants.SUCCESS_MSG, "SUCCESS");
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", httpMethod = "DELETE", response = MerchantController.class, notes = "删除")
    public BaseResult deleteById(@PathVariable Long id) {
        if(id == null || id <= 0) {
            return new BaseResult(BaseConstants.FAILED_CODE, BaseConstants.FAILED_MSG, "请求参数错误");
        }
        try {
            services.deleteByUpdate(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResult(BaseConstants.FAILED_CODE, BaseConstants.FAILED_MSG, "删除数据异常");
        }
        return new BaseResult(BaseConstants.SUCCESS_CODE, BaseConstants.SUCCESS_MSG, "SUCCESS");
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询", httpMethod = "GET", response = MerchantController.class, notes = "根据id查询")
    public BaseResult getById(@PathVariable Long id) {
        if(id == null || id <= 0) {
            return new BaseResult(BaseConstants.FAILED_CODE, BaseConstants.FAILED_MSG, "请求参数错误");
        }
        Merchant model = services.selectById(id);

        MerchantVo modelVo = new MerchantVo().transModelToVo(model);

        return new BaseResult(BaseConstants.SUCCESS_CODE, BaseConstants.SUCCESS_MSG, modelVo);
    }

    /**
     * 根据条件查询
     * @param valid
     * @return
     */
    @GetMapping
    @ApiOperation(value = "根据condition查询", httpMethod = "GET", response = MerchantController.class, notes = "根据条件查询")
    public BaseResult getById(MerchantValid valid) {
        if(null == valid) {
            return new BaseResult(BaseConstants.FAILED_CODE, BaseConstants.FAILED_MSG, "请求参数错误");
        }
        List<Merchant> list = services.selectByConditions(valid);


        return new BaseResult(BaseConstants.SUCCESS_CODE, BaseConstants.SUCCESS_MSG, list);
    }
}

```

##### `service`示例代码
```java
package com.coder.sample.service.impl;

import com.coder.sample.mapper.MerchantMapper;
import com.coder.sample.model.Merchant;
import com.coder.sample.service.MerchantService;
import com.coder.sample.dto.MerchantDto;
import com.coder.sample.valid.MerchantValid;

import wang.smile.common.base.BaseService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author wangsy
 * @date 2018/08/27.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class MerchantServiceImpl extends BaseService<Merchant> implements MerchantService {

    @Resource
    private MerchantMapper merchantMapper;

    @Override
    public void insertDto(MerchantDto dto) {
        Merchant model = new MerchantDto().transfer(dto);

        model.setBeenDeleted(false);
        model.setInsertTime(new Date());

        merchantMapper.insert(model);
    }

    @Override
    public Merchant selectById(Object id) {
        Merchant model = merchantMapper.selectByPrimaryKey(id);
        /**
         * 判断model的beenDelete是否为true, 如果为true表示数据已删除
         */
        if(model!=null && model.getBeenDeleted()) {
            return null;
        }
        return model;
    }

    @Override
    public List<Merchant> selectByConditions(MerchantValid valid) {

        Condition condition = new Condition(Merchant.class);
        Example.Criteria criteria = condition.createCriteria();
        /**
         * 查询未被删除的数据
         */
        criteria.andEqualTo("beenDeleted", false);

        return merchantMapper.selectByCondition(criteria);
    }

    @Override
    public void deleteByUpdate(Object id) {
        Merchant model = merchantMapper.selectByPrimaryKey(id);
        /**
         * 非物理删除(设置beenDeleted为true表示数据被删除)
         */
        model.setBeenDeleted(true);
        model.setDeleteTime(new Date());
        merchantMapper.updateByPrimaryKeySelective(model);
    }
}
```