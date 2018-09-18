package ioccontainer.materials;

public class BeanWithException {
    public BeanWithException() throws MyException {
        throw new MyException();
    }
}
