package wang.smile.common.base;

import lombok.Data;

/**
 * 统一返回结果类
 * @author wangsy
 * @date 2018-5-26
 */
@Data
public class BaseResult {

    /**
     * 状态码：1成功，其他为失败
     */
    public int code;

    /**
     * 成功为success，其他为失败原因
     */
    public String message;

    /**
     * 数据结果集
     */
    public Object data;

    /**
     * 列表数据
     */
    public Object rows;

    public Integer total;

    public BaseResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResult(int code, String message, Object rows, Integer total) {
        this.code = code;
        this.message = message;
        this.rows = rows;
        this.total = total;
    }
}
