package er.rennala.query.page;

import er.rennala.query.Order;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 排序规则
 */
@Setter
@Getter
@ToString
public class OrderRule {

    /**
     * 排序字段名 (db table column)
     */
    private String field;

    /**
     * 顺序, ASC or DESC
     */
    private Order order;

    public OrderRule() {
    }

    public OrderRule(String field, Order order) {
        this.field = field;
        this.order = order;
    }

    public OrderItem toMybatisPlusOrderItem() {
        if (order == Order.ASC) {
            return OrderItem.asc(field);
        } else {
            return OrderItem.desc(field);
        }
    }

}
