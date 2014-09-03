package tv.floe.canova.formatters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by mjk on 9/1/14.
 */
public abstract class LibSVMInputFormat<T> extends BaseInputFormat<T> {


    @Override
    public abstract T read(InputStream is, String delim) throws IOException;
    public T read(InputStream is) throws IOException {
        return read(is, SPACE);
    }

    /**
     * Returns a ConcurrentSkpListMap of a libSVM entry
     * @param is
     * @param recordDelim -- usually a new line, here you can specify
     * @param splitDelim -- for libSVM it's a colon but you can specify here
     * @return
     * @throws IOException
     */
    public static Map<Integer,Double> parseRead(InputStream is, String recordDelim, String splitDelim, Map<Integer,Double> ret) throws IOException {


        String l = readLine(recordDelim,is);

        String[] parts = l.split(SPACE);

        int classIndex = Integer.parseInt(parts[0]);
        double classIndex_d = (double)classIndex;
        ret.put(0, classIndex_d);

        for (int x = 1; x < parts.length; x++) {
            String[] feature = parts[x].split(splitDelim);
            int index = Integer.parseInt(feature[0]);
            double val = Double.parseDouble(feature[1]);
            ret.put(index,val);
        }
        return ret;

    }
    protected Map<Integer,Double> parseRead(InputStream is, Map<Integer,Double> ret) throws IOException {
        return parseRead(is,DELIM_NEWLINE, DELIM_COLON, ret);
    }

    /**
     * Read inputstream until delim is encountered; single byte character for now.
     * @param delim -- a single byte character that demarcates the stream
     * @param is
     * @return
     * @throws IOException
     */
    protected static String readLine(String delim, InputStream is) throws IOException {
        String ret = new String();
        String everything = new String(".*");
        int c;
        while ( (c = is.read()) != -1 ) {
            char ch = (char) c;
            if (ch == (delim.charAt(0))) {
                break;
            }
            ret += new String(String.valueOf(ch));
        }
        return ret;
    }

    @Override
    public abstract T read(String is, String delim) throws IOException;
    public  T read(String is) throws IOException {
        return read(is, COMMA);
    }


    @Override
    public abstract T read(File file, String delim) throws IOException;
    public T read(File file) throws IOException {
        return read(file, COMMA);
    }
    protected Map<Integer,Double> fileReader(FileInputStream file, String recordDelim, String splitDelim,Map<Integer,Double> ret) throws IOException {
        return parseRead(file, recordDelim, splitDelim,ret);
    }

}