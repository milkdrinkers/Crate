package io.github.milkdrinkers.crate.util;

import io.github.milkdrinkers.crate.internal.provider.CrateProviders;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Class for easier, more convenient {@literal &} strait interaction with files
 */
@UtilityClass
public class FileUtils {

    // ----------------------------------------------------------------------------------------------------
    // Getting Files
    // ----------------------------------------------------------------------------------------------------

    public List<File> listFiles(@NonNull final File folder) {
        return listFiles(folder, null);
    }

    /**
     * Returns a List of Files in a folder which ends with a specific extension.
     *
     * <p>If there are no files in the folder or the folder is not found, an
     * empty list is returned instead of null.
     *
     * @param folder    Folder to search in.
     * @param extension Extension to search for. Set to null to skip extension validation.
     */
    public List<File> listFiles(
        @NonNull final File folder,
        @Nullable final String extension) {
        final List<File> result = new ArrayList<>();

        val files = folder.listFiles();

        if (files == null) {
            return result;
        }

        // if the extension is null we always add the file.
        for (val file : files) {
            if (extension == null || file.getName().endsWith(extension)) {
                result.add(file);
            }
        }

        return result;
    }

    public File getAndMake(
        @NonNull final String name,
        @NonNull final String path) {
        return getAndMake(new File(path, name));
    }

    public File getAndMake(@NonNull final File file) {
        try {
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

        } catch (final IOException ex) {
            throw CrateProviders.exceptionHandler().create(
                ex,
                "Error while creating file '" + file.getName() + "'.",
                "In: '" + getParentDirPath(file) + "'"
            );
        }
        return file;
    }

    // ----------------------------------------------------------------------------------------------------
    // Methods for handling the extension of files
    // ----------------------------------------------------------------------------------------------------

    private String getExtension(@NonNull final String path) {
        return path.lastIndexOf(".") > 0
            ? path.substring(path.lastIndexOf(".") + 1)
            : "";
    }

    public String getExtension(@NonNull final File file) {
        return getExtension(file.getName());
    }

    public String replaceExtensions(@NonNull final String fileName) {
        if (!fileName.contains(".")) {
            return fileName;
        }
        return fileName.replace("." + getExtension(fileName), "");
    }

    public String getParentDirPath(@NonNull final File file) {
        return getParentDirPath(file.getAbsolutePath());
    }

    /**
     * Since file.getParentFile can be null we created an extension function to get the path of the
     * parent file
     *
     * @param fileOrDirPath Path to file
     * @return Path to file as String
     */
    private String getParentDirPath(@NonNull final String fileOrDirPath) {
        final boolean endsWithSlash = fileOrDirPath.endsWith(File.separator);
        return fileOrDirPath
            .substring(0, fileOrDirPath.lastIndexOf(File.separatorChar, endsWithSlash
                ? fileOrDirPath.length() - 2
                : fileOrDirPath.length() - 1));
    }

    public boolean hasChanged(final File file, final long timeStamp) {
        if (file == null) {
            return false;
        }
        return timeStamp < file.lastModified();
    }

    // ----------------------------------------------------------------------------------------------------
    // Methods for reading & writing a file
    // ----------------------------------------------------------------------------------------------------

    public interface IOFunction<T> {
        void apply(T t) throws IOException;
    }

    public InputStream createInputStream(@NonNull final File file) {
        try {
            return Files.newInputStream(file.toPath());
        } catch (final IOException ex) {
            throw CrateProviders.exceptionHandler().create(
                ex,
                "Error while creating InputStream from '" + file.getName() + "'.",
                "In: '" + getParentDirPath(file) + "'");
        }
    }

    private OutputStream createOutputStream(@NonNull final File file) {
        try {
            return new FileOutputStream(file);
        } catch (final FileNotFoundException ex) {
            throw CrateProviders.exceptionHandler().create(
                ex,
                "Error while creating OutputStream from '" + file.getName() + "'.",
                "In: '" + getParentDirPath(file) + "'");
        }
    }

    /**
     * Creates a {@link Reader} for the specified file.
     *
     * @param file The file to read from.
     * @return A BufferedReader for the specified file.
     * @throws IOException If an I/O error occurs while creating the Reader.
     * @since 4.0.0
     */
    public BufferedReader createReader(@NonNull final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        final InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        return new BufferedReader(isr);
    }

    /**
     * Executes the given method with a {@link Reader} for the specified file.
     *
     * @param file   The file to read from.
     * @param method The method to execute with the Reader.
     * @throws IOException If an I/O error occurs while reading from the file.
     * @since 4.0.0
     */
    public void reader(
        @NonNull final File file,
        @NotNull final IOFunction<BufferedReader> method
    ) throws IOException {
        try (
            final BufferedReader reader = createReader(file);
        ) {
            method.apply(reader);
        }
    }

    /**
     * Creates a {@link Writer} for the specified file.
     * The file will be created if it does not exist, and overwritten if it does.
     *
     * @param file The file to write to.
     * @return A BufferedWriter for the specified file.
     * @throws IOException If an I/O error occurs while creating the Writer.
     * @since 4.0.0
     */
    public BufferedWriter createWriter(@NonNull final File file) throws IOException {
        final FileOutputStream fos = new FileOutputStream(file, false);
        final OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        return new BufferedWriter(osw);
    }

    /**
     * Executes the given method with a {@link Writer} for the specified file.
     * The file will be created if it does not exist, and overwritten if it does.
     *
     * @param file   The file to write to.
     * @param method The method to execute with the Writer.
     * @throws IOException If an I/O error occurs while writing to the file.
     * @since 4.0.0
     */
    public void writer(
        @NonNull final File file,
        @NotNull final IOFunction<BufferedWriter> method
    ) throws IOException {
        try (
            final BufferedWriter writer = createWriter(file);
        ) {
            method.apply(writer);
        }
    }

    public void write(
        @NonNull final File file,
        @NonNull final List<String> lines
    ) {
        try {
            writer(file, writer -> {
                for (final String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            });
        } catch (final IOException ex) {
            throw CrateProviders.exceptionHandler().create(
                ex,
                "Error while writing to '" + file.getName() + "'.",
                "In: '" + getParentDirPath(file) + "'");
        }
    }

    /**
     * Writes the content of the given InputStream to the specified file.
     * If the file already exists, it will append the content to the file.
     *
     * @param file         The file to write to.
     * @param inputStream  The InputStream containing the data to write.
     */
    public void writeToFile(
        @NonNull final File file,
        @NonNull final InputStream inputStream
    ) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            if (!file.exists()) {
                Files.copy(inputStream, file.toPath());
            } else {
                byte[] data = new byte[8192];
                int count;
                while ((count = inputStream.read(data, 0, 8192)) != -1) {
                    outputStream.write(data, 0, count);
                }
            }
        } catch (final IOException ex) {
            throw CrateProviders.exceptionHandler().create(
                ex,
                "Error while writing InputStream to '" + file.getName() + "'.",
                "In: '" + getParentDirPath(file) + "'");
        }
    }

    public List<String> readAllLines(@NonNull final File file) {
        try (
            final BufferedReader reader = createReader(file)
        ) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException ex) {
            throw CrateProviders.exceptionHandler().create(
                ex,
                "Error while reading '" + file.getName() + "'.",
                "In: '" + getParentDirPath(file) + "'");
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // Archiving
    // ----------------------------------------------------------------------------------------------------

    @SneakyThrows
    public void zipFile(final String sourceDirectory, final String to) {
        final File fileTo = getAndMake(new File(to + ".zip"));

        @Cleanup ZipOutputStream zipOutputStream = new ZipOutputStream(createOutputStream(fileTo));
        final Path pathFrom = Paths.get(new File(sourceDirectory).toURI());

        Files.walk(pathFrom)
            .filter(path -> !Files.isDirectory(path))
            .forEach(
                path -> {
                    val zipEntry = new ZipEntry(pathFrom.relativize(path).toString());

                    try {
                        zipOutputStream.putNextEntry(zipEntry);

                        Files.copy(path, zipOutputStream);
                        zipOutputStream.closeEntry();
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }
                });
    }

    // ----------------------------------------------------------------------------------------------------
    // Checksums
    // ----------------------------------------------------------------------------------------------------

    public String md5ChecksumAsString(@NonNull final File filename) {
        val checkSum = md5Checksum(filename);
        val result = new StringBuilder();

        for (val b : checkSum) {
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return result.toString();
    }

    private byte[] md5Checksum(@NonNull final File file) {
        try (final FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead;

            do {
                numRead = fileInputStream.read(buffer);

                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            return complete.digest();
        } catch (final IOException | NoSuchAlgorithmException ex) {
            throw CrateProviders.exceptionHandler().create(
                ex,
                "Error while creating checksum of '" + file.getName() + "'.",
                "In: '" + getParentDirPath(file) + "'");
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // Misc
    // ----------------------------------------------------------------------------------------------------

    public void extractResource(
        @NonNull final String targetDirectory,
        @NonNull final String resourcePath,
        final boolean replace
    ) {
        Valid.checkBoolean(
            !resourcePath.isEmpty(),
            "ResourcePath mustn't be empty");

        Valid.checkBoolean(
            !targetDirectory.isEmpty(),
            "Target directory mustn't be empty");

        val target = new File(targetDirectory, resourcePath);

        if (target.exists() && !replace) {
            return;
        }

        getAndMake(target);

        try (
            val inputStream = CrateProviders
                .inputStreamProvider()
                .createInputStreamFromInnerResource(resourcePath)) {

            Valid.notNull(
                inputStream,
                "The embedded resource '" + resourcePath + "' cannot be found");
            Files.copy(Objects.requireNonNull(inputStream), target.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (final IOException ioException) {
            throw CrateProviders
                .exceptionHandler()
                .create(
                    ioException,
                    "Exception while extracting file",
                    "Directory: '" + targetDirectory + "'",
                    "ResourcePath: '" + resourcePath + "'");
        }
    }

    public void extractResourceFolderContents(
        @NonNull final File sourceJarFile,
        @NonNull final File targetDirectory,
        @NonNull final String sourceDirectory,
        final boolean replace
    ) {

        if (!targetDirectory.exists()) {
            Valid.checkBoolean(
                targetDirectory.mkdirs(),
                "Can't create directory '" + targetDirectory.getName() + "'",
                "Parent: '" + getParentDirPath(targetDirectory) + "'");
        }

        Valid.checkBoolean(
            targetDirectory.isDirectory(),
            "Target directory must be an directory");

        try (final JarFile jarFile = new JarFile(sourceJarFile)) {
            for (final Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
                final JarEntry jarEntry = entries.nextElement();
                final String entryName = jarEntry.getName();

                if (entryName.startsWith(sourceDirectory) && !jarEntry.isDirectory()) {
                    extractResource(targetDirectory.getAbsolutePath(), entryName, replace);
                }
            }

        } catch (final Throwable throwable) {
            throw CrateProviders
                .exceptionHandler()
                .create(
                    throwable,
                    "Failed to extract folder from '"
                        + targetDirectory
                        + "' to '"
                        + sourceDirectory
                        + "'");
        }
    }

    private void copyFolder(
        @NonNull final File source,
        @NonNull final File destination
    ) throws IOException {

        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }

            final String[] files = source.list();

            if (files == null) {
                return;
            }

            for (val file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(destination, file);

                copyFolder(srcFile, destFile);
            }
        } else {

            @Cleanup InputStream in = createInputStream(source);
            @Cleanup OutputStream out = createOutputStream(destination);
            byte[] buffer = new byte[1024];

            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }
}
