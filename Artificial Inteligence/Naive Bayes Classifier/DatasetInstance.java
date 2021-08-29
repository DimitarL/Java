public class DatasetInstance {
    private final String className;
    private final String[] attributes;

    public DatasetInstance(String className, String[] attributes) {
        this.attributes = attributes;
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public String[] getAttributes() {
        return attributes;
    }
}