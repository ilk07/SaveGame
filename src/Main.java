import java.io.*;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        GameProgress progress1 = new GameProgress(100, 12, 3, 250.91);
        GameProgress progress2 = new GameProgress(49, 19, 9, 1071.12);
        GameProgress progress3 = new GameProgress(1, 90, 37, 10018.00);

        saveGame("C:/Users/Мишка/Desktop/Games/savegames/save1.dat", progress1);
        saveGame("C:/Users/Мишка/Desktop/Games/savegames/save2.dat", progress2);
        saveGame("C:/Users/Мишка/Desktop/Games/savegames/save3.dat", progress3);

        //архивируем файлы
        zipFiles("C:/Users/Мишка/Desktop/Games/savegames/zip.zip",
                "C:/Users/Мишка/Desktop/Games/savegames/save3.dat",
                "C:/Users/Мишка/Desktop/Games/savegames/save2.dat",
                "C:/Users/Мишка/Desktop/Games/savegames/save1.dat"
        );

        //удаляем файлы вне архива из папки
        File dir = new File("C:/Users/Мишка/Desktop/Games/savegames/");
        if (dir.isDirectory()) {
            for (File item : dir.listFiles()) {
                if (item.isFile() && getFileExtension(item).equals("dat")) {
                    item.delete();
                }
            }
        }
    }

    public static boolean isAbs(String path) {
        return Path.of(path).isAbsolute();
    }

    public static String getFileExtension(File file) {
        String ext = "";
        int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            ext = file.getName().substring(i + 1);
        }
        return ext;
    }

    public static void zipFiles(String arcPath, String... v) { //архивация списка

        try (ZipOutputStream zout = new ZipOutputStream(
                new FileOutputStream(arcPath)
        )) {
            for (String filePath : v) {
                File file = new File(filePath);

                //TODO а как узнать есть ли файл?

                FileInputStream fis = new FileInputStream(filePath);
                ZipEntry entry = new ZipEntry(file.getName());
                zout.putNextEntry(entry);

                //считываем содержимое файла в массив byte
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                //добавляем содержимое к архиву
                zout.write(buffer);
                zout.closeEntry();
                fis.close();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void saveGame(String absolutePath, GameProgress gameProgress) {
        if (isAbs(absolutePath)) { //если путь абсолютный
            try (
                    FileOutputStream fos = new FileOutputStream(absolutePath);
                    ObjectOutputStream oos = new ObjectOutputStream(fos)
            ) {
                oos.writeObject(gameProgress);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}