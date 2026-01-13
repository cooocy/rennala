package er.rennala.domain;

/**
 * Shadow of a domain object, can be used as a reference to the original object. e.g.
 * Post:
 * - id: 1
 * - title: "Hello World"
 * - content: "This is a test post"
 * - author: UserShadow(id=1, name="Foo")
 *
 * User:
 * - id: 1
 * - name: "Foo"
 * - password: "xxxxx"
 */
public abstract class Shadow {

}