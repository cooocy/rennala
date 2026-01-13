package er.rennala.query.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 分页查询结果.
 */
@Getter
@ToString
public class Pager<R> {

    public final List<R> records;

    /**
     * 第几页
     */
    public final int page;

    /**
     * 每页几条
     */
    public final int limit;

    /**
     * 总条数
     */
    public final int totalCount;

    /**
     * 总页数
     */
    public final int totalPage;

    /**
     * <p> 根据分页条件创建一个空数据的 Pager 对象.
     *
     * @param pageArgs [nonnull] 分页查询参数
     * @param <R>      记录类型
     * @return [nonnull] 空数据的 Pager 对象
     */
    public static <R> Pager<R> newEmptyPager(PageQueryArgs pageArgs) {
        return new Pager<>(new ArrayList<>(), pageArgs.getPage(), pageArgs.getLimit(), 0, 0);
    }

    /**
     * 将 Mybatis-Plus 的 IPage 对象转换成 Pager 对象, 用于返回.
     *
     * @param page    [nonnull] Mybatis-Plus 的 IPage 对象
     * @param records [nonnull, empty-able] 当前页的记录列表
     * @return [nonnull] Pager 对象
     */
    public static <N, R> Pager<N> newPager(IPage<R> page, List<N> records) {
        return new Pager<>(records, (int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal(), (int) page.getPages());
    }

    /**
     * 使用 originalPager 的分页字段和 newRecords 组装成一个新的 pager.
     *
     * @param originalPager [nonnull] 原分页
     * @param newRecords    [nonnull, empty-able] 新 records
     * @return [nonnull] 新的 pager
     */
    public static <N, R> Pager<N> newPager(Pager<R> originalPager, List<N> newRecords) {
        return new Pager<>(newRecords, originalPager.page, originalPager.limit, originalPager.totalCount, originalPager.totalPage);
    }

    /**
     * 使用指定的分页字段和 newRecords 组装成一个新的 pager.
     *
     * @param page       第几页
     * @param limit      每页几条
     * @param totalCount 总条数
     * @param totalPage  总页数
     * @param newRecords [nonnull] 当前页记录
     * @param <R>        记录类型
     * @return [nonnull] 新的 pager
     */
    public static <R> Pager<R> newPager(Long page, Long limit, Long totalCount, Long totalPage, List<R> newRecords) {
        return new Pager<>(newRecords, page.intValue(), limit.intValue(), totalCount.intValue(), totalPage.intValue());
    }

    public static <R> Pager<R> newPager(Integer page, Integer limit, Integer totalCount, Integer totalPage, List<R> newRecords) {
        return new Pager<>(newRecords, page, limit, totalCount, totalPage);
    }

    private Pager(List<R> records, int page, int limit, int totalCount, int totalPage) {
        this.records = records;
        this.page = page;
        this.limit = limit;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
    }

}
