public class DMSList {
    private IntNode sentinel;

    public DMSList() {
        sentinel = new IntNode(-1000, null);
    }

    public class IntNode {
        public int item;
        public IntNode next;

        public IntNode(int i, IntNode h) {
            item = i;
            next = h;
        }

        public int max() {
            return Math.max(item, next.max());
        }
    }

    public void add(int item) {}

    public int max() {
        return sentinel.next.max();
    }

    public void insertFront(int x) {
            sentinel.next = new IntNode(x, sentinel.next);
        }
}
