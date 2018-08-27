package com.coder.sample.dto;

import org.springframework.beans.BeanUtils;
import com.coder.sample.model.Merchant;

import java.io.Serializable;

import lombok.Data;

/**
 * @author wangsy
 * @date 2018/08/27
 */
@Data
public class MerchantDto implements Serializable {

    public Merchant transfer(MerchantDto dto) {

        Merchant model = new Merchant();
        BeanUtils.copyProperties(dto, model);

        return model;
    }
}
