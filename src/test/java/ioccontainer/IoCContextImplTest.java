package ioccontainer;

import ioccontainer.materials.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IoCContextImplTest {
    @Test
    void shouldGetBeanWhenRegistered() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBean.class);
        MyBean myBeanInstance = context.getBean(MyBean.class);

        assertEquals(MyBean.class, myBeanInstance.getClass());
    }

    @Test
    void shouldGetCorrespondingBean() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBean.class);
        context.registerBean(AnotherBean.class);

        MyBean myBean = context.getBean(MyBean.class);
        AnotherBean anotherBean = context.getBean(AnotherBean.class);

        assertEquals(MyBean.class, myBean.getClass());
        assertEquals(AnotherBean.class, anotherBean.getClass());
    }

    @Test
    void shouldNotBeSameInstanceWhenGetAgain() throws InstantiationException, IllegalAccessException {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBean.class);
        MyBean anotherMyBean = context.getBean(MyBean.class);
        MyBean myBean = context.getBean(MyBean.class);

        assertNotSame(myBean, anotherMyBean);
    }

    @Test
    void shouldThrowExceptionWhenRegisterNull() {
        IoCContext context = new IoCContextImpl();

        assertThrows(IllegalArgumentException.class,
            () -> context.registerBean(null),
            "beanClazz is mandatory");
    }

    @Test
    void shouldThrowExceptionWhenBeanCanNotBeInstantiated() {
        IoCContext context = new IoCContextImpl();

        assertThrows(IllegalArgumentException.class,
            () -> context.registerBean(AbstractClass.class),
            AbstractClass.class.getSimpleName() + "is abstract");
    }

    @Test
    void shouldThrowExceptionWhenBeanHasNoDefaultConstructor() {
        IoCContext context = new IoCContextImpl();

        assertThrows(IllegalArgumentException.class,
            () -> context.registerBean(ClassWithoutDefaultConstructor.class),
            ClassWithoutDefaultConstructor.class.getSimpleName() + "has no default constructor");
    }

    @Test
    void shouldNotThrowsWhenRegisterSameBeanTwice() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBean.class);

        assertDoesNotThrow(() -> context.registerBean(MyBean.class));
    }

    @Test
    void shouldThrowExceptionWhenGetNull() {
        IoCContext context = new IoCContextImpl();

        assertThrows(IllegalArgumentException.class,
            () -> context.getBean(null));
    }

    @Test
    void shouldThrowExceptionWhenGetBeanNotRegistered() {
        IoCContext context = new IoCContextImpl();

        assertThrows(IllegalStateException.class,
            () -> context.getBean(MyBean.class));
    }

    @Test
    void shouldThrowExceptionFromBeanConstructor() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(BeanWithException.class);

        assertThrows(MyException.class,
            () -> context.getBean(BeanWithException.class));
    }

    @Test
    void shouldThrowExceptionWhenRegisterAfterGetBean() throws InstantiationException, IllegalAccessException {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBean.class);
        context.getBean(MyBean.class);

        assertThrows(IllegalStateException.class,
            () -> context.registerBean(MyBean.class));
    }

    @Test
    void shouldRegisterBeanByBaseClass() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBeanBase.class, MyBean.class);

        MyBeanBase myBeanInstance = context.getBean(MyBeanBase.class);

        assertEquals(MyBean.class, myBeanInstance.getClass());
    }

    @Test
    void shouldRegisterBeanByInterface() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyInterface.class, MyBean.class);

        MyInterface myBeanInstance = context.getBean(MyInterface.class);

        assertEquals(MyBean.class, myBeanInstance.getClass());
    }

    @Test
    void shouldRegisterBeanByBothBaseClassAndInterface() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBeanBase.class, MyBean.class);
        context.registerBean(MyInterface.class, MyBean.class);

        MyBeanBase myBeanInstance = context.getBean(MyBeanBase.class);
        MyInterface anotherMyBeanInstance = context.getBean(MyInterface.class);

        assertEquals(MyBean.class, myBeanInstance.getClass());
        assertEquals(MyBean.class, anotherMyBeanInstance.getClass());
        assertNotSame(myBeanInstance, anotherMyBeanInstance);
    }

    @Test
    void shouldOverrideBeanWhenRegisterAnotherBeanWithSameResolveType() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBeanBase.class, MyBean.class);
        context.registerBean(MyBeanBase.class, AnotherBean.class);

        MyBeanBase myBeanInstance = context.getBean(MyBeanBase.class);

        assertEquals(AnotherBean.class, myBeanInstance.getClass());
    }

    @Test
    void shouldOverrideBeanWhenRegisterAnotherBeanWithSameResolveTypeByOverloading() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBeanBase.class, MyBean.class);
        context.registerBean(MyBeanBase.class);

        MyBeanBase myBeanInstance = context.getBean(MyBeanBase.class);

        assertEquals(MyBeanBase.class, myBeanInstance.getClass());
    }


    @Test
    void shouldInjectDependenciesWhenGetBean() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBeanWithDependency.class);
        context.registerBean(MyDependency.class);

        MyBeanWithDependency bean = context.getBean(MyBeanWithDependency.class);
        assertNotNull(bean.getDependency());
        assertEquals(MyDependency.class, bean.getDependency().getClass());
    }

    @Test
    void shouldThrowExceptionWhenGetBeanWithDependencyNotRegistered() {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBeanWithDependency.class);

        assertThrows(IllegalStateException.class, () -> context.getBean(MyBeanWithDependency.class));
    }

    @Test
    void shouldBeAbleHasRecursiveDependency() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBeanDependsAnotherBean.class);
        context.registerBean(AnotherBeanDependsMyBean.class);

        MyBeanDependsAnotherBean beanDependsAnotherBean = context.getBean(MyBeanDependsAnotherBean.class);
        assertSame(beanDependsAnotherBean, beanDependsAnotherBean.getDependency().getDependency());

        AnotherBeanDependsMyBean anotherBeanDependsMyBean = context.getBean(AnotherBeanDependsMyBean.class);
        assertSame(anotherBeanDependsMyBean, anotherBeanDependsMyBean.getDependency().getDependency());
    }

    @Test
    void shouldBeAbleHasRecursiveDependencyMoreThanTwoBeans() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(ADependsB.class);
        context.registerBean(BDependsC.class);
        context.registerBean(CDependsA.class);

        ADependsB beanA = context.getBean(ADependsB.class);
        BDependsC beanB = context.getBean(BDependsC.class);
        CDependsA beanC = context.getBean(CDependsA.class);

        assertSame(beanA, beanA.getDependency().getDependency().getDependency());
        assertSame(beanB, beanB.getDependency().getDependency().getDependency());
        assertSame(beanC, beanC.getDependency().getDependency().getDependency());
    }

    @Test
    void shouldBeAbleHasMultipleDependencies() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBeanWithMultiDependencies.class);
        context.registerBean(MyDependency.class);
        context.registerBean(AnotherDependency.class);

        MyBeanWithMultiDependencies beanWithMultiDependencies = context.getBean(MyBeanWithMultiDependencies.class);
        assertEquals(MyDependency.class, beanWithMultiDependencies.getDependency().getClass());
        assertEquals(AnotherDependency.class, beanWithMultiDependencies.getAnotherDependency().getClass());
    }

    @Test
    void shouldNotBeSameInstanceWhenBeanHasTwoSameDependencies() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBeanWithSameDependencies.class);
        context.registerBean(MyDependency.class);

        MyBeanWithSameDependencies bean = context.getBean(MyBeanWithSameDependencies.class);

        assertNotSame(bean.getDependency(), bean.getAnotherDependency());
    }

    @Test
    void shouldNotBeSameInstanceWhenBeanHasTwoSameDependenciesFromSuper() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBeanWithSuperDependency.class);
        context.registerBean(MyDependency.class);

        MyBeanWithSuperDependency beanWithSuperDependency = context.getBean(MyBeanWithSuperDependency.class);
        assertNotSame(beanWithSuperDependency.getDependency(), beanWithSuperDependency.getSuperDependency());
    }

    @Test
    void shouldBeAbleDependsOnSelf() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(SelfDependencyBean.class);

        SelfDependencyBean selfDependencyBean = context.getBean(SelfDependencyBean.class);
        assertSame(selfDependencyBean, selfDependencyBean.getDependency());
    }

    @Test
    void shouldBeAbleDependsOnSelfTwice() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(DoubleSelfDependencyBean.class);

        DoubleSelfDependencyBean doubleSelfDependencyBean = context.getBean(DoubleSelfDependencyBean.class);
        assertSame(doubleSelfDependencyBean, doubleSelfDependencyBean.getDependency());
        assertSame(doubleSelfDependencyBean, doubleSelfDependencyBean.getAnotherDependency());
    }

    @Test
    void shouldBeAbleDiamondDependency() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(DiamondDependencyBean.class);
        context.registerBean(DiamondDependency.class);

        DiamondDependencyBean diamondDependencyBean = context.getBean(DiamondDependencyBean.class);
        DiamondDependency dependency = diamondDependencyBean.getDependency();
        DiamondDependency anotherDependency = diamondDependencyBean.getAnotherDependency();

        assertNotSame(dependency, anotherDependency);
        assertSame(dependency.getDependency(), anotherDependency.getDependency());
    }

    @Test
    void shouldInjectSuperClassDependencyFirstWhenGetBean() throws InstantiationException, IllegalAccessException {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBeanWithSuperDependency.class);
        context.registerBean(MyDependency.class);

        MyBeanWithSuperDependency beanWithSuperDependency = context.getBean(MyBeanWithSuperDependency.class);

        MyDependency superDependency = beanWithSuperDependency.getSuperDependency();
        assertEquals(MyDependency.class, superDependency.getClass());

        List<Object> instantiatedBeans = new ArrayList<>(context.getInstantiatedBeans());
        MyDependency dependency = beanWithSuperDependency.getDependency();
        assertTrue(instantiatedBeans.contains(dependency));
        assertTrue(instantiatedBeans.contains(superDependency));
        assertTrue(instantiatedBeans.indexOf(superDependency) < instantiatedBeans.indexOf(dependency));
    }

    @Test
    void shouldCloseAllInstantiatedBeansWhenCloseIoCContext() throws Exception {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBeanWithSuperDependency.class);
        context.registerBean(MyDependency.class);

        MyBeanWithSuperDependency beanWithSuperDependency = context.getBean(MyBeanWithSuperDependency.class);
        MyDependency superDependency = beanWithSuperDependency.getSuperDependency();
        MyDependency dependency = beanWithSuperDependency.getDependency();

        context.close();

        assertTrue(superDependency.isClosed());
        assertTrue(dependency.isClosed());
    }

    @Test
    void shouldCloseInstantiatedBeansOrderedReverselyByCreating() throws Exception {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBeanWithSuperDependency.class);
        context.registerBean(MyDependency.class);

        MyBeanWithSuperDependency beanWithSuperDependency = context.getBean(MyBeanWithSuperDependency.class);
        MyDependency superDependency = beanWithSuperDependency.getSuperDependency();
        MyDependency dependency = beanWithSuperDependency.getDependency();

        ArrayList<String> closingLog = new ArrayList<>();
        String superDependencyCloseMessage = "Super Dependency Close!";
        String dependencyCloseMessage = "Dependency Close!";
        superDependency.setOnClose(() -> {
            closingLog.add(superDependencyCloseMessage);
        });
        dependency.setOnClose(() -> {
            closingLog.add(dependencyCloseMessage);
        });

        context.close();

        assertIterableEquals(
            Arrays.asList(dependencyCloseMessage, superDependencyCloseMessage),
            closingLog);
    }

    @Test
    void shouldThrowFirstExceptionAfterAllBeansHasClosed() throws Exception {
        IoCContextImpl context = new IoCContextImpl();
        context.registerBean(MyBeanWithSuperDependency.class);
        context.registerBean(MyDependency.class);

        MyBeanWithSuperDependency beanWithSuperDependency = context.getBean(MyBeanWithSuperDependency.class);
        MyDependency superDependency = beanWithSuperDependency.getSuperDependency();
        MyDependency dependency = beanWithSuperDependency.getDependency();

        String superDependencyCloseExceptionMessage = "Super Dependency Close Exception";
        String dependencyCloseExceptionMessage = "Dependency Close Exception";
        superDependency.setOnClose(() -> {
            throw new RuntimeException(superDependencyCloseExceptionMessage);
        });
        dependency.setOnClose(() -> {
            throw new RuntimeException(dependencyCloseExceptionMessage);
        });

        try {
            context.close();
        } catch (Exception exception) {
            assertEquals(dependencyCloseExceptionMessage, exception.getMessage());
        } finally {
            assertTrue(superDependency.isClosed());
            assertTrue(dependency.isClosed());
        }
    }
}