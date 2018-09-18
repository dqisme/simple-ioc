package ioccontainer;

public interface IoCContext {
    <T> void registerBean(Class<T> beanClazz);

    <T> T getBean(Class<T> resolveClazz) throws IllegalAccessException, InstantiationException;

    <T> void registerBean(Class<? super T> resolveClazz, Class<T> beanClazz);
}
