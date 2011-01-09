package ch.lambdaj.function.aggregate;

import java.math.*;
import java.util.*;

/**
 * @author Mario Fusco
 */
public class Avg implements Aggregator<Number> {

    public Number aggregate(Iterator<? extends Number> iterator) {
        int counter = 0;
        Number total = null;
        while (iterator.hasNext()) {
            total = aggregate(total, iterator.next());
            counter++;
        }
        return divide(total, counter);
    }

    private Number aggregate(Number first, Number second) {
        if (second == null) return first == null ? 0.0 : first;

        if (second instanceof Integer) return aggregate(first, (Integer)second);
        if (second instanceof Long) return aggregate(first, (Long)second);
        if (second instanceof Float) return aggregate(first, (Float)second);
        if (second instanceof Double) return aggregate(first, (Double)second);
        if (second instanceof BigInteger) return aggregate(first, (BigInteger)second);
        if (second instanceof BigDecimal) return aggregate(first, (BigDecimal)second);

        throw new RuntimeException("unable to aggregate " + first + " and " + second);
    }

    private Number divide(Number total, int count) {
        if (count < 2) return total;
        if (total instanceof Integer) return total.intValue() / count;
        if (total instanceof Long) return total.longValue() / count;
        if (total instanceof Float) return total.floatValue() / count;
        if (total instanceof Double) return total.doubleValue() / count;
        if (total instanceof BigInteger) return ((BigInteger)total).divide(BigInteger.valueOf(count));
        if (total instanceof BigDecimal) return ((BigDecimal)total).divide(new BigDecimal(count));

        throw new RuntimeException("Unknown number type");
    }

    private Integer aggregate(Number first, Integer second) {
        return first == null ? second : first.intValue() + second;
    }

    private Long aggregate(Number first, Long second) {
        return first == null ? second : first.longValue() + second;
    }

    private Float aggregate(Number first, Float second) {
        return first == null ? second : first.floatValue() + second;
    }

    private Double aggregate(Number first, Double second) {
        return first == null ? second : first.doubleValue() + second;
    }

    private BigInteger aggregate(Number first, BigInteger second) {
        return first == null ? second : ((BigInteger)first).add(second);
    }

    private BigDecimal aggregate(Number first, BigDecimal second) {
        return first == null ? second : ((BigDecimal)first).add(second);
    }
}
