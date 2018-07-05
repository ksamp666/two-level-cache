package cachestrategy;

/**
 * This class is used to generate correct strategy instance based on strategy type
 */
public class StrategyBuilder<KeyType> {
    private StrategyType strategyType;

    /**
     * Class constructor
     * @param strategyType - type of strategy to be generated
     */
    public StrategyBuilder(StrategyType strategyType) {
        this.strategyType = strategyType;
    }

    /**
     * Generates a strategy according to strategy type
     * @return generated strategt according to strategy type
     */
    public Strategy<KeyType> createStrategy() {
        Strategy<KeyType> strategy = null;
        switch(strategyType) {
            case STRATEGY_FIFO:
                strategy = new FifoStrategy<>();
                break;
            case STRATEGY_LRU:
                strategy = new LruStrategy<>();
                break;
            case STRATEGY_LIFO:
                strategy = new LifoStrategy<>();
                break;
            case STRATEGY_MRU:
                strategy = new MruStrategy<>();
                break;
            case STRATEGY_LFU:
                strategy = new LfuStrategy<>();
                break;
        }
        return strategy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!StrategyBuilder.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final StrategyBuilder objectToCompareWith = (StrategyBuilder) obj;

        if(this.strategyType!=objectToCompareWith.strategyType) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return 17 + strategyType.hashCode();
    }
}
