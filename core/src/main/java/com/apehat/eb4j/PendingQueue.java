///*
// * Copyright Apehat.com
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.apehat.eb4j;
//
//import com.apehat.eb4j.event.Event;
//import com.apehat.eb4j.event.EventQueue;
//import com.apehat.eb4j.subscription.Subscriber;
//import com.apehat.eb4j.subscription.SubscriberStore;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * @author hanpengfei
// * @since 1.0
// */
//@Deprecated
//public class PendingQueue implements EventQueue, SubscriberStore {
//
//    private final BlockingQueue<Subscriber> subscribers = new LinkedBlockingQueue<>();
//    private final BlockingQueue<Submitted<?>> submitteds = new LinkedBlockingQueue<>();
//
//    private AtomicInteger subscriberCount = new AtomicInteger();
//    private Event preEvent = null;
//
//    @Override
//    public void put(Event event) {
//        Submitted<Event> eventSubmitted = new Submitted<>(event);
//        try {
//            submitteds.put(eventSubmitted);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public Event nextEvent() {
//        flush();
//        if (hasNext()) {
//            Submitted<?> poll = submitteds.poll();
//            assert poll != null;
//            preEvent = (Event) poll.source;
//            return preEvent;
//        }
//        return null;
//    }
//
//    @Override
//    public boolean hasNext() {
//        flush();
//        return submitteds.peek() != null;
//    }
//
//    private void flush() {
//        if (subscribers.size() == subscriberCount.get()) {
//            return;
//        }
//        synchronized (submitteds) {
//            Submitted<?> peek = submitteds.peek();
//            while (peek != null && !peek.isEvent) {
//                Submitted<?> poll = submitteds.poll();
//                assert poll != null;
//                subscribers.add((Subscriber) poll.source);
//                peek = submitteds.peek();
//            }
//        }
//    }
//
//    @Override
//    public void store(Subscriber subscriber) {
//        Submitted<Subscriber> subscriberSubmitted = new Submitted<>(subscriber);
//        try {
//            submitteds.put(subscriberSubmitted);
//            subscriberCount.set(subscriberCount.get() + 1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void remove(Subscriber subscriber) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public Collection<Subscriber> subscribersOf(Event event) {
//        Set<Subscriber> subscriberSet = new HashSet<>();
//        if (event.equals(preEvent)) {
//            for (Subscriber subscriber : subscribers) {
//                if (event.head().type().belongTo(subscriber.subscribeTo())) {
//                    subscriberSet.add(subscriber);
//                }
//            }
//        }
//        return subscriberSet;
//    }
//
//    @Override
//    public boolean contains(Subscriber subscriber) {
//        if (subscribers.contains(subscriber)) {
//            return true;
//        }
//        Submitted<Subscriber> submitted = new Submitted<>(subscriber);
//        return submitteds.contains(submitted);
//    }
//
//    @Override
//    public int size() {
//        return subscriberCount.get();
//    }
//
//    private static class Submitted<T> {
//
//        private final T source;
//        private final boolean isEvent;
//
//        private Submitted(T source) {
//            this.source = source;
//            this.isEvent = source instanceof Event;
//        }
//    }
//}
