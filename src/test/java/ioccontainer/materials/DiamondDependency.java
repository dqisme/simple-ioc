package ioccontainer.materials;

import ioccontainer.CreateOnTheFly;

public class DiamondDependency {
    @CreateOnTheFly
    private DiamondDependencyBean dependency;

    public DiamondDependencyBean getDependency() {
        return dependency;
    }

    public void setDependency(DiamondDependencyBean dependency) {
        this.dependency = dependency;
    }
}
