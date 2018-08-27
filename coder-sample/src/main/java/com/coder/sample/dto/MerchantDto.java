package com.coder.sample.dto;

import org.springframework.beans.BeanUtils;
import com.coder.sample.model.Merchant;

/**
 * @author wangsy
 * @date 2018/08/27
 */
public class MerchantDto {

    public Merchant transfer(MerchantDto dto) {

        Merchant model = new Merchant();
        BeanUtils.copyProperties(dto, model);

        return model;
    }
}
