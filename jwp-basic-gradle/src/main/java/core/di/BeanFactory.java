package core.di;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.swing.text.html.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class BeanFactory implements BeanDefinitionRegistry {

    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private final Map<Class<?>, BeanDefinition> beanDefinitions = Maps.newHashMap();

    public void initialize() {
        for (Class<?> clazz : getBeanClasses()) {
            getBean(clazz);
        }
        log.debug("complete bean scan");
    }

    @Override
    public void registerBeanDefinition(final Class<?> clazz, final BeanDefinition beanDefinition) {
        log.debug("register bean = {}", clazz);
        beanDefinitions.put(clazz, beanDefinition);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        Object bean = beans.get(clazz);
        if (bean != null) {
            return (T) bean;
        }
        // 인젝트 작업
        BeanDefinition beanDefinition = beanDefinitions.get(clazz);
        if (beanDefinition != null && beanDefinition instanceof AnnotatedBeanDefinition) {
            Optional<Object> optionalBean = createAnnotatedBean(beanDefinition);
            optionalBean.ifPresent(b -> beans.put(clazz, b));
            return (T) optionalBean.orElse(null);
        }
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses());
        beanDefinition = beanDefinitions.get(concreteClass);
        bean = inject(beanDefinition);
        beans.put(concreteClass, bean);
        return (T) bean;
    }

    private Optional<Object> createAnnotatedBean(final BeanDefinition beanDefinition) {
        AnnotatedBeanDefinition abd = (AnnotatedBeanDefinition) beanDefinition;
        Method method = abd.getMethod();
        Object[] args = populateArguments(method.getParameterTypes());
        return BeanFactoryUtils.invokeMethod(method, getBean(method.getDeclaringClass()), args);
    }

    private Object[] populateArguments(final Class<?>[] parameterTypes) {
        List<Object> args = Lists.newArrayList();
        for (Class<?> param : parameterTypes) {
            Object bean = getBean(param);
            if (bean == null) {
                throw new NullPointerException(param + "에 해당하는 Bean이 존재하지 않습니다.");
            }
            args.add(getBean(param));
        }
        return args.toArray();
    }

    public Set<Class<?>> getBeanClasses() {
        return Collections.unmodifiableSet(beanDefinitions.keySet());
    }

    public Object inject(final BeanDefinition beanDefinition) {
        if (beanDefinition.getResolvedInjectMode() == InjectType.INJECT_NO) {
            return BeanUtils.instantiate(beanDefinition.getBeanClass());
        }
        if (beanDefinition.getResolvedInjectMode() == InjectType.INJECT_FIELD) {
            return injectFields(beanDefinition);
        }
        return injectConstructor(beanDefinition);
    }

    private Object injectFields(final BeanDefinition beanDefinition) {
        Object bean = BeanUtils.instantiate(beanDefinition.getBeanClass());
        Set<Field> fields = beanDefinition.getInjectFields();
        for (Field field : fields) {
            injectField(bean, field);
        }
        return bean;
    }

    private void injectField(final Object bean, final Field field) {
        log.debug("Inject Bean = {}, Field = {}", bean, field);
        field.setAccessible(true);
        try {
            field.set(bean, getBean(field.getType()));
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Object injectConstructor(final BeanDefinition beanDefinition) {
        Constructor<?> constructor = beanDefinition.getInjectConstructor();
        List<Object> arguments = Lists.newArrayList();
        for (Class<?> parameterType : constructor.getParameterTypes()) {
            arguments.add(getBean(parameterType));
        }
        return BeanUtils.instantiateClass(constructor, arguments.toArray());
    }

    public void registerBean(final Class<?> preInstantiatedBean, final Object bean) {
        beans.put(preInstantiatedBean, bean);
    }

}
