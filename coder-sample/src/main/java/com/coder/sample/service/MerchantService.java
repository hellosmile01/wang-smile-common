package com.coder.sample.service;

import com.coder.sample.model.Merchant;
import com.coder.sample.dto.MerchantDto;
import com.coder.sample.valid.MerchantValid;
import wang.smile.common.base.Service;

import java.util.List;

/**
 * @author wangsy
 * @date 2018/08/27
 */
public interface MerchantService extends Service<Merchant> {

   /**
     * 插入数据
     * @param dto
     */
    void insertByDto(MerchantDto dto);

    /**
     * 修改数据
     * @param dto
     */
    void updateByDto(MerchantDto dto)  throws Exception;

    /**
     * 非物理删除
     * @param id
     */
    void deleteByUpdate(Object id);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Merchant selectById(Object id);

    /**
     * 根据条件查询
     * @param valid
     * @return
     */
    List<Merchant> selectByConditions(MerchantValid valid);
}
