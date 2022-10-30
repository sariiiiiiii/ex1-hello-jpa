package hellojpa;

public class ValueMain {

    public static void main(String[] args) {

        /**
         * 값 타입
         * 1. int, double 같은 기본 타입(primitive type)은 절대 공유 X
         * 2. 기본 타입은 항상 값을 복사함
         * 3. Integer같은 래퍼 클래스 나 String 같은 특수한 클래스는 공유 가능한 객체이지만 변경 X
         */

        int a = 10;
        int b = a; // 값 복사

        a = 20;

        System.out.println("a = " + a);
        System.out.println("b = " + b);

        Integer numA = Integer.valueOf(10);
        Integer numB = numA;
        
        // 값이 복제가 되는 것이 아니라 reference값만 넘어감(참조값, 주소복사)
        System.out.println("numA = " + numA);
        System.out.println("numB = " + numB);

    }
}
