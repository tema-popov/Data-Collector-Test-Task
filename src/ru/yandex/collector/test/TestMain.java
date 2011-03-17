package ru.yandex.collector.test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import ru.yandex.collector.util.Miner;
import ru.yandex.collector.util.Parser;
import ru.yandex.collector.model.Job;

/**
 * Created by IntelliJ IDEA.
 * User: system.29a
 * Date: 08.03.11
 * Time: 14:04
 * Description: Класс для тестирования всяких штук
 */
public class TestMain {
    private BufferedReader in;
    private List<String> buffer;
    private List<Job> jobs;
    private PrintWriter out;
    private String query = "Менеджер по продажам";
    final private String parserConfigFileName = "miner_config_template.xml";
    final private String parserConfigFileWorkDirectory = "resources/";
    final private String parserConfigFilePath = parserConfigFileWorkDirectory + parserConfigFileName;
    public static void main(String[] args) throws IOException {
        TestMain program = new TestMain();
        program.readParserConfigFile();
        program.setQueryInParserConfigFile(program.query);
        program.writeParserConfigFile();
        Miner miner = new Miner(program.parserConfigFileWorkDirectory, program.parserConfigFileName);
        miner.minerStart();
        Parser parser = new Parser(program.parserConfigFileWorkDirectory + "jobsoutput.xml");
        program.jobs = parser.parseIntoJobs();
        program.writeJobsConsole();
    }

    private void readParserConfigFile() throws IOException {
        in = new BufferedReader(new FileReader(parserConfigFilePath));
        buffer = new ArrayList<String>();
        String readLine = in.readLine();
        while (readLine != null) {
            buffer.add(readLine);
            readLine = in.readLine();
        }
        in.close();
    }

    private void setQueryInParserConfigFile(String query) {
        for (int i = 0; i < buffer.size(); i++) {
            String currentString = buffer.get(i);
            if (currentString.contains("var-def name=\"searchQuery\"")){
                buffer.set(i, "    <var-def name=\"searchQuery\">" + query + "</var-def>");
                return;
            }
        }
    }

    private void writeParserConfigFile() throws IOException{
        out = new PrintWriter(parserConfigFilePath);
        for (String each: buffer){
            out.println(each);
        }
        out.flush();
        out.close();
    }

    private void writeJobsConsole() throws IOException{
        for (Job each: jobs){
            System.out.println(each.toString());
        }
    }


}
