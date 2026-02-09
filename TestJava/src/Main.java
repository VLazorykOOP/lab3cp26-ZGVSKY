import java.util.ArrayList;
import java.util.List;


//  ПАТЕРН BUILDER


// об'єкт пк
class Computer {
    private String cpu;
    private String gpu;
    private String ram;
    private String storage;
    private String cooling;

    public void setCpu(String cpu) { this.cpu = cpu; }
    public void setGpu(String gpu) { this.gpu = gpu; }
    public void setRam(String ram) { this.ram = ram; }
    public void setStorage(String storage) { this.storage = storage; }
    public void setCooling(String cooling) { this.cooling = cooling; }

    @Override
    public String toString() {
        return "Computer Spec: [CPU=" + cpu + ", GPU=" + gpu +
                ", RAM=" + ram + ", HDD/SSD=" + storage + ", Cooling=" + cooling + "]";
    }
}

//  Builder Interface (Інтерфейс будівельника)
interface ComputerBuilder {
    void buildCPU();
    void buildGPU();
    void buildRAM();
    void buildStorage();
    void buildCooling();
    Computer getComputer();
}

//Concrete Builder 1 (Ігровий ПК)
class GamingPCBuilder implements ComputerBuilder {
    private Computer computer;

    public GamingPCBuilder() { this.computer = new Computer(); }

    @Override
    public void buildCPU() { computer.setCpu("Intel Core i9-13900K"); }
    @Override
    public void buildGPU() { computer.setGpu("NVIDIA RTX 4090"); }
    @Override
    public void buildRAM() { computer.setRam("32GB DDR5"); }
    @Override
    public void buildStorage() { computer.setStorage("2TB NVMe SSD"); }
    @Override
    public void buildCooling() { computer.setCooling("Liquid Cooling System"); }
    @Override
    public Computer getComputer() { return this.computer; }
}

//Concrete Builder 2 (Офісний ПК)
class OfficePCBuilder implements ComputerBuilder {
    private Computer computer;

    public OfficePCBuilder() { this.computer = new Computer(); }

    @Override
    public void buildCPU() { computer.setCpu("Intel Core i3-12100"); }
    @Override
    public void buildGPU() { computer.setGpu("Integrated Graphics"); }
    @Override
    public void buildRAM() { computer.setRam("8GB DDR4"); }
    @Override
    public void buildStorage() { computer.setStorage("512GB SSD"); }
    @Override
    public void buildCooling() { computer.setCooling("Standard Air Cooler"); }
    @Override
    public Computer getComputer() { return this.computer; }
}

//Керує процесом будівництва
class Director {
    private ComputerBuilder builder;

    public void setBuilder(ComputerBuilder builder) {
        this.builder = builder;
    }

    public Computer constructComputer() {
        builder.buildCPU();
        builder.buildRAM();
        builder.buildGPU();
        builder.buildStorage();
        builder.buildCooling();
        return builder.getComputer();
    }
}


// ITERATOR


//Елемент колекції
class Component {
    private String name;
    private double price;

    public Component(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() { return name + " ($" + price + ")"; }
}

// Інтерфейс ітератора
interface MyIterator {
    boolean hasNext();
    Object next();
}

//Інтерфейс колекції
interface Container {
    MyIterator getIterator();
}

//Concrete Aggregate Складде зберігаються компоненти
class Warehouse implements Container {
    private List<Component> components;

    public Warehouse() {
        components = new ArrayList<>();
        // Заповнюємо склад товарами
        components.add(new Component("RTX 4090", 1600));
        components.add(new Component("Intel i9", 600));
        components.add(new Component("Samsung SSD", 100));
        components.add(new Component("Corsair RAM", 150));
    }

    @Override
    public MyIterator getIterator() {
        return new ComponentIterator();
    }

    // Внутрішній клас ітератора
    private class ComponentIterator implements MyIterator {
        int index = 0;

        @Override
        public boolean hasNext() {
            return index < components.size();
        }

        @Override
        public Object next() {
            if (this.hasNext()) {
                return components.get(index++);
            }
            return null;
        }
    }
}

// ==========================
// ЧАСТИНА 3: ПАТЕРН FACADE
// ==========================

// Facade (Спрощує взаємодію клієнта з системою)
class ComputerShopFacade {
    private Director director;
    private Warehouse warehouse;
    private GamingPCBuilder gamingBuilder;
    private OfficePCBuilder officeBuilder;

    public ComputerShopFacade() {
        this.director = new Director();
        this.warehouse = new Warehouse();
        this.gamingBuilder = new GamingPCBuilder();
        this.officeBuilder = new OfficePCBuilder();
    }

    //інтерфейс для покупки ігрового ПК
    public void buyGamingPC() {
        System.out.println("\n--- [Facade] Preparing Gaming PC Order ---");
        director.setBuilder(gamingBuilder);
        Computer pc = director.constructComputer();
        System.out.println("Assembled: " + pc);
        System.out.println("Order completed!");
    }

    // інтерфейс для покупки офісного ПК
    public void buyOfficePC() {
        System.out.println("\n--- [Facade] Preparing Office PC Order ---");
        director.setBuilder(officeBuilder);
        Computer pc = director.constructComputer();
        System.out.println("Assembled: " + pc);
        System.out.println("Order completed!");
    }

    //нтерфейс для перегляду складу
    public void showCatalog() {
        System.out.println("\n--- [Facade] Shop Catalog (Warehouse) ---");
        MyIterator iterator = warehouse.getIterator();

        while (iterator.hasNext()) {
            Component item = (Component) iterator.next();
            System.out.println("Item available: " + item);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // Клієнт взаємодіє ТІЛЬКИ з фасадоь

        ComputerShopFacade shop = new ComputerShopFacade();

        shop.showCatalog();

        shop.buyGamingPC();

        shop.buyOfficePC();
    }
}