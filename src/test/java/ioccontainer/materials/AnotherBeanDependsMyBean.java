package ioccontainer.materials;

import ioccontainer.CreateOnTheFly;

public class AnotherBeanDependsMyBean {
    @CreateOnTheFly
    private MyBeanDependsAnotherBean dependency;

    public MyBeanDependsAnotherBean getDependency() {
        return dependency;
    }

    public void setDependency(MyBeanDependsAnotherBean dependency) {
        this.dependency = dependency;
    }
}
