import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

import static java.nio.file.FileVisitResult.*;

/**
 * Created by tony on 1/16/17.
 */

public class DirectoryCleaner extends SimpleFileVisitor<Path> {
    private static int deletionCriteriaAge;
    private static ArrayList fileSHAHolder = new ArrayList();

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
//        deleteOld(file, attr);
        try {
            checkCopy(file);
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }

    public void setDeletionCriteriaAge(int deletionCriteriaAge) {
        this.deletionCriteriaAge = deletionCriteriaAge;
    }

    private void deleteOld(Path path, BasicFileAttributes attr) throws IOException {
        long ageOfFile = attr.lastModifiedTime().toInstant().until(Instant.now(), ChronoUnit.DAYS);//Number of days between today and last modified day of file
        if(ageOfFile >= deletionCriteriaAge) {
            Files.delete(path);
            System.out.println("File: " + path.getFileName() + " has been deleted.");
        }
    }

    private void checkCopy(Path path) throws NoSuchAlgorithmException, IOException{
        byte[] currentFileSHA = computeSHA1(path);
        if(fileSHAHolder.size() == 0) {
            fileSHAHolder.add(currentFileSHA);
        } else {
            boolean isCopy = false;
            for(int i = 0; i < fileSHAHolder.size(); i++) {
                if(Arrays.equals(currentFileSHA, (byte[]) fileSHAHolder.get(i))) {
                    isCopy = true;
                }
            }

            if(isCopy) {
                Files.delete(path);
                System.out.println("File: " + path.getFileName() + " has been deleted.");
            } else {
                fileSHAHolder.add(currentFileSHA);
            }
        }
    }

    private byte[] computeSHA1(Path path) throws NoSuchAlgorithmException, IOException {
        MessageDigest fileDigest = MessageDigest.getInstance("SHA-1");
        fileDigest.update(Files.readAllBytes(path));
        byte[] fileSHA = fileDigest.digest();
        return fileSHA;
    }
}