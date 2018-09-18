package ioccontainer.materials;

import ioccontainer.CreateOnTheFly;

public class SuperDependencyBean {
    @CreateOnTheFly
    private MyDependency superDependency;

    public MyDependency getSuperDependency() {
        return superDependency;
    }

    public void setSuperDependency(MyDependency superDependency) {
        this.superDependency = superDependency;
    }
}
