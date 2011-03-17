package ru.yandex.collector.util;

import ru.yandex.collector.model.Job;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: system.29a
 * Date: 15.03.11
 * Time: 23:46
 * Description:
 */
public class ConfigMakerByQuery {
    private BufferedReader in;
    private int loop;
    private List<String> buffer;
    private PrintWriter out;
    private String query;
    private String parserConfigFileName = "miner_config_template.xml";

    public ConfigMakerByQuery(int loop, String query, String parserConfigFileName) {
        this.loop = loop;
        this.query = query;
        this.parserConfigFileName = parserConfigFileName;

    }

    public void makeConfig() {
        readParserConfigFile();
        setQueryInParserConfigFile(loop, query);
        writeParserConfigFile();
    }

    private void readParserConfigFile() {
        try {
            in = new BufferedReader(new FileReader(parserConfigFileName));
            buffer = new ArrayList<String>();
            String readLine = in.readLine();
            while (readLine != null) {
                buffer.add(readLine);
                readLine = in.readLine();
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setQueryInParserConfigFile(int loop, String query) {
        for (int i = 0; i < buffer.size(); i++) {
            String currentString = buffer.get(i);
            if (currentString.contains("var-def name=\"searchQuery\"")) {
                buffer.set(i, "    <var-def name=\"searchQuery\">" + query + "</var-def>");
            }
            if (currentString.contains("call-param name=\"maxloops")) {
                buffer.set(i, "\t       \t<call-param name=\"maxloops\">" + loop + "</call-param>");
            }

        }
    }

    private void writeParserConfigFile() {
        try {
            out = new PrintWriter(parserConfigFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (String each : buffer) {
            out.println(each);
        }
        out.flush();
        out.close();
    }

}
