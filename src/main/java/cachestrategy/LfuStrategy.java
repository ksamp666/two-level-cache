package cachestrategy;
import java.util.*;

/**
 * LFU cache strategy implementation
 */
public class LfuStrategy<KeyType> implements Strategy<KeyType> {

    private ArrayList<KeyType> elements = new ArrayList<>();
    private Map<KeyType, Integer> elementsUses = new HashMap<>();

    @Override
    public KeyType getReplacingKey() {
        return elements.get(0);
    }

    @Override
    public void notifyAdd(KeyType key) {
        elements.add(key);
        elementsUses.put(key, 0);
    }

    @Override
    public void notifyRemove(KeyType key) {
        elements.remove(key);
        elementsUses.remove(key);
    }

    @Override
    public void notifyGet(KeyType key) {
        elementsUses.put(key, elementsUses.get(key)+1);
        elements.sort((o1, o2) -> {
            if (elementsUses.get(o1) < elementsUses.get(o2)) {
                return -1;
            } else if (elementsUses.get(o1) > elementsUses.get(o2)) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    @Override
    public void notifyClear() {
        elements.clear();
        elementsUses.clear();
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
        if (!LfuStrategy.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final LfuStrategy objectToCompareWith = (LfuStrategy) obj;

        return this.elements.equals(objectToCompareWith.elements);
    }

    @Override
    public int hashCode() {
        int result = 17;
        for (KeyType element: this.elements) {
            result = 31 * result + element.hashCode();
            result = 31 * result + elementsUses.get(element).hashCode();
        }
        return result;
    }
}
