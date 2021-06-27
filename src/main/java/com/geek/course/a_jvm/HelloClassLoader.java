package com.geek.course.a_jvm;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class
 * 文件所有字节（x=255-x）处理后的文件。文件群里提供
 */
public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Class<?> helloCLazz = new HelloClassLoader().findClass("Hello");
        Method hello = helloCLazz.getMethod("hello");
        hello.setAccessible(true);
        hello.invoke(helloCLazz.newInstance());
    }

    @Override
    protected Class<?> findClass(String name) {
        try {
            byte[] hello = Files
                    .readAllBytes(Paths.get(Objects.requireNonNull(this.getResource("Hello.xlass")).toURI()));
            for (int i = 0; i < hello.length; i++) {
                hello[i] = (byte) (255 - hello[i]);
            }
            //u8编码下有乱码，请问这部分乱码是什么?
            System.out.println(new String(hello));
            return defineClass(name, hello, 0, hello.length);
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException(e);
        }

    }
}
