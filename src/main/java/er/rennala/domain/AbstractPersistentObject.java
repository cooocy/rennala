package er.rennala.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * <p> 持久层实体抽象.
 */
@ToString
@Setter
@Getter
public abstract class AbstractPersistentObject {

    /**
     * <p> ID
     */
    @TableId

    /**
     * <p> 创建时间, 不要对业务展示
     */ private Instant createdTime;

    /**
     * <p> 更新时间, 不要对业务展示
     */
    private Instant updatedTime;

    /**
     * <p> 逻辑删除
     * 重要：不要在自定义实体中定义该字段，会导致条件失效
     */
    @TableLogic(value = "0", delval = "1")
    private Boolean deleted = false;

    /**
     * <p> 创建时调用
     */
    public void whenCreated() {
        createdTime = Instant.now();
        updatedTime = Instant.now();
    }

    /**
     * <p> 更新时调用
     */
    public void whenUpdated() {
        updatedTime = Instant.now();
    }

}
