package org.openjfx.dao;

import java.util.List;

public interface GenericDAO<E> {

    public E create(E e);

    public E update(E e);

    public E findOne(Object id);

    public boolean delete(Object id);

    public List<E> listAll();
}
