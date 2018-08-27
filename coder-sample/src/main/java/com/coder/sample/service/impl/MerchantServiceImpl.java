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
