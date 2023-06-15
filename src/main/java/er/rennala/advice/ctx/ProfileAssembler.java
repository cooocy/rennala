package er.rennala.advice.ctx;

import jakarta.annotation.Nonnull;

import java.util.Optional;

/**
 * 定义如何根据 Token 组装 Profile.
 */
public interface ProfileAssembler<P extends AbstractProfile, T extends Token> {

    @Nonnull
    Optional<P> assemble(@Nonnull T token);

}
