package ioccontainer.materials;

import ioccontainer.CreateOnTheFly;

public class CDependsA {
    @CreateOnTheFly
    private ADependsB dependency;

    public ADependsB getDependency() {
        return dependency;
    }

    public void setDependency(ADependsB dependency) {
        this.dependency = dependency;
    }
}
