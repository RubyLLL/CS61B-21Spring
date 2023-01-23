ckgrndomizedtest;
* rybased list.
  ahr Josh Hug
 0 1 2 3 4 5 6 7
 ie:[6 9 -1 2 0 0 0 0 ...]
 sz 
 Ivints:
dda:The next item we want to add, will go into position size
eta:The item we want to return is in position size - 1
iz:h number of items in the list should be size.

bia BuggyAList<Item> {
  rae Item[] items;
  rae int size;
  *Ceates an empty list. */
  ui BuggyAList() {
 ites = (Item[]) new Object[1];
 siz = 0;
  
/eszes the underlying array to the target capacity. */
  rae void resize(int capacity) {
 Ite[] a = (Item[]) new Object[capacity];
 forint i = 0; i < size; i++) {
    a[i] = items[i];
 }
 tes = a;
  
/nsrts X into the back of the list. */
  ui void addLast(Item x) {
 if size == items.length) {
    resize(size * 2);
 }
 tes[size] = x;
 siz = size + 1;
  
/etrns the item from the back of the list. */
  ui Item getLast() {
 retrn items[size - 1];
  
/et the ith item in the list (0 is the front). */
  ui Item get(int i) {
 retrn items[i];
  
/etrns the number of items in the list. */
  ui int size() {
 retrn size;
  
/eltes item from back of the list and
 retrns deleted item. */
  ui Item removeLast() {
 if (size < items.length / 4) && (size > 4)) {
    resize(items.length / 4);
 }
 te x = getLast();
 ites[size - 1] = null;
 siz = size - 1;
 retrn x;
  
 public void play(int happiness) {
     if (happiness > 10) {
     System.out.println("Woo it is so much fun being a dog!")
     }
     }
     }
    
     public class TestAnimal {
     public static void main(String[] args) {
     Animal a = new Animal("Pluto", 10);
     Cat c = new Cat("Garfield", 6);
     Dog d = new Dog("Fido", 4);
     a.greet(); // ______________________
     c.greet(); // ______________________
     d.greet(); // ______________________
     c.play(); // ______________________
     c.play(":)") // ______________________
     a = c;
     ((Cat) a).greet(); // ______________________
     ((Cat) a).play(":D"); // ______________________
     a.play(14); // ______________________
     ((Dog) a).play(12); // ______________________
     a.greet(); // ______________________
     c = a; // ______________________
     }
     }