# mysql-jooq-test

use [vertx-jooq](https://github.com/jklingsporn/vertx-jooq/tree/master/vertx-jooq-rx-async) to generate CRUD template code

## Features
- mysql-async
- Fluent
- RxJava2
## Usage

1. execute `src/main/resources/sys_user.sql`
2. modify mysql configuration in pom.xml
3. cd jooq-test directory
4. delete `src/main/generated` and then run `mvn jooq-codegen:generate`
5. run `DbTestVerticle.main()`