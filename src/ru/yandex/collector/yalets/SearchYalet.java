package ru.yandex.collector.yalets;

import net.sf.xfresh.core.InternalRequest;
import net.sf.xfresh.core.InternalResponse;
import net.sf.xfresh.db.AbstractDbYalet;
import ru.yandex.collector.util.*;
import ru.yandex.collector.model.Job;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: system.29a
 * Date: 12.03.11
 * Time: 23:37
 * Description:
 */
public class SearchYalet extends AbstractDbYalet {

    List<Job> jobs;
    String answer = "";
    Miner miner;
    Parser parser;
    Storekeeper storekeeper;
    String query;
    int loop;
    String button;

    public void process(final InternalRequest req, final InternalResponse res) {
        storekeeper = new Storekeeper(jdbcTemplate);
        button = req.getParameter("button");
        if ("clean".equals(button)){
            storekeeper.cleanDB();
            answer = "База Очищена";
            res.add(answer);
            return;
        }
        if ("query".equals(button)){
            query = req.getParameter("queryText");
            loop = req.getIntParameter("loop");
            jobs = checkQuery();

            for (Job each: jobs){
                res.add(each);
            }
            answer = "Добавлено записей в базу данных: " + jobs.size();
            res.add(answer);
            return;
        }
        if ("base".equals(button)){
            jobs = storekeeper.getAll();
            for (Job each: jobs){
                res.add(each);
            }
            answer = "Показана вся база данных. Записей: " + jobs.size();
            res.add(answer);
            return;
        }
        if ("cluster".equals(button)){
            int numberOfRecords = 0;
            List<List<Job>> clusters = storekeeper.getClusters();
            for (List<Job> each: clusters){
                res.add(each);
                numberOfRecords += each.size();
            }
            answer = "Выявлено кластеров с повторениями: " + clusters.size() +
                    ". Всего элементов показано: " + numberOfRecords;
            res.add(answer);
        }

    }

    private List<Job> checkQuery(){
        ConfigMakerByQuery configMakerByQuery = new ConfigMakerByQuery(loop, query, "/resources/" + "miner_config_template.xml");
        configMakerByQuery.makeConfig();
        miner = new Miner("resources/", "miner_config_template.xml");
        miner.minerStart();
        parser = new Parser("resources/" + "jobsoutput.xml");
        storekeeper.putJobs(parser.parseIntoJobs());
        List<Job> jobsNotClusterized = storekeeper.getNotClusterizedJobs();
        List<Job> jobsClusterized = storekeeper.getClusterizedJobs();
        Clusterizer clusterizer = new Clusterizer(storekeeper.getLastRootNumber());
        List<Job> testResult = clusterizer.clusterizeAll(jobsNotClusterized, jobsClusterized);
        storekeeper.putJobs(testResult);
        storekeeper.setLastRootNumber(clusterizer.getLastRootNumber());
        return jobsNotClusterized;
    }

}
