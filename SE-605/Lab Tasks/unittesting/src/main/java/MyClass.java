public class MyClass {
    public float div(float a, float b) {
        if (b==0){
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }
}
