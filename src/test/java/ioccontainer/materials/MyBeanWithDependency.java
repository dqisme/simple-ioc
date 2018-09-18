package ioccontainer.materials;

import ioccontainer.CreateOnTheFly;

public class MyBeanWithDependency {
    @CreateOnTheFly
    private MyDependency dependency;

    public MyDependency getDependency() {
        return dependency;
    }

    public void setDependency(MyDependency dependency) {
        this.dependency = dependency;
    }
}
