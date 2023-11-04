package core.di.factory;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;


// DI를 하고 인스턴스를 생성하는 역할
public class ConstructorInjector extends AbstractInjector {
    private static final Logger log = LoggerFactory.getLogger(ConstructorInjector.class);

    public ConstructorInjector(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    Set<?> getInjectedBeans(Class<?> clazz) {
        return Sets.newHashSet();
    }

    @Override
    Class<?> getBeanClass(Object injectedBean) {
        return null;
    }

    @Override
    void inject(Object injectedBean, Object bean, BeanFactory beanFactory) {

    }
}
