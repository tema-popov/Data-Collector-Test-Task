package ru.yandex.collector.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.yandex.collector.model.Job;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: system.29a
 * Date: 15.03.11
 * Time: 16:44
 * Description:
 */
public class Parser {
    private File xmlFile;
    private Document xmlDocument;
    private List<Job> jobs;
    Node rootNode;


    public Parser(String fileName) {
        xmlFile = new File(fileName);
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = null;
        try {
            builder = f.newDocumentBuilder();
            xmlDocument = builder.parse(xmlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Job> parseIntoJobs() {
        NodeList jobsNodeList = xmlDocument.getElementsByTagName("job");
        ArrayList<Job> jobs = new ArrayList<Job>();
        for (int i = 0; i < jobsNodeList.getLength(); i++) {
            NodeList curJobNodeList = jobsNodeList.item(i).getChildNodes();
            Job curJob = new Job();
            for (int j = 0; j < curJobNodeList.getLength(); j++) {
                if (curJobNodeList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    String nodeName = curJobNodeList.item(j).getNodeName();
                    String content = curJobNodeList.item(j).getTextContent();
                    if ("title".equals(nodeName)) {
                        curJob.setTitle(content);
                    }

                    if ("description".equals(nodeName)) {
                        curJob.setDescription(content);
                    }

                    if ("link".equals(nodeName)) {
                        curJob.setLink(content);
                    }

                    if ("salary".equals(nodeName)) {
                        curJob.setSalary(content);
                    }

                    if ("full".equals(nodeName)) {
                        curJob.setFullDay(!content.isEmpty());
                    }
                }
            }
            jobs.add(curJob);
        }
        return jobs;
    }


}
