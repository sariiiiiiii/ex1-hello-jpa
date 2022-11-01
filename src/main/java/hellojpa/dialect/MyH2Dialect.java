package hellojpa.dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class MyH2Dialect extends H2Dialect {
// 내가 사용하고 있는 DB를 상속받는다

    public MyH2Dialect() {
        /**
         * 기본 생성자에서 resisterFunction으로 사용자정의 함수를 등록해준다
         * name(이름)을 등록해주고 함수는 소스코드를 보면서 등록을 해줘야한다 (왠만하면 되어 있는것을 사용해보자...)
         */
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }
}
