package com.coder.sample.vo;

import org.springframework.beans.BeanUtils;
import com.coder.sample.model.Merchant;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangsy
 * @date 2018/08/27.
 */
public class MerchantVo {

    public MerchantVo transModelToVo(Merchant model) {
        BeanUtils.copyProperties(model, this);
        return this;
    }

    /**
     * 将ModelList转换为ModelVoList
     * @param memberList
     * @return
     */
    public List<MerchantVo> transModelListToVoList(List<Merchant> memberList) {
        List<MerchantVo> collect = memberList.stream().map(e -> this.transModelToVo(e)).collect(Collectors.toList());
        return collect;
    }
}
