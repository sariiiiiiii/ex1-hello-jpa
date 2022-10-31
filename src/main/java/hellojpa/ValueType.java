package hellojpa;

public class ValueType {
    public static void main(String[] args) {

        /**
         * 값 타입 비교
         * 1. 동일성(identity) 비교 : 인스턴스의 참조 값을 비교, == 사용
         * 2. 동등성(equivalence) 비교 : 인스턴스의 값을 비교, equals() 사용
         * 3. 값 타입은 a.equals(b)를 사용해서 동등성 비교를 해야 함
         * 4. 값 타입의 equals() 메소드를 적절하게 재정의(주로 모든 필드 사용)
         */

        int a = 10;
        int b = 10;
        System.out.println(a == b); // true

        Address addressA = new Address("city");
        Address addressB = new Address("city");
        System.out.println("addressA == addressB : " + (addressA == addressB)); // 주소값을 비교하는것이기 때문에 false

        // 지금은 false가 나온다 equals()의 기본은 == 비교이기 떄문
        // 그럼 equals()를 어떻게 사용해야 할까?, Address domain에 가서 override를 해줘야 함
        System.out.println("addressA equals addressB : " + (addressA.equals(addressB))); 

    }
}
