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
        return model;
    }

    @Override
    public List<Merchant> selectByConditions(MerchantValid valid) {

        Condition condition = new Condition(Merchant.class);
        Example.Criteria criteria = condition.createCriteria();

        return merchantMapper.selectByCondition(criteria);
    }

    @Override
    public void deleteByUpdate(Object id) {
        Merchant model = merchantMapper.selectByPrimaryKey(id);
        model.setBeenDeleted(true);
        model.setDeleteTime(new Date());
        merchantMapper.updateByPrimaryKeySelective(model);
    }

}
