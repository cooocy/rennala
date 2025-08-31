package er.rennala.request.filters.ctx;

import java.util.Optional;

/**
 * 定义如何根据 Token 组装 Profile.
 */
public interface ProfileAssembler<P extends AbstractProfile, T extends Token> {

    Optional<P> assemble(T token);

}
