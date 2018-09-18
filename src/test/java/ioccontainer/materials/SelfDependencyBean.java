package ioccontainer.materials;

import ioccontainer.CreateOnTheFly;

public class SelfDependencyBean {
    @CreateOnTheFly
    private  SelfDependencyBean dependency;

    public SelfDependencyBean getDependency() {
        return dependency;
    }

    public void setDependency(SelfDependencyBean dependency) {
        this.dependency = dependency;
    }
}
