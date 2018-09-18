package ioccontainer.materials;

import ioccontainer.CreateOnTheFly;

public class MyBeanWithSuperDependency extends SuperDependencyBean {
    @CreateOnTheFly
    private MyDependency dependency;

    public MyDependency getDependency() {
        return dependency;
    }

    public void setDependency(MyDependency dependency) {
        this.dependency = dependency;
    }
}
