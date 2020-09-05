import javafx.stage.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class File extends java.io.File implements Cloneable, AutoCloseable {
    public PrintWriter out = null;
    public Scanner in = null;
    public FileOutputStream outstream = null;
    private char type;

    // JavaFX Integration
    private static FileChooser fc = new FileChooser();
    private static DirectoryChooser dc = new DirectoryChooser();

    // Ensuring only a singular in, out exists per file
    private static HashMap<String, Scanner> scanners = new HashMap<>();
    private static HashMap<String, PrintWriter> appendwriters = new HashMap<>();
    private static HashMap<String, PrintWriter> overwriters = new HashMap<>();
    private static HashMap<String, FileOutputStream> writeoutstreams = new HashMap<>();
    private static HashMap<String, FileOutputStream> appendoutstreams = new HashMap<>();

    // Extension Filters
    public static FileChooser.ExtensionFilter PNG = new FileChooser.ExtensionFilter("Portable Newtwork Graphics (PNG) Files", "*.png"),
            ICO = new FileChooser.ExtensionFilter("ICO Files", "*.ico"),
            JPG = new FileChooser.ExtensionFilter("JPEG Files", "*.jpg", "*.jpeg"),
            GIF = new FileChooser.ExtensionFilter("Graphics Interchange Format (GIF) Files", "*.gif"),
            JFIF = new FileChooser.ExtensionFilter("JPEG File Interchange Format (JFIF) Files", "*.jfif"),
            PDF = new FileChooser.ExtensionFilter("Portable Document Format (PDF) Files", "*.pdf"),
            HTML = new FileChooser.ExtensionFilter("HyperText Markup Language (HTML) Files", "*.htm", "*.html"),
            XHTML = new FileChooser.ExtensionFilter("Extensible HyperText Markup Language (XHTML) Files", "*.xhtml"),
            PHTML = new FileChooser.ExtensionFilter("PHP HyperText Markup Language (PHTML) Files", "*.phtml"),
            XML = new FileChooser.ExtensionFilter("Extensible Markup Language (XML) Files", "*.xml"),
            FXML = new FileChooser.ExtensionFilter("JavaFX XML Files", "*.fxml"),
            PHP = new FileChooser.ExtensionFilter("PHP Files", "*.php", "*.php5"),
            CSS = new FileChooser.ExtensionFilter("Cascading Style Sheets (CSS) Files", "*.css"),
            JS = new FileChooser.ExtensionFilter("JavaScript (JS) Files", "*.js", "*.jsm"),
            JSON = new FileChooser.ExtensionFilter("JSON Files", "*.json"),
            JAVA = new FileChooser.ExtensionFilter("Java Files", "*.java"),
            KOTLIN = new FileChooser.ExtensionFilter("Kotlin Files", "*.kt"),
            SCALA = new FileChooser.ExtensionFilter("Scala Files", "*.scala"),
            PYTHON = new FileChooser.ExtensionFilter("Python Files", "*.py", "*.pyc", "*.pyw"),
            RUBY = new FileChooser.ExtensionFilter("Ruby Files", "*.rb", "*.rbw", "*.rake", "*.rbx"),
            C = new FileChooser.ExtensionFilter("C Files", "*.c", "*.h", "*.idc"),
            CPP = new FileChooser.ExtensionFilter("C++ Files", "*.cpp", "*.hpp", "*.c++", "*.h++", "*.cc", "*.hh", "*.cxx", "*.hxx", "*.C", "*.H", "*.cp", "*.cpp"),
            CSHARP = new FileChooser.ExtensionFilter("C# Files", "*.cs"),
            INO = new FileChooser.ExtensionFilter("Arduino Files", "*.ino"),
            VB = new FileChooser.ExtensionFilter("Visual Basic Files", "*.vbs"),
            GO = new FileChooser.ExtensionFilter("Golang Files", "*.go"),
            RUST = new FileChooser.ExtensionFilter("Rust Files", "*.rs"),
            SWIFT = new FileChooser.ExtensionFilter("Swift Files", "*.swift"),
            TEXT = new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.text"),
            MD = new FileChooser.ExtensionFilter("Markdown (MD) Files", "*.md"),
            README = new FileChooser.ExtensionFilter("README Files", "*.README"),
            BIBTEX = new FileChooser.ExtensionFilter("BibTeX Files", "*.bib"),
            LATEX = new FileChooser.ExtensionFilter("LaTeX Files", "*.tex", "*.aux", "*.toc"),
            SQL = new FileChooser.ExtensionFilter("Structured Query Language (SQL) Files", "*.sql");

    // Setting Initial Directory
    static {
        fc.setInitialDirectory(new java.io.File(System.getProperty("user.dir")));
        dc.setInitialDirectory(new java.io.File(System.getProperty("user.dir")));
    }


    // Constructors
    public File(String filename) {
        this(filename, 'r');
    }

    public File(String filename, char type) {
        super(filename);
        try {
            if (!exists()) createNewFile();
        } catch(IOException ex) {}
        this.type = type;
        if(!isDirectory()) {
            if ((type == 'r' || type == 'w' || type == 'a') && Files.isReadable(toPath())) {
                if(scanners.containsKey(getAbsolutePath())) in = scanners.get(getAbsolutePath());
                else {
                    try { in = new Scanner(this); scanners.put(getAbsolutePath(), in); } catch (IOException e) {}
                }
            }
            if (type == 'w' && Files.isWritable(toPath())) {
                if(writeoutstreams.containsKey(getAbsolutePath()) && overwriters.containsKey(getAbsolutePath())) {
                    outstream = writeoutstreams.get(getAbsolutePath());
                    out = overwriters.get(getAbsolutePath());
                } else {
                    try {
                        outstream = new FileOutputStream(filename);
                        out = new PrintWriter(outstream);
                        writeoutstreams.put(getAbsolutePath(), outstream);
                        overwriters.put(getAbsolutePath(), out);
                    } catch (IOException e) {}
                }
            } else if (type == 'a' && Files.isWritable(toPath())) {
                if(appendoutstreams.containsKey(getAbsolutePath()) && appendwriters.containsKey(getAbsolutePath())) {
                    outstream = appendoutstreams.get(getAbsolutePath());
                    out = appendwriters.get(getAbsolutePath());
                } else {
                    try {
                        outstream = new FileOutputStream(filename, true);
                        out = new PrintWriter(outstream);
                        appendoutstreams.put(getAbsolutePath(), outstream);
                        appendwriters.put(getAbsolutePath(), out);
                    } catch (IOException e) {}
                }
            }
        }
    }

    public File() { this('r'); }
    public File(char type) { this(getFile(), type); }

    public File(java.io.File file) { this(file.getAbsolutePath()); }
    public File(java.io.File file, char type) { this(file.getAbsolutePath(), type); }

    public File(URL url) { this(url.getFile()); }
    public File(URL url, char type) { this(url.getFile(), type); }

    public File(URI uri) throws MalformedURLException { this(uri.toURL().getFile()); }
    public File(URI uri, char type) throws MalformedURLException { this(uri.toURL().getFile(), type); }

    public static File read(String filename) { return new File(filename); }
    public static File read(File file) { return new File(file); }
    public static File read(java.io.File file) { return new File(file); }
    public static File read(URI uri) throws MalformedURLException { return new File(uri); }
    public static File read(URL url) { return new File(url); }

    public static File write(String filename) { return new File(filename, 'w'); }
    public static File write(File file) { return new File(file, 'w'); }
    public static File write(java.io.File file) { return new File(file, 'w'); }
    public static File write(URI uri) throws MalformedURLException { return new File(uri, 'w'); }
    public static File write(URL url) { return new File(url, 'w'); }

    public static File append(String filename) { return new File(filename, 'a'); }
    public static File append(File file) { return new File(file, 'a'); }
    public static File append(java.io.File file) { return new File(file, 'a'); }
    public static File append(URI uri) throws MalformedURLException { return new File(uri, 'a'); }
    public static File append(URL url) { return new File(url, 'a'); }


    // Methods
    public String getName() { return getAbsolutePath(); }
    public File getAbsoluteFile() { return new File(super.getAbsoluteFile()); }
    public File getParentFile() { return new File(super.getParentFile()); }
    public File getCanonicalFile() {
        try { return new File(super.getCanonicalFile()); } catch (IOException e) { return null; }
    }

    public boolean rename(File dest) { return renameTo(dest); }

    public File[] listFiles() { return convert(listFiles()); }

    public String getRelativePath() { return new File(System.getProperty("user.dir")).relativize(this); }
    public String getRelativePath(String directory) { return new File(directory).relativize(this); }
    public String getRelativePath(File directory) { return directory.relativize(this); }
    public String getRelativePath(java.io.File directory) { return directory.toURI().relativize(this.toURI()).getPath(); }
    public String getRelativePath(URI directory) { return directory.relativize(this.toURI()).getPath(); }
    public String getRelativePath(URL directory) throws URISyntaxException { return directory.toURI().relativize(this.toURI()).getPath(); }

    public static File[] convert(java.io.File ...files) { return convert(Arrays.asList(files)); }

    public static File[] convert(Collection<java.io.File> files) {
        File[] rearr = new File[files.size()];
        int i = 0;
        for(java.io.File file: files) {
            rearr[i] = new File(file);
            i++;
        }
        return rearr;
    }

    public static File getFile(Stage stage) { return convert(fc.showOpenDialog(stage))[0]; }
    public static File[] getFiles(Stage stage) { return convert(fc.showOpenMultipleDialog(stage)); }

    public static File getFile() { return getFile(new Stage()); }
    public static File[] getFiles() { return getFiles(new Stage()); }

    public static File getFile(Stage stage, FileChooser.ExtensionFilter...extensions) { return getFile(stage, Arrays.asList(extensions)); }

    public static File getFile(Stage stage, Collection<FileChooser.ExtensionFilter> extensions) {
        fc.getExtensionFilters().addAll(extensions);
        File file = getFile(stage);
        fc.getExtensionFilters().clear();
        return file;
    }

    public static File getFile(FileChooser.ExtensionFilter...extensions) { return getFile(new Stage(), extensions); }
    public static File getFile(Collection<FileChooser.ExtensionFilter> extensions) { return getFile(new Stage(), extensions); }

    public static File[] getFiles(Stage stage, FileChooser.ExtensionFilter...extensions) { return getFiles(stage, Arrays.asList(extensions)); }

    public static File[] getFiles(Stage stage, Collection<FileChooser.ExtensionFilter> extensions) {
        fc.getExtensionFilters().addAll(extensions);
        File[] files = getFiles(stage);
        fc.getExtensionFilters().clear();
        return files;
    }

    public static File[] getFiles(FileChooser.ExtensionFilter...extensions) { return getFiles(new Stage(), extensions); }
    public static File[] getFiles(Collection<FileChooser.ExtensionFilter> extensions) { return getFiles(new Stage(), extensions); }


    public static File getPNG(Stage stage) { return getFile(stage, PNG); }
    public static File[] getPNGs(Stage stage) { return getFiles(stage, PNG); }
    public static File getPNG() { return getFile(PNG); }
    public static File[] getPNGs() { return getFiles(PNG); }


    public static File getJPG(Stage stage) { return getFile(stage, JPG); }
    public static File[] getJPGs(Stage stage) { return getFiles(stage, JPG); }
    public static File getJPG() { return getFile(JPG); }
    public static File[] getJPGs() { return getFiles(JPG); }


    public static File getICO(Stage stage) { return getFile(stage, ICO); }
    public static File[] getICOs(Stage stage) { return getFiles(stage, ICO); }
    public static File getICO() { return getFile(ICO); }
    public static File[] getICOs() { return getFiles(ICO); }


    public static File getGIF(Stage stage) { return getFile(stage, GIF); }
    public static File[] getGIFs(Stage stage) { return getFiles(stage, GIF); }
    public static File getGIF() { return getFile(GIF); }
    public static File[] getGIFs() { return getFiles(GIF); }


    public static File getJFIF(Stage stage) { return getFile(stage, JFIF); }
    public static File[] getJFIFs(Stage stage) { return getFiles(stage, JFIF); }
    public static File getJFIF() { return getFile(JFIF); }
    public static File[] getJFIFs() { return getFiles(JFIF); }


    public static File getPDF(Stage stage) { return getFile(stage, PDF); }
    public static File[] getPDFs(Stage stage) { return getFiles(stage, PDF);}
    public static File getPDF() { return getFile(PDF); }
    public static File[] getPDFs() { return getFiles(PDF);}


    public static File getHTML(Stage stage) { return getFile(stage, HTML); }
    public static File[] getHTMLs(Stage stage) { return getFiles(stage, HTML);}
    public static File getHTML() { return getFile(HTML); }
    public static File[] getHTMLs() { return getFiles(HTML);}


    public static File getXHTML(Stage stage) { return getFile(stage, XHTML); }
    public static File[] getXHTMLs(Stage stage) { return getFiles(stage, XHTML);}
    public static File getXHTML() { return getFile(XHTML); }
    public static File[] getXHTMLs() { return getFiles(XHTML);}


    public static File getPHTML(Stage stage) { return getFile(stage, PHTML); }
    public static File[] getPHTMLs(Stage stage) { return getFiles(stage, PHTML);}
    public static File getPHTML() { return getFile(PHTML); }
    public static File[] getPHTMLs() { return getFiles(PHTML);}


    public static File getXML(Stage stage) { return getFile(stage, XML); }
    public static File[] getXMLs(Stage stage) { return getFiles(stage, XML);}
    public static File getXML() { return getFile(XML); }
    public static File[] getXMLs() { return getFiles(XML);}


    public static File getFXML(Stage stage) { return getFile(stage, FXML); }
    public static File[] getFXMLs(Stage stage) { return getFiles(stage, FXML);}
    public static File getFXML() { return getFile(FXML); }
    public static File[] getFXMLs() { return getFiles(FXML);}


    public static File getPHP(Stage stage) { return getFile(stage, PHP); }
    public static File[] getPHPs(Stage stage) { return getFiles(stage, PHP);}


    public static File getCSS(Stage stage) { return getFile(stage, CSS); }
    public static File[] getCSSs(Stage stage) { return getFiles(stage, CSS);}


    public static File getJS(Stage stage) { return getFile(stage, JS); }
    public static File[] getJSs(Stage stage) { return getFiles(stage, JS);}


    public static File getJSON(Stage stage) { return getFile(stage, JSON); }
    public static File[] getJSONs(Stage stage) { return getFiles(stage, JSON);}


    public static File getJAVA(Stage stage) { return getFile(stage, JAVA); }
    public static File[] getJAVAs(Stage stage) { return getFiles(stage, JAVA);}


    public static File getKOTLIN(Stage stage) { return getFile(stage, KOTLIN); }
    public static File[] getKOTLINs(Stage stage) { return getFiles(stage, KOTLIN);}


    public static File getSCALA(Stage stage) { return getFile(stage, SCALA); }
    public static File[] getSCALAs(Stage stage) { return getFiles(stage, SCALA);}


    public static File getPYTHON(Stage stage) { return getFile(stage, PYTHON); }
    public static File[] getPYTHONs(Stage stage) { return getFiles(stage, PYTHON);}


    public static File getRUBY(Stage stage) { return getFile(stage, RUBY); }
    public static File[] getRUBYs(Stage stage) { return getFiles(stage, RUBY);}


    public static File getC(Stage stage) { return getFile(stage, C); }
    public static File[] getCs(Stage stage) { return getFiles(stage, C);}


    public static File getCPP(Stage stage) { return getFile(stage, CPP); }
    public static File[] getCPPs(Stage stage) { return getFiles(stage, CPP);}


    public static File getCSHARP(Stage stage) { return getFile(stage, CSHARP); }
    public static File[] getCSHARPs(Stage stage) { return getFiles(stage, CSHARP);}


    public static File getINO(Stage stage) { return getFile(stage, INO); }
    public static File[] getINOs(Stage stage) { return getFiles(stage, INO);}


    public static File getVB(Stage stage) { return getFile(stage, VB); }
    public static File[] getVBs(Stage stage) { return getFiles(stage, VB);}


    public static File getGO(Stage stage) { return getFile(stage, GO); }
    public static File[] getGOs(Stage stage) { return getFiles(stage, GO);}


    public static File getRUST(Stage stage) { return getFile(stage, RUST); }
    public static File[] getRUSTs(Stage stage) { return getFiles(stage, RUST);}


    public static File getSWIFT(Stage stage) { return getFile(stage, SWIFT); }
    public static File[] getSWIFTs(Stage stage) { return getFiles(stage, SWIFT);}


    public static File getTEXT(Stage stage) { return getFile(stage, TEXT); }
    public static File[] getTEXTs(Stage stage) { return getFiles(stage, TEXT);}


    public static File getMD(Stage stage) { return getFile(stage, MD); }
    public static File[] getMDs(Stage stage) { return getFiles(stage, MD);}


    public static File getREADME(Stage stage) { return getFile(stage, README); }
    public static File[] getREADMEs(Stage stage) { return getFiles(stage, README);}


    public static File getBIBTEX(Stage stage) { return getFile(stage, BIBTEX); }
    public static File[] getBIBTEXs(Stage stage) { return getFiles(stage, BIBTEX);}


    public static File getLATEX(Stage stage) { return getFile(stage, LATEX); }
    public static File[] getLATEXs(Stage stage) { return getFiles(stage, LATEX);}


    public static File getSQL(Stage stage) { return getFile(stage, SQL); }
    public static File[] getSQLs(Stage stage) { return getFiles(stage, SQL);}

    public static File getPHP() { return getFile(PHP); }
    public static File[] getPHPs() { return getFiles(PHP);}


    public static File getCSS() { return getFile(CSS); }
    public static File[] getCSSs() { return getFiles(CSS);}


    public static File getJS() { return getFile(JS); }
    public static File[] getJSs() { return getFiles(JS);}


    public static File getJSON() { return getFile(JSON); }
    public static File[] getJSONs() { return getFiles(JSON);}


    public static File getJAVA() { return getFile(JAVA); }
    public static File[] getJAVAs() { return getFiles(JAVA);}


    public static File getKOTLIN() { return getFile(KOTLIN); }
    public static File[] getKOTLINs() { return getFiles(KOTLIN);}


    public static File getSCALA() { return getFile(SCALA); }
    public static File[] getSCALAs() { return getFiles(SCALA);}


    public static File getPYTHON() { return getFile(PYTHON); }
    public static File[] getPYTHONs() { return getFiles(PYTHON);}


    public static File getRUBY() { return getFile(RUBY); }
    public static File[] getRUBYs() { return getFiles(RUBY);}


    public static File getC() { return getFile(C); }
    public static File[] getCs() { return getFiles(C);}


    public static File getCPP() { return getFile(CPP); }
    public static File[] getCPPs() { return getFiles(CPP);}


    public static File getCSHARP() { return getFile(CSHARP); }
    public static File[] getCSHARPs() { return getFiles(CSHARP);}


    public static File getINO() { return getFile(INO); }
    public static File[] getINOs() { return getFiles(INO);}


    public static File getVB() { return getFile(VB); }
    public static File[] getVBs() { return getFiles(VB);}


    public static File getGO() { return getFile(GO); }
    public static File[] getGOs() { return getFiles(GO);}


    public static File getRUST() { return getFile(RUST); }
    public static File[] getRUSTs() { return getFiles(RUST);}


    public static File getSWIFT() { return getFile(SWIFT); }
    public static File[] getSWIFTs() { return getFiles(SWIFT);}


    public static File getTEXT() { return getFile(TEXT); }
    public static File[] getTEXTs() { return getFiles(TEXT);}


    public static File getMD() { return getFile(MD); }
    public static File[] getMDs() { return getFiles(MD);}


    public static File getREADME() { return getFile(README); }
    public static File[] getREADMEs() { return getFiles(README);}


    public static File getBIBTEX() { return getFile(BIBTEX); }
    public static File[] getBIBTEXs() { return getFiles(BIBTEX);}


    public static File getLATEX() { return getFile(LATEX); }
    public static File[] getLATEXs() { return getFiles(LATEX);}


    public static File getSQL() { return getFile(SQL); }
    public static File[] getSQLs() { return getFiles(SQL);}

    public static File getDirectory(Stage stage) { return convert(dc.showDialog(stage))[0]; }

    public boolean hasNext() { return in.hasNext(); }

    public int count(String substring) {
        String input = read();
        String[] words = input.split(substring);
        return words.length-1;
    }

    public int count(char substring) { return count(substring+""); }

    public int count(int substring) { return count(substring+""); }

    public int count(double substring) { return count(substring+""); }

    public int count(float substring) { return count(substring+""); }

    public String[] split(String delimeter) {
        String input = read();
        String[] words = input.split(delimeter);
        return words;
    }

    public String read() {
        try {
            return new String(Files.readAllBytes(Paths.get(getAbsolutePath())));
        } catch (IOException e) {}
        return "";
    }

    public String readLine() {
        if(in != null) {
            if(hasNext()) return in.nextLine();
            else {
                try { Scanner inp = new Scanner(this); return inp.nextLine(); } catch (IOException e) {}
            }
        } return "";
    }

    public String[] readLines() {
        List<String> lines = Collections.emptyList();
        try { lines = Files.readAllLines(Paths.get(getAbsolutePath()), StandardCharsets.UTF_8); } catch (IOException e) {}
        return lines.toArray(new String[lines.size()]);
    }

    public void close() {
        try { in.close(); } catch(NullPointerException ex) {}
        try { out.close(); } catch(NullPointerException ex) {}
    }

    public String relativize(File file) { return toURI().relativize(file.toURI()).getPath(); }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if(obj == null || !(obj instanceof File)) return false;
        return getAbsolutePath().equals(((File) obj).getAbsolutePath());
    }

    public boolean equals(File other) { return getAbsolutePath().equals(other.getAbsolutePath()); }

    public File clone() { return new File(getAbsolutePath()); }

    public int compareTo(File o) { return super.compareTo(o); }
}
