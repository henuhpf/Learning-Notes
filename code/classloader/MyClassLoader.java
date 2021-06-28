import java.io.*;

public class MyClassLoader extends ClassLoader {
    private static String CLASSPATH;

    /**
     * protected ClassLoader(ClassLoader parent) {
     *     this(checkCreateClassLoader(), parent);
     * }
     */
    public MyClassLoader(String classpath) {
        super(ClassLoader.getSystemClassLoader());
        this.CLASSPATH = classpath;
    }

    public Class findClass(String name) {
        byte[] b = loadClassData(name);
        System.out.println(b);
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassData(String name) {
        // load the class data from the connection
        name = name.replace(".", File.separator);
        System.out.println(CLASSPATH + name + ".class");
        try (FileInputStream inputStream = new FileInputStream(CLASSPATH + name + ".class");
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
            int byteCount = 0;
            while((byteCount = inputStream.read()) != -1){
                byteArrayOutputStream.write(byteCount);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
