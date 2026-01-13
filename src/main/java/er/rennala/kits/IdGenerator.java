package er.rennala.kits;

import cn.hutool.core.util.IdUtil;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.annotation.Nonnull;

import java.util.Random;

/**
 * <p> ID 生成器.
 */
public class IdGenerator {

    /**
     * <p> 获取一个雪花算法生成的 ID.
     *
     * @return ID
     */
    @Nonnull
    public static Long snowflakeId() {
        return IdUtil.getSnowflake().nextId();
    }

    /**
     * <p> 获取一个 UUID, 无横线, 小写.
     *
     * @return UUID
     */
    @Nonnull
    public static String lowerCaseUUID() {
        return IdUtil.fastSimpleUUID().toLowerCase();
    }

    /**
     * <p> 获取一个 UUID, 无横线, 大写.
     *
     * @return UUID
     */
    @Nonnull
    public static String upperCaseUUID() {
        return IdUtil.fastSimpleUUID().toUpperCase();
    }

    /**
     * <p> 生成一个指定长度的 NanoId, 由大写字母和数字组成.
     *
     * @param length 长度, > 0
     * @return NanoId
     */
    @Nonnull
    public static String nanoId(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length must be greater than zero.");
        }
        return NanoIdUtils.randomNanoId(new Random(), "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray(), length);
    }

}


