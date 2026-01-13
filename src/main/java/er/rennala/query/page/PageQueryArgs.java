package er.rennala.query.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p> 分页查询参数.
 */
@Setter
@Getter
@ToString
public class PageQueryArgs {

    /**
     * 第几页
     */
    private Integer page;

    /**
     * 每页几条
     */
    private Integer limit;

    /**
     * 排序规则
     */
    private List<OrderRule> orderRules = new ArrayList<>();

    /**
     * <p> 将当前分页查询条件转换成 Mybatis-Plus 的 Page 对象, 用于查询.
     * <p> 会对 page 和 limit 的值进行合理化处理: page[1, INT_MAX], limit[1, 500], 默认 page = 1, limit = 20.
     *
     * @return [nonnull] Page 对象
     */
    public Page toMybatisPlusPage() {
        Page<Object> page = new Page<>(purePage(), pureLimit());
        page.setOrders(orderRules.stream().map(OrderRule::toMybatisPlusOrderItem).collect(Collectors.toList()));
        return page;
    }

    private Integer purePage() {
        if (Objects.isNull(page) || this.page < 1) {
            return 1;
        }
        return page;
    }

    private int pureLimit() {
        if (Objects.isNull(limit)) {
            return 20;
        }
        if (this.limit < 1) {
            return 1;
        }
        if (this.limit > 500) {
            return 500;
        }
        return limit;
    }

}
