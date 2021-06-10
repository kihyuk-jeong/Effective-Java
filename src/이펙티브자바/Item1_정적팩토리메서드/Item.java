package 이펙티브자바.Item1_정적팩토리메서드;

public interface Item {

    void print();

    static Item createItem(String name) {
        return new Book2(name);
    }
}

class Book2 implements Item {

    String name;

    Book2(String name) {
        this.name = name;
    }

    @Override
    public void print() {
        System.out.println(name);
    }
}


class Main {
    public static void main(String[] args){
        Item item = Item.createItem("LOW");
        item.print();
    }
}