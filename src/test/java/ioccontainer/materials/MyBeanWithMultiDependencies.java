package ioccontainer.materials;

import ioccontainer.CreateOnTheFly;

public class MyBeanWithMultiDependencies {
    @CreateOnTheFly
    private MyDependency dependency;

    @CreateOnTheFly
    private AnotherDependency anotherDependency;

    public MyDependency getDependency() {
        return dependency;
    }

    public void setDependency(MyDependency dependency) {
        this.dependency = dependency;
    }

    public AnotherDependency getAnotherDependency() {
        return anotherDependency;
    }

    public void setAnotherDependency(AnotherDependency anotherDependency) {
        this.anotherDependency = anotherDependency;
    }
}
