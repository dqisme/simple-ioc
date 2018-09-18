package ioccontainer.materials;

import ioccontainer.CreateOnTheFly;

public class BDependsC {
    @CreateOnTheFly
    private CDependsA dependency;

    public CDependsA getDependency() {
        return dependency;
    }

    public void setDependency(CDependsA dependency) {
        this.dependency = dependency;
    }
}
