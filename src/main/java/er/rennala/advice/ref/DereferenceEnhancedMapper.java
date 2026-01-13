package er.rennala.advice.ref;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 增强版 BaseMapper，所有查询方法都会被 @Dereference 增强
 *
 * @param <T> PO 类型
 */
@Dereference
public interface DereferenceEnhancedMapper<T> extends BaseMapper<T> {

    @Override
    T selectById(Serializable id);

    default Optional<T> selectOptionalById(Serializable id) {
        return Optional.ofNullable(selectById(id));
    }

    @Override
    List<T> selectByIds(@Param(Constants.COLL) Collection<? extends Serializable> idList);

    @Override
    default List<T> selectBatchIds(@Param(Constants.COLL) Collection<? extends Serializable> idList) {
        return BaseMapper.super.selectBatchIds(idList);
    }

    @Override
    void selectByIds(@Param(Constants.COLL) Collection<? extends Serializable> idList,
                     ResultHandler<T> resultHandler);

    @Override
    default void selectBatchIds(@Param(Constants.COLL) Collection<? extends Serializable> idList,
                                ResultHandler<T> resultHandler) {
        BaseMapper.super.selectBatchIds(idList, resultHandler);
    }

    @Override
    default List<T> selectByMap(Map<String, Object> columnMap) {
        return BaseMapper.super.selectByMap(columnMap);
    }

    @Override
    default void selectByMap(Map<String, Object> columnMap,
                             ResultHandler<T> resultHandler) {
        BaseMapper.super.selectByMap(columnMap, resultHandler);
    }

    @Override
    default T selectOne(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper) {
        return BaseMapper.super.selectOne(queryWrapper);
    }

    @Override
    default T selectOne(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper,
                        boolean throwEx) {
        return BaseMapper.super.selectOne(queryWrapper, throwEx);
    }

    @Override
    List<T> selectList(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    @Override
    void selectList(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper,
                    ResultHandler<T> resultHandler);

    @Override
    List<T> selectList(IPage<T> page,
                       @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    @Override
    void selectList(IPage<T> page,
                    @Param(Constants.WRAPPER) Wrapper<T> queryWrapper,
                    ResultHandler<T> resultHandler);

    @Override
    List<Map<String, Object>> selectMaps(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    @Override
    void selectMaps(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper,
                    ResultHandler<Map<String, Object>> resultHandler);

    @Override
    List<Map<String, Object>> selectMaps(IPage<? extends Map<String, Object>> page,
                                         @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    @Override
    void selectMaps(IPage<? extends Map<String, Object>> page,
                    @Param(Constants.WRAPPER) Wrapper<T> queryWrapper,
                    ResultHandler<Map<String, Object>> resultHandler);

    @Override
    <E> List<E> selectObjs(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    @Override
    <E> void selectObjs(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper,
                        ResultHandler<E> resultHandler);

    @Override
    default <P extends IPage<T>> P selectPage(P page,
                                              @Param(Constants.WRAPPER) Wrapper<T> queryWrapper) {
        return BaseMapper.super.selectPage(page, queryWrapper);
    }

    @Override
    default <P extends IPage<Map<String, Object>>> P selectMapsPage(P page,
                                                                    @Param(Constants.WRAPPER) Wrapper<T> queryWrapper) {
        return BaseMapper.super.selectMapsPage(page, queryWrapper);
    }

}
