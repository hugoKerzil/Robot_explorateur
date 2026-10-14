import nicellipse.component.NiBasicComponent;

public class Item {

    private String name;
    private NiBasicComponent corps;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NiBasicComponent getCorps() {
        return corps;
    }

    public void setCorps(NiBasicComponent corps) {
        this.corps = corps;
    }

    public Item(String name, NiBasicComponent corps) {
        this.name = name;
        this.corps = corps;
    }
}
