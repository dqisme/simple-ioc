package ioccontainer.materials;

import ioccontainer.CreateOnTheFly;
import ioccontainer.materials.AnotherBeanDependsMyBean;

public class MyBeanDependsAnotherBean {
    @CreateOnTheFly
    private AnotherBeanDependsMyBean dependency;

    public AnotherBeanDependsMyBean getDependency() {
        return dependency;
    }

    public void setDependency(AnotherBeanDependsMyBean dependency) {
        this.dependency = dependency;
    }
}
