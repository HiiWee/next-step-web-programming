package core.di;

import java.util.Set;

public interface ApplicationContext {

    Set<Class<?>> getBeanClasses();

    <T> T getBean(final Class<T> beanClass);

}
