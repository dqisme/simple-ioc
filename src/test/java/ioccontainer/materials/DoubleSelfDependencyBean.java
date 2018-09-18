package ioccontainer.materials;

import ioccontainer.CreateOnTheFly;

public class DoubleSelfDependencyBean {
    @CreateOnTheFly
    private DoubleSelfDependencyBean dependency;

    @CreateOnTheFly
    private DoubleSelfDependencyBean anotherDependency;

    public DoubleSelfDependencyBean getAnotherDependency() {
        return anotherDependency;
    }

    public void setAnotherDependency(DoubleSelfDependencyBean anotherDependency) {
        this.anotherDependency = anotherDependency;
    }

    public DoubleSelfDependencyBean getDependency() {
        return dependency;
    }

    public void setDependency(DoubleSelfDependencyBean dependency) {
        this.dependency = dependency;
    }
}
