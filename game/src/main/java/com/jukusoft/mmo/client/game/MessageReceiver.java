package com.jukusoft.mmo.client.game;

@FunctionalInterface
public interface MessageReceiver<T> {

    public void receive(T buffer);

}
