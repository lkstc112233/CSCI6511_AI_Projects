package com.photoncat.aiproj2.util;

import java.util.*;

/**
 * Note: There's not a built-in Fibonacci heap in java, plus the heap it self has a huge constant factor
 * in most implementations.
 *
 * However, I found this useful trick for the search problem.
 *
 * This Queue will only extract elements with same key ONCE.
 *
 * So multiple decreased value can be simply inserted into this queue. And when removed, they will only be removed once.
 *
 * Moreover, this queue offers a method to check whether an element has been removed or not.
 *
 * By default the queue is a min heap.
 */
public class OnetimePriorityQueue<Element> extends PriorityQueue<Map.Entry<Element, Integer>> {
    private final Map<Element, Integer> extracted = new HashMap<>();
    public static final Comparator compareByMax = Map.Entry.comparingByValue((a, b)->((Integer)b - (Integer)a));

    public OnetimePriorityQueue() {
        super(Map.Entry.comparingByValue());
    }

    public OnetimePriorityQueue(Comparator comparator) {
        super(comparator);
    }

    public boolean add(Element key, Integer value) {
        return offer(new AbstractMap.SimpleEntry<>(key, value));
    }

    public boolean extracted(Element elem) {
        return extracted.containsKey(elem);
    }

    /**
     * @return true if the element is added into the queue. false if the element is extracted already.
     */
    @Override
    public boolean offer(Map.Entry<Element, Integer> element) {
        if (!extracted.containsKey(element.getKey())) {
            return super.offer(element);
        } else if (extracted.get(element.getKey()) > element.getValue()) {
            extracted.remove(element.getKey());
            return super.offer(element);
        }
        return false;
    }

    @Override
    public Map.Entry<Element, Integer> poll() {
        Map.Entry<Element, Integer> elem = peek();
        extracted.put(elem.getKey(), elem.getValue());
        super.poll();
        return elem;
    }

    @Override
    public Map.Entry<Element, Integer> peek(){
        while (super.size() != 0) {
            if (extracted.containsKey(super.peek().getKey())) {
                // Remove extracted elements.
                super.poll();
            } else {
                return super.peek();
            }
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return peek() == null;
    }

    @Override
    public void clear() {
        super.clear();
        extracted.clear();
    }

    @Override
    public int size() {
        throw new IllegalStateException();
    }
}
