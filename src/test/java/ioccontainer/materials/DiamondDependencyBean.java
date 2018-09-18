package ioccontainer.materials;

import ioccontainer.CreateOnTheFly;

public class DiamondDependencyBean {
    @CreateOnTheFly
    private DiamondDependency dependency;

    @CreateOnTheFly
    private DiamondDependency anotherDependency;

    public DiamondDependency getDependency() {
        return dependency;
    }

    public void setDependency(DiamondDependency dependency) {
        this.dependency = dependency;
    }

    public DiamondDependency getAnotherDependency() {
        return anotherDependency;
    }

    public void setAnotherDependency(DiamondDependency anotherDependency) {
        this.anotherDependency = anotherDependency;
    }
}
