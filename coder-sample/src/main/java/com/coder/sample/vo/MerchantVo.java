package com.coder.sample.vo;

import org.springframework.beans.BeanUtils;
import com.coder.sample.model.Merchant;

/**
 * @author wangsy
 * @date 2018/08/27.
 */
public class MerchantVo {

    public MerchantVo transModelToVo(Merchant model) {
        BeanUtils.copyProperties(model, this);
        return this;
    }
}
