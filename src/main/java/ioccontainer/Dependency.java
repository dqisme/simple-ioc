package ioccontainer;

public class Dependency {
    private Object consumer;
    private Object supplier;

    public Dependency(Object consumer, Object supplier) {
        this.consumer = consumer;
        this.supplier = supplier;
    }

    public Object getConsumer() {
        return consumer;
    }

    public void setConsumer(Object consumer) {
        this.consumer = consumer;
    }

    public Object getSupplier() {
        return supplier;
    }

    public void setSupplier(Object supplier) {
        this.supplier = supplier;
    }
}
