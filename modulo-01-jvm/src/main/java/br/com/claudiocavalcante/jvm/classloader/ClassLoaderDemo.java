package br.com.claudiocavalcante.jvm.classloader;

public class ClassLoaderDemo {

        public static void main(String[] args) {

            System.out.println("==============================================");
            System.out.println(" JVM LAB - CLASS LOADERS");
            System.out.println("==============================================");

            ClassLoader applicationClassLoader =
                    ClassLoaderDemo.class.getClassLoader();

            ClassLoader platformClassLoader =
                    ClassLoader.getPlatformClassLoader();

            ClassLoader systemClassLoader =
                    ClassLoader.getSystemClassLoader();

            ClassLoader bootstrapClassLoader =
                    String.class.getClassLoader();

            ClassLoader contextClassLoader =
                    Thread.currentThread().getContextClassLoader();

            System.out.println();
            System.out.println("Application ClassLoader:");
            System.out.println(applicationClassLoader);

            System.out.println();
            System.out.println("Platform ClassLoader:");
            System.out.println(platformClassLoader);

            System.out.println();
            System.out.println("System ClassLoader:");
            System.out.println(systemClassLoader);

            System.out.println();
            System.out.println("Bootstrap ClassLoader:");
            System.out.println(bootstrapClassLoader);

            System.out.println();
            System.out.println("Thread Context ClassLoader:");
            System.out.println(contextClassLoader);

            System.out.println();
            System.out.println("ClassLoader Hierarchy:");

            System.out.println("Application -> Parent:");
            System.out.println(applicationClassLoader.getParent());

            System.out.println();

            System.out.println("Platform -> Parent:");
            System.out.println(platformClassLoader.getParent());

            System.out.println();
            System.out.println("==============================================");
        }

}
