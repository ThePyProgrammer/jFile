package application.Model;

import javafx.stage.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;


public class File {
    private java.io.File file;
    public PrintWriter out;
    public Scanner in;
    public FileOutputStream outstream;
    private char type;
    private static FileChooser fc = new FileChooser();

    File(String filename) {
        this(filename, 'r');
    }

    File(String filename, char type) {
        this.type = type;
        file = new java.io.File(filename);
        if (type == 'r' || type == 'w' || type == 'a') {
            try {
                in = new Scanner(file);
            } catch (FileNotFoundException e) {
                in = null;
            }
        }
        if (type == 'r') {
            outstream = null;
            out = null;
        } else if (type == 'w') {
            try {
                outstream = new FileOutputStream(filename);
                out = new PrintWriter(outstream);
            } catch (FileNotFoundException e) {
                out = null;
            }
        } else if (type == 'a') {
            try {
                outstream = new FileOutputStream(filename, true);
                out = new PrintWriter(outstream);
            } catch (FileNotFoundException e) {
                out = null;
            }
        }
    }

    File(Stage stage) {
        this(getfile(stage));
    }

    File(Stage stage, char type) {
        this(getfile(stage), type);
    }

    File(File file1) {
        this(file1.getAbsolutePath());
    }

    File(java.io.File file1) {
        this(file1.getAbsolutePath());
    }

    File(File file1, char type) {
        this(file1.getAbsolutePath(), type);
    }

    File(java.io.File file1, char type) {
        this(file1.getAbsolutePath(), type);
    }

    public String getAbsolutePath() { return file.getAbsolutePath(); }
    public String getCanonicalPath() throws IOException { return file.getCanonicalPath(); }
    public String getName() { return file.getAbsolutePath(); }
    public String getPath() { return file.getPath(); }
    public String getParent() { return file.getParent(); }

    public Date lastModified() {return new Date(file.lastModified()); }
    public long lastModifiedinMS() {return file.lastModified(); }
    public long length() {return file.length(); }

    public boolean delete() { return file.delete(); }
    public boolean rename(File dest) { return file.renameTo(dest.file); }
    public boolean mkdir() { return file.mkdir(); }

    public File[] listFiles() {
        return convert(file.listFiles());
    }

    public static File[] convert(java.io.File ...files) {
        File[] rearr = new File[files.length];
        for(int i = 0; i < files.length; i++) {
            rearr[i] = new File(files[i]);
        }
        return rearr;
    }

    public boolean exists() { return file.exists(); }
    public boolean canRead() {return file.canRead(); }
    public boolean canWrite() {return file.canWrite(); }
    public boolean isDirectory() {return file.isDirectory(); }
    public boolean isFile() {return file.isFile(); }
    public boolean isAbsolute() {return file.isAbsolute(); }
    public boolean isHidden() {return file.isHidden(); }

    public static File getfile(Stage stage) {

        return convert(fc.showOpenDialog(stage))[0];
    }

    public boolean hasNext() {
        return in.hasNext();
    }

    public int count(String substring) throws IOException {
        String input = read();
        String[] words = input.split(substring);
        return words.length-1;
    }

    public int count(char substring) throws IOException {
        return count(substring+"");
    }

    public int count(int substring) throws IOException {
        return count(substring+"");
    }

    public int count(double substring) throws IOException {
        return count(substring+"");
    }

    public int count(float substring) throws IOException {
        return count(substring+"");
    }

    public String[] split(String delimeter) throws IOException {
        String input = read();
        String[] words = input.split(delimeter);
        return words;
    }

    public String read() {
        if (this.in != null) {
            Scanner inp;
            try {
                inp = new Scanner(file);
                String res = "";
                while (hasNext()) {
                    res += inp.nextLine()+'\n';
                }
                return res;
            } catch (FileNotFoundException e) {
                return "";
            }
        }
        else return "";
    }

    public String readLine() {
        if (in != null) {
            if (!hasNext()) {
                try {
                    in = new Scanner(file);
                } catch (FileNotFoundException e) {
                    in = null;
                }
            }
            return in.nextLine();
        }
        else return "";
    }



    public String[] readLines() {
        if (in != null) return read().split("\n");
        else return new String[0];
    }

    public int[] readInts(String delimeter) throws IOException {
        String[] s;
        if (delimeter == "\n") s = readLines();
        else s = readLine().split(delimeter);
        int[] rearr = new int[s.length];
        for(int i = 0; i < s.length; i++) {
            rearr[i] = Integer.parseInt(s[i].trim());
        }
        return rearr;
    }
    public int[] readInts(char delimeter) throws IOException {
        try {
            String[] s;
            if (delimeter == '\n') s = readLines();
            else s = readLine().split("" + delimeter);
            int[] rearr = new int[s.length];
            for (int i = 0; i < s.length; i++) {
                rearr[i] = Integer.parseInt(s[i].trim());
            }
            return rearr;
        } catch (Exception ex) {
            throw new IOException();
        }
    }
    public int[] readInts() throws IOException {
        try {
            String[] s = readLines();
            int[] rearr = new int[s.length];
            for (int i = 0; i < s.length; i++) {
                rearr[i] = Integer.parseInt(s[i].trim());
            }
            return rearr;
        }
        catch (Exception ex) {
            throw new IOException("");
        }
    }

    public void close() {
        try { in.close(); } catch(NullPointerException ex) {}
        try { out.close(); } catch(NullPointerException ex) {}
    }

    public java.io.File getFile() {
        return file;
    }
}

