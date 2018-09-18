package ioccontainer.materials;

import ioccontainer.CreateOnTheFly;

public class ADependsB {
    @CreateOnTheFly
    private BDependsC dependency;

    public BDependsC getDependency() {
        return dependency;
    }

    public void setDependency(BDependsC dependency) {
        this.dependency = dependency;
    }
}
