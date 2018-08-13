package com.coder.sample.service.impl;

import com.coder.sample.mapper.MerchantMapper;
import com.coder.sample.model.Merchant;
import com.coder.sample.service.MerchantService;
import wang.smile.common.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by wangsy on 2018/08/13.
 */
@Service
@Transactional
public class MerchantServiceImpl extends BaseService<Merchant> implements MerchantService {
    @Resource
    private MerchantMapper merchantMapper;

}
