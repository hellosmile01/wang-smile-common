package wang.smile.common.base;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * @author wangsy
 * @Date 2018/8/13.
 */
public abstract class BaseService<T> implements Service<T> {
    @Autowired
    protected Mapper<T> mapper;

    @Override
    public long count(T model){
        return mapper.selectCount(model);
    }

    @Override
    public long countByCondition(Condition condition){
        return mapper.selectCountByCondition(condition);
    }

    @Override
    public void insert(T model) {
        mapper.insert(model);
    }

    @Override
    public void insertSelective(T model) {
        mapper.insertSelective(model);
    }

    @Override
    public void insertList(List<T> models) {
        mapper.insertList(models);
    }

    @Override
    public void deleteByPrimaryKey(Object id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByIds(String ids) {
        mapper.deleteByIds(ids);
    }

    @Override
    public void deleteByCondition(Condition condition){
        mapper.deleteByCondition(condition);
    }

    @Override
    public void updateByCondition(T model, Condition condition){
        mapper.updateByCondition(model,condition);
    }

    @Override
    public void updateByConditionSelective(T model, Condition condition){
        mapper.updateByConditionSelective(model,condition);
    }

    @Override
    public void updateByPrimaryKey(T model){
        mapper.updateByPrimaryKey(model);
    }

    @Override
    public void updateByPrimaryKeySelective(T model) {
        mapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public T selectByPrimaryKey(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public T selectOne(T model){
        return mapper.selectOne(model);
    }

    @Override
    public List<T> selectAll(){
        return mapper.selectAll();
    }

    @Override
    public T selectFirst(T model){
        List<T> result = mapper.select(model);
        if (null != result && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public T selectFirstByCondition(Condition condition) {
        List<T> result = mapper.selectByCondition(condition);
        if (null != result && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public List<T> select(T model){
        return mapper.select(model);
    }

    @Override
    public List<T> selectForStartPage(T model, Integer pageNum, Integer pageSize){
        PageHelper.offsetPage(pageNum, pageSize, false);
        return mapper.select(model);
    }

    @Override
    public List<T> selectByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    @Override
    public List<T> selectByCondition(Condition condition) {
        return mapper.selectByCondition(condition);
    }

    @Override
    public List<T> selectByConditionForStartPage(Condition condition, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, false);
        return mapper.selectByCondition(condition);
    }
}
