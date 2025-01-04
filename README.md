A web server scaffold using SpringBoot.

## Features

- [x] HealthController
- [x] GlobalExceptionHandler
- [x] RequestLogAdvice
- [x] ContextAdvice
- [x] CheckTokenAdvice
- [x] RefProcessor

## Kit Box

#### Carian

Some useful structures of web. e.g. `MarkerPageQueryArgs`, `MarkerPager` and so on.

#### Rennala
Some useful structures of web. e.g. `AbstractProfile`, `Context` and so on.

## How to use?

#### Firstly, add dependence in `pom.xml` or `build.gradle`.

This is a gradle demo.

`build.gradle`

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.1.0'
    id 'io.spring.dependency-management' version '1.1.0'
}

sourceCompatibility = '17'
group = 'YOUR_GROUP'
version = ''

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    maven { url 'https://maven.aliyun.com/nexus/content/groups/public/' }
    maven { url "https://jitpack.io" }
    mavenCentral()
    maven { url 'https://repo.spring.io/milestone' }
    maven { url 'https://nexus.dcyy.cc/repository/the-lands-between/' }
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:2022.0.0-M5"
    }
}

dependencies {
    // springboot
    implementation 'org.springframework.boot:spring-boot-starter'
    implementation 'org.springframework.boot:spring-boot-starter-web'

    // Rennala: A Web Server Infra using SpringBoot.
    implementation 'er:rennala:1.0.3-springboot-3.1.0'

    // kits
    implementation 'er:carian:1.0.1'
    implementation 'com.google.guava:guava:31.1-jre'
    implementation 'cn.hutool:hutool-all:5.7.13'
}
```

`settings.gradle`
```groovy
// Add Plugin Management.
pluginManagement {
    repositories {
        maven { url 'https://repo.spring.io/milestone' }
        gradlePluginPortal()
    }
}
```

#### HealthController and GlobalExceptionHandler

HealthController is a ping-pong index controller.

Auto Configuration, the only thing you need to do is defining the application name in `application.yaml`.

```yaml
spring:
  application:
    name: Hello Rennala
```

Then you can call `http://localhost:8080` and health controller will give you a response.

#### RequestLogAdvice

A filter to log each http request.
You can switch on it like this:

```java
@Configuration
public class AdviceConfig {
    @Bean
    @ConditionalOnMissingBean(RequestLogAdvice.class)
    public RequestLogAdvice requestLogAdvice() {
        return new RequestLogAdvice(true);
        // or
        // return new RequestLogAdvice(false);
    }
}
```

or

```java
@ConditionalOnMissingBean(RequestLogAdvice.class)
@Import({RequestLogAdvice.class})
@Configuration
public class AdviceConfig {
}
```

#### ContextAdvice

A filter to encapsulate Request Context in Request.

You can switch on it like this:

```java
@Configuration
public class AdviceConfig {
    @Bean
    @ConditionalOnMissingBean(ContextAdvice.class)
    public ContextAdvice contextAdvice() {
        return new ContextAdvice();
    }
}
```

or

```java 
@ConditionalOnMissingBean(ContextAdvice.class)
@Import({ContextAdvice.class})
@Configuration
public class AdviceConfig {
}
```

Then you must implement `TokenPolice` and `ProfileAssembler`.

This is a demo.

```java

@Component
public class SimpleTokenPolice implements TokenPolice<SimpleToken> {

    private final TokenRepository tokenRepository;

    @Autowired
    public SimpleTokenPolice(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Optional<String> getTokenKey(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("X-Token"));
    }

    @Override
    public Optional<SimpleToken> decodeToken(String tokenKey) {
        return tokenRepository.findById(tokenKey);
    }

    @Override
    public boolean isValid(SimpleToken token) {
        return token.isValid();
    }

    @Override
    public void refresh(SimpleToken token) {
        token.refresh();
        tokenRepository.save(token);
    }

}

@Component
public class SimpleProfileAssembler implements ProfileAssembler<Profile, SimpleToken> {

    private final UserRepository userRepository;

    @Autowired
    public SimpleProfileAssembler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Profile> assemble(SimpleToken token) {
        return userRepository.findById(token.getUserId()).map(Profile::new);
    }

}
```

You can access Context using `ContextReader`, like this:

```java
@RequestMapping("/d")
@RestController
public class DemoController {

    private final HttpServletRequest request;

    @Autowired
    public DemoController(HttpServletRequest request) {
        this.request = request;
    }

    @GetMapping("/w")
    public Result<Void> w() {
        log.info("ctx: {}", ContextReader.getContext(request));
        return Result.ok();
    }

}
```

#### CheckTokenAdvice

A filter to check token in Request, supporting white-list mode and black-list mode.

You can switch on it like this:

```java
@Import({CheckTokenAdvice.class})
@Configuration
public class AdviceConfig {
}
```

or

```java
@Configuration
public class AdviceConfig {
    @Bean
    public CheckTokenAdvice checkTokenAdvice() {
        return new CheckTokenAdvice();
    }
}
```

You need to define white list or black list in `application.yaml`.

Note that only one mode can take effect.

This is a white-list demo.

```yaml
auth:
  uri:
    white:
      - /d/w1
      - /d/w2
    mode:
      white
```

This is a black-list demo.

```yaml
auth:
  uri:
    black:
      - /d/b1
      - /d/b2
    mode:
      black
```

#### RefProcessor

A processor used to parse the referenced fields within any objects, according to the `RefAnalyzer` you have configured,
query and restore the original object.

This is an example.

`Structs`

```java
import java.beans.Transient;

class Book {

    Long id;

    @Ref(analyzer = PublisherRepository.class, refField = "code", analysisTo = "publisher")
    String publisherCode;

    @Transient
    Publisher publisher;

    @Ref(analyzer = UserRPC.class, refField = "id", analysisTo = "authors")
    List<Long> authorIds;

    @Transient
    List<UserShadow> authors;

    @DeepRef
    List<Sponsor> sponsors;

}

class Publisher {
    String code;
    String name;
    String address;
}

/**
 * Only includes a part of the fields in User.
 */
class UserShadow {
    Long id;
    String name;
}

class Sponsor {
    Long id;
    String name;

    @Ref(analyzer = UserRPC.class, refField = "id", analysisTo = "boss")
    Long bossId;
    
    @Transient
    UserShadow boss;
}
```

`RefAnalyzers`

```java
import java.util.List;

class PublisherRepository implements RefAnalyzer<Publisher, Publisher, String> {

    Publisher findOne(String code) {}

    List<Publisher> findMany(List<String> codes) {}
    
    Publisher fallback(String code) {
        return new Publisher(code, "Unknown", "Unknown");
    }
    
    Publisher convert(Publisher publisher) {
        return publisher;
    }

}

class UserRPC implements RefAnalyzer<UserShadow, User, Long> {

    User findOne(Long id) {}

    List<User> findMany(List<Long> ids) {}

    User fallback(Long id) {
        return new User(id, "Unknown");
    }

    /**
     * The class User has many sensitive information, so we convert it to UserShadow.
     */
    UserShadow convert(User user) {
        return new UserShadow(user.getId(), user.getName());
    }

}
```

**How to use?**

```java
class BookRepository {
    
    @RefAnalysis
    List<Book> findMany();
    
    @RefAnalysis
    Book findOne(Long id);
    
}
```

## 😊 Who is Rennala ?

[Rennala: Queen of the Full Moon](https://eldenring.wiki.fextralife.com/Rennala+Queen+of+the+Full+Moon)