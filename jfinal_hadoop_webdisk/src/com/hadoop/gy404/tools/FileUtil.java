package com.hadoop.gy404.tools;

import info.monitorenter.cpdetector.CharsetPrinter;
import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Properties;

public class FileUtil {

    public static void main(String args[]) throws IOException {
        // 源文件夹
        String url1 = "D:/user/test/";
        // 目标文件夹
        String url2 = "D:/user/testcopy/";
        // 创建目标文件夹
        (new File(url2)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(url1)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 复制文件
                String type = file[i].getName().substring(
                        file[i].getName().lastIndexOf(".") + 1);

                if (type.equalsIgnoreCase("txt"))// 转码处理
                {
                    copyFile(file[i], new File(url2 + file[i].getName()),
                            MTOServerConstants.CODE_UTF_8,
                            MTOServerConstants.CODE_GBK);
                } else {
                    copyFile(file[i], new File(url2 + file[i].getName()));
                }
            }
            if (file[i].isDirectory()) {
                // 复制目录
                String sourceDir = url1 + File.separator + file[i].getName();
                String targetDir = url2 + File.separator + file[i].getName();
                copyDirectiory(sourceDir, targetDir);
            }
        }
    }

    public static Properties getProperties(String config) throws Exception {
        Properties properties = new Properties();
        // 获取class文件夹
        ClassLoader loader = FileUtil.class.getClassLoader();
        // 加载文件
        InputStream is = loader.getResourceAsStream(config);
        if (is == null) {
            throw new Exception("properties is not found");
        }
        // 读取
        properties.load(is);
        return properties;
    }

    public static final String FILE_SEPARATOR = System.getProperties()
            .getProperty("file.separator");

    public static String getFilePrefix(String fileFullName) {
        int splitIndex = fileFullName.lastIndexOf(".");
        return fileFullName.substring(0, splitIndex);
    }

    public static String getFilePrefix(File file) {
        String fileFullName = file.getName();
        return getFilePrefix(fileFullName);
    }

    public static String getFileSuffix(String fileFullName) {
        int splitIndex = fileFullName.lastIndexOf(".");
        return fileFullName.substring(splitIndex + 1);
    }

    public static String getFileSuffix(File file) {
        String fileFullName = file.getName();
        return getFileSuffix(fileFullName);
    }

    public static String appendFileSeparator(String path) {
        return path
                + (path.lastIndexOf(File.separator) == path.length() - 1 ? ""
                : File.separator);
    }

    /**
     * 文件转化为字节数组
     */
    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1) {
                out.write(b, 0, n);
            }
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * 把字节数组保存为一个文件
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 从字节数组获取对象
     */
    public static Object getObjectFromBytes(byte[] objBytes) throws Exception {
        if (objBytes == null || objBytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        return oi.readObject();
    }

    /**
     * 从对象获取一个字节数组
     */
    public static byte[] getBytesFromObject(Serializable obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        return bo.toByteArray();
    }

    /**
     * 获取文件编码格式(字符集)
     *
     * @param file 输入文件
     * @return 字符集名称，如果不支持的字符集则返回null
     */
    public static Charset getFileEncoding(File file) {
        /*------------------------------------------------------------------------
         detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
         cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法
         加进来，如ParsingDetector、 JChardetFacade、ASCIIDetector、UnicodeDetector。
         detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
         字符集编码。
         使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar
         cpDetector是基于统计学原理的，不保证完全正确。
         --------------------------------------------------------------------------*/
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        /*-------------------------------------------------------------------------
         ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于
         指示是否显示探测过程的详细信息，为false不显示。
         ---------------------------------------------------------------------------*/
        detector.add(new ParsingDetector(false));
        /*--------------------------------------------------------------------------
         JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码
         测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以
         再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
         ---------------------------------------------------------------------------*/
        detector.add(JChardetFacade.getInstance());// 用到antlr.jar、chardet.jar
        // ASCIIDetector用于ASCII编码测定
        detector.add(ASCIIDetector.getInstance());
        // UnicodeDetector用于Unicode家族编码的测定
        detector.add(UnicodeDetector.getInstance());
        Charset charset = null;
        try {
            charset = detector.detectCodepage(file.toURI().toURL());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return charset;
    }

    /**
     * 文件编码格式转换
     *
     * @param inFile 输入文件
     * @param inCharset 输入文件字符集
     * @param outFile 输出文件
     * @param outCharset 输出文件字符集
     * @return 转码后的字符流
     * @throws IOException
     */
    public static byte[] convertFileEncoding(File inFile, Charset inCharset,
            File outFile, Charset outCharset) throws IOException {

        RandomAccessFile inRandom = new RandomAccessFile(inFile, "r");

        FileChannel inChannel = inRandom.getChannel();

        // 将输入文件的通道通过只读的权限 映射到内存中。
        MappedByteBuffer byteMapper = inChannel.map(
                FileChannel.MapMode.READ_ONLY, 0, (int) inFile.length());

        CharsetDecoder inDecoder = inCharset.newDecoder();
        CharsetEncoder outEncoder = outCharset.newEncoder();

        CharBuffer cb = inDecoder.decode(byteMapper);
        ByteBuffer outBuffer = null;
        try {
            outBuffer = outEncoder.encode(cb);

            RandomAccessFile outRandom = null;
            FileChannel outChannel = null;
            if (outFile != null) {
                try {
                    outRandom = new RandomAccessFile(outFile, "rw");
                    outChannel = outRandom.getChannel();
                    outChannel.write(outBuffer);
                } finally {
                    if (outChannel != null) {
                        outChannel.close();
                    }
                    if (outRandom != null) {
                        outRandom.close();
                    }
                }
            }
        } finally {
            inChannel.close();
            inRandom.close();
        }

        return outBuffer.array();
    }

    /**
     * 文件编码格式转换
     *
     * @param inFile 输入文件
     * @param inCharset 输入文件字符集
     * @param outCharset 输出字符集
     * @return 转码后的字符流
     * @throws IOException Exception
     */
    public static byte[] convertFileEncoding(File inFile, Charset inCharset,
            Charset outCharset) throws IOException {
        return convertFileEncoding(inFile, inCharset, (File) null, outCharset);
    }

    /**
     * 将文件字符集转化为指定字符集
     *
     * @param inFile 输入文件
     * @param outFile 输出文件
     * @param outCharset 输出文件字符集
     * @return 转码后的字符流
     * @throws IOException
     */
    public static byte[] convertFileEncoding(File inFile, File outFile,
            Charset outCharset) throws IOException {
        return convertFileEncoding(inFile, getFileEncoding(inFile), outFile,
                outCharset);
    }

    /**
     * 将文件字符集转化为指定字符集
     *
     * @param inFile 输入文件
     * @return 转码后的字符流
     * @throws IOException
     */
    public static byte[] convertFileEncoding(File inFile, Charset outCharset)
            throws IOException {
        return convertFileEncoding(inFile, (File) null, outCharset);
    }

    /**
     * 将文件字符集转换为系统字符集
     *
     * @param inFile 输入文件
     * @return 转码后的字符流
     * @throws IOException
     */
    public static byte[] convertFileEncodingToSys(File inFile)
            throws IOException {
        return convertFileEncoding(inFile, (File) null,
                Charset.defaultCharset());
    }

    /**
     * 将文件字符集转换为系统字符集
     *
     * @param inFile 输入文件
     * @param outFile 输出文件
     * @return 转码后的字符流
     * @throws IOException
     */
    public static byte[] convertFileEncodingToSys(File inFile, File outFile)
            throws IOException {
        return convertFileEncoding(inFile, outFile, Charset.defaultCharset());
    }

    /**
     * 将数据写入文件
     *
     * @param inputStream
     * @param filePath
     * @return
     */
    public static File writeFile(InputStream inputStream, String filePath) {
        FileOutputStream out = null;
        FileChannel outChannel = null;

        File file = new File(filePath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {

            out = new FileOutputStream(file);
            outChannel = out.getChannel();

            byte[] buffer = new byte[1024];

            BufferedInputStream bufferedInputStream = new BufferedInputStream(
                    inputStream);

            ByteBuffer outBuffer = ByteBuffer.allocate(buffer.length);

            while (bufferedInputStream.read(buffer) != -1) {
                outBuffer.put(buffer);
                outBuffer.flip();
                outChannel.write(outBuffer);
                outBuffer.clear();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file.exists() ? file : null;
    }

    public static String readFile(String path, String charSet) {
        if (!new File(path).exists()) {
            return null;
        }
        StringBuffer arrs = new StringBuffer();
        try {
            BufferedReader b = null;
            if (charSet != null) {
                b = new BufferedReader(new InputStreamReader(
                        new FileInputStream(path), charSet));
            } else {
                b = new BufferedReader(new InputStreamReader(
                        new FileInputStream(path)));
            }
            String s = "";
            while ((s = b.readLine()) != null) {
                arrs.append(s + System.getProperty("line.separator"));
            }
            b.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return arrs.toString();
    }

    public static void write(String fileName, String encoding, String str) {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), encoding));
            out.write(str);
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String guessEncoding(String filename) {
        try {
            CharsetPrinter charsetPrinter = new CharsetPrinter();
            String encode = charsetPrinter.guessEncoding(new File(filename));
            return encode;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String read(String fileName, String encoding) {

        String string = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), encoding));

            String str = "";
            while ((str = in.readLine()) != null) {
                string += str + "\n";
            }
            in.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return string;
    }

    // 复制文件
    public static void copyFile(File sourceFile, File targetFile)
            throws IOException {
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null) {
                inBuff.close();
            }
            if (outBuff != null) {
                outBuff.close();
            }
        }
    }

    // 复制文件夹
    public static void copyDirectiory(String sourceDir, String targetDir)
            throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(
                        new File(targetDir).getAbsolutePath() + File.separator
                        + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) { // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) { // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else { // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    private static boolean deleteDirectory(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param srcFileName
     * @param destFileName
     * @param srcCoding
     * @param destCoding
     * @throws IOException
     */
    public static void copyFile(File srcFileName, File destFileName,
            String srcCoding, String destCoding) throws IOException {// 把文件转换为GBK文件
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(
                    srcFileName), srcCoding));
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(destFileName), destCoding));
            char[] cbuf = new char[1024 * 5];
            int len = cbuf.length;
            int off = 0;
            int ret = 0;
            while ((ret = br.read(cbuf, off, len)) > 0) {
                off += ret;
                len -= ret;
            }
            bw.write(cbuf, 0, off);
            bw.flush();
        } finally {
            if (br != null) {
                br.close();
            }
            if (bw != null) {
                bw.close();
            }
        }
    }

    /**
     *
     * @param filepath
     * @throws IOException
     */
    public static void del(String filepath) throws IOException {
        File f = new File(filepath);// 定义文件路径
        if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
            if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
                f.delete();
            } else {// 若有则把文件放进数组，并判断是否有下级目录
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
                    }
                    delFile[j].delete();// 删除文件
                }
            }
        }
    }
}
