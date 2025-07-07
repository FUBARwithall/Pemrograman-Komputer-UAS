    package util;

    import java.io.*;

    public class Serializer {
        public static void save(Object obj, String filename) throws IOException {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(obj);
            }
        }

        public static Object load(String filename) throws IOException, ClassNotFoundException {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                return ois.readObject();
            }
        }
    }
