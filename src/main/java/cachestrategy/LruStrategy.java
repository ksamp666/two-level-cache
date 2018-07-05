package cachestrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * LRU cache strategy implementation
 */
public class LruStrategy<KeyType> implements Strategy<KeyType> {

    private LinkedList<KeyType> elements = new LinkedList<>();

    @Override
    public KeyType getReplacingKey() {
        return elements.getFirst();
    }

    @Override
    public void notifyAdd(KeyType key) {
        elements.add(key);
    }

    @Override
    public void notifyRemove(KeyType key) {
        elements.remove(key);
    }

    @Override
    public void notifyGet(KeyType key) {
        elements.remove(key);
        elements.add(key);
    }

    @Override
    public void notifyClear() {
        elements.clear();
    }

    @Override
    public List<KeyType> getElementsOrderList() {
        return elements;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!LruStrategy.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final LruStrategy objectToCompareWith = (LruStrategy) obj;

        return this.elements.equals(objectToCompareWith.elements);
    }

    @Override
    public int hashCode() {
        int result = 17;
        for (KeyType element: this.elements) {
            result = 31 * result + element.hashCode();
        }
        return result;
    }
}
