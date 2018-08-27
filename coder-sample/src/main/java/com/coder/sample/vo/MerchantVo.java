package com.coder.sample.vo;

import org.springframework.beans.BeanUtils;
import com.coder.sample.model.Merchant;

import java.util.List;
import java.io.Serializable;
import java.util.stream.Collectors;

import lombok.Data;

/**
 * @author wangsy
 * @date 2018/08/27.
 */
@Data
public class MerchantVo implements Serializable {

    public static MerchantVo transModelToVo(Merchant model) {
        MerchantVo vo = new MerchantVo();
        BeanUtils.copyProperties(model, vo);
        return vo;
    }

    /**
     * 将ModelList转换为ModelVoList
     * @param modelList
     * @return
     */
    public static List<MerchantVo> transModelListToVoList(List<Merchant> modelList) {
        List<MerchantVo> voList = modelList.stream().map(e -> transModelToVo(e)).collect(Collectors.toList());
        return voList;
    }
}
