package ioccontainer.materials;

public class MyDependency implements AutoCloseable {
    private boolean isClosed = false;

    private Runnable onClose = () -> {};

    @Override
    public void close() throws Exception {
        this.isClosed = true;
        onClose.run();
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    public boolean isClosed() {
        return isClosed;
    }
}
