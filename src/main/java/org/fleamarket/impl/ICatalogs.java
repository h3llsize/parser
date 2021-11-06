package org.fleamarket.impl;

import java.io.IOException;

public interface ICatalogs {
     void parseAllContainers() throws InterruptedException;
     IContainer getRandomWbContainer();
     IItem getRandomItem() throws IOException, ClassNotFoundException;

}
