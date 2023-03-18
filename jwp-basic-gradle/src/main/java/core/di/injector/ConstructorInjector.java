package core.di.injector;

import static core.di.BeanFactoryUtils.findConcreteClass;
import static core.di.BeanFactoryUtils.getInjectedConstructor;

import core.di.BeanFactory;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import org.springframework.beans.BeanUtils;

public class ConstructorInjector implements Injector {

    private final BeanFactory beanFactory;

    public ConstructorInjector(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void inject(final Class<?> clazz) {
        if (beanFactory.getBean(clazz) == null) {
            instantiateClass(findConcreteClass(clazz, beanFactory.getPreInstantiatedBeans()));
        }
    }

    private Object instantiateClass(final Class<?> preInstantiatedBean) {
        Constructor<?> injectedConstructor = getInjectedConstructor(preInstantiatedBean);
        if (injectedConstructor == null) {
            Object bean = BeanUtils.instantiateClass(preInstantiatedBean);
            beanFactory.registerBean(preInstantiatedBean, bean);
            return bean;
        }
        Object bean = instantiateParameterConstructor(injectedConstructor);
        beanFactory.registerBean(preInstantiatedBean, bean);
        return bean;
    }

    private Object instantiateParameterConstructor(final Constructor<?> injectedConstructor) {
        Class<?>[] parameterTypes = injectedConstructor.getParameterTypes();
        instantiateParameter(parameterTypes);
        return BeanUtils.instantiateClass(injectedConstructor, findBeans(parameterTypes));
    }

    private void instantiateParameter(final Class<?>[] parameterTypes) {
        for (Class<?> parameterType : parameterTypes) {
            Class<?> concreteClass = findConcreteClass(parameterType, beanFactory.getPreInstantiatedBeans());
            if (beanFactory.getBean(concreteClass) == null) {
                instantiateClass(concreteClass);
            }
        }
    }

    private Object[] findBeans(final Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes)
                .map(type -> findConcreteClass(type, beanFactory.getPreInstantiatedBeans()))
                .map(beanFactory::getBean)
                .toArray();
    }


}
