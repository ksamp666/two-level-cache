package cachestrategy;

import java.util.List;
import java.util.Stack;

/**
 * LIFO cache strategy implementation
 */
public class LifoStrategy<KeyType> implements Strategy<KeyType> {

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
        //Do nothing
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
        if (!LifoStrategy.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final LifoStrategy objectToCompareWith = (LifoStrategy) obj;

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
