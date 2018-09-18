package ioccontainer.materials;

import ioccontainer.CreateOnTheFly;

public class MyBeanWithSameDependencies {
    @CreateOnTheFly
    private MyDependency dependency;

    @CreateOnTheFly
    private MyDependency anotherDependency;

    public MyDependency getAnotherDependency() {
        return anotherDependency;
    }

    public void setAnotherDependency(MyDependency anotherDependency) {
        this.anotherDependency = anotherDependency;
    }

    public MyDependency getDependency() {
        return dependency;
    }

    public void setDependency(MyDependency dependency) {
        this.dependency = dependency;
    }
}
