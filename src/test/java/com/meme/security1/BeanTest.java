package com.meme.security1;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class BeanTest {
	@Autowired
    DefaultListableBeanFactory bf;

    @Test
    public void beans(){
        for(String n : bf.getBeanDefinitionNames()){
            System.out.println(n + " \t" + bf.getBean(n).getClass().getName());
        }
    }
}