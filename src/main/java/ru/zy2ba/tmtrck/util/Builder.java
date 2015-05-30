package ru.zy2ba.tmtrck.util;

/**
 * 
 * @author zy2ba
 * @since 04.05.2015.
 * @param <T> generic Type.
 * 
 * Generic builder which has abstract build method.
 */
interface Builder<T> {

    T build() throws Exception;
}
