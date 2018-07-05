package cachestrategy;

import java.util.List;
import java.util.Stack;

/**
 * MRU cache strategy implementation
 */
public class MruStrategy<KeyType> implements Strategy<KeyType> {

    private Stack<KeyType> elements = new Stack<>();

    @Override
    public KeyType getReplacingKey() {
        return elements.peek();
    }

    @Override
    public void notifyAdd(KeyType key) {
        elements.push(key);
    }

    @Override
    public void notifyRemove(KeyType key) {
        elements.remove(key);
    }

    @Override
    public void notifyGet(KeyType key) {
        elements.remove(key);
        elements.push(key);
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
        if (!MruStrategy.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final MruStrategy objectToCompareWith = (MruStrategy) obj;

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
