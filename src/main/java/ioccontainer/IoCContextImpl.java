package ioccontainer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

public class IoCContextImpl implements IoCContext, AutoCloseable {

    private Map<Class, Class> registeredClass = new HashMap<>();
    private boolean hasResolved = false;
    private Set<Object> instantiatedBeans = new LinkedHashSet<>();

    public Set<Object> getInstantiatedBeans() {
        return instantiatedBeans;
    }

    @Override
    public <T> void registerBean(Class<T> beanClazz) {
        this.registerBean(beanClazz, beanClazz);
    }

    @Override
    public <T> void registerBean(Class<? super T> resolveClazz, Class<T> beanClazz) {
        if (hasResolved) {
            throw new IllegalStateException();
        }
        checkBeanClazz(beanClazz);
        registeredClass.put(resolveClazz, beanClazz);
    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) throws IllegalAccessException, InstantiationException {
        hasResolved = true;
        Map<Class, Dependency> dependencyBeans = new LinkedHashMap<>();
        return getInjectedBean(resolveClazz, dependencyBeans);
    }

    private <T> T getInjectedBean(Class<T> resolveClazz, Map<Class, Dependency> dependencyBeans)
        throws InstantiationException, IllegalAccessException {

        checkResolvedClazz(resolveClazz);
        T beanInstance = (T) registeredClass.get(resolveClazz).newInstance();
        Class<?> beanInstanceClass = beanInstance.getClass();
        recordBeanInstance(dependencyBeans, beanInstanceClass, new Dependency(null, beanInstance));
        for (Field field : getAllFields(beanInstanceClass)) {
            injectFieldDependencies(beanInstance, field, dependencyBeans);
        }
        return beanInstance;
    }

    private List<Field> getAllFields(Class<?> beanInstanceClass) {
        List<Field> fields = new ArrayList<>();
        Class<?> currentTargetClass = beanInstanceClass;
        while (currentTargetClass != null) {
            for (Field declaredField : currentTargetClass.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(CreateOnTheFly.class)) {
                    fields.add(declaredField);
                }
            }
            currentTargetClass = currentTargetClass.getSuperclass();
        }
        Collections.reverse(fields);
        return fields;
    }

    private <T> void checkBeanClazz(Class<T> beanClazz) {
        if (beanClazz == null) {
            throw new IllegalArgumentException("beanClazz is mandatory");
        }
        if (beanClazz.isInterface() || Modifier.isAbstract(beanClazz.getModifiers())) {
            throw new IllegalArgumentException(beanClazz.getSimpleName() + "is abstract");
        }
        if (Arrays.stream(beanClazz.getDeclaredConstructors()).noneMatch(constructor -> constructor.getParameterCount() == 0)) {
            throw new IllegalArgumentException(beanClazz.getSimpleName() + "has no default constructor");
        }
    }

    private <T> void checkResolvedClazz(Class<T> resolveClazz) {
        if (resolveClazz == null) {
            throw new IllegalArgumentException();
        }
        if (!registeredClass.containsKey(resolveClazz)) {
            throw new IllegalStateException();
        }
    }

    private <T> void injectFieldDependencies(T beanInstance, Field field, Map<Class, Dependency> dependencyBeans)
        throws IllegalAccessException, InstantiationException {

        String fieldName = field.getName();
        Class<?> fieldType = field.getType();
        try {
            beanInstance.getClass()
                .getMethod(getSetterName(fieldName), fieldType)
                .invoke(beanInstance, getDependency(beanInstance, dependencyBeans, fieldType));
        } catch (NoSuchMethodException | InvocationTargetException ignored) {
            // Do nothing.
        }
    }

    private String getSetterName(String fieldName) {
        return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    private <T> Object getDependency(
        T beanInstance,
        Map<Class, Dependency> dependencyBeans,
        Class<?> fieldType) throws InstantiationException, IllegalAccessException {

        Object dependencyBean;
        if (dependencyBeans.containsKey(fieldType)) {
            dependencyBean = getExistedDependency(beanInstance, dependencyBeans, fieldType);
        } else {
            dependencyBean = getNewDependency(beanInstance, dependencyBeans, fieldType);
        }
        return dependencyBean;
    }

    private <T> Object getExistedDependency(
        T beanInstance,
        Map<Class, Dependency> dependencyBeans,
        Class<?> fieldType) throws InstantiationException, IllegalAccessException {

        Object dependencyBean;
        Dependency dependency = dependencyBeans.get(fieldType);
        if (dependency.getConsumer() == beanInstance) {
            dependencyBean = this.getInjectedBean(fieldType, dependencyBeans);
        } else {
            dependencyBean = dependency.getSupplier();
        }
        return dependencyBean;
    }

    private <T> Object getNewDependency(
        T beanInstance,
        Map<Class, Dependency> dependencyBeans,
        Class<?> fieldType) throws InstantiationException, IllegalAccessException {

        Object dependencyBean;
        dependencyBean = this.getInjectedBean(fieldType, dependencyBeans);
        recordBeanInstance(dependencyBeans, fieldType, new Dependency(beanInstance, dependencyBean));
        return dependencyBean;
    }

    private void recordBeanInstance(
        Map<Class, Dependency> dependencyBeans,
        Class<?> fieldType,
        Dependency dependency) {

        dependencyBeans.put(fieldType, dependency);
        this.instantiatedBeans.add(dependency.getSupplier());
    }

    @Override
    public void close() throws Exception {
        List<Object> toBeClosedBeans = new ArrayList<>(this.instantiatedBeans);
        Collections.reverse(toBeClosedBeans);
        Exception toBeThrowException = null;
        for (Object instantiatedBean : toBeClosedBeans) {
            if (Arrays.asList(instantiatedBean.getClass().getInterfaces()).contains(AutoCloseable.class)) {
                try {
                    ((AutoCloseable) instantiatedBean).close();
                } catch (Exception exception) {
                    if (toBeThrowException == null) {
                        toBeThrowException = exception;
                    }
                }
            }
        }
        if (toBeThrowException != null) {
            throw toBeThrowException;
        }
    }
}
