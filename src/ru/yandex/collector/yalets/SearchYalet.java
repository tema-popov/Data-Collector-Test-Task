package ru.yandex.collector.yalets;

import net.sf.xfresh.core.InternalRequest;
import net.sf.xfresh.core.InternalResponse;
import net.sf.xfresh.db.AbstractDbYalet;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import ru.yandex.collector.util.*;
import ru.yandex.collector.model.Job;

import java.sql.ResultSet;
import java.sql.SQLException;
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
            checkQuery();
            jobs = storekeeper.getAll();
            for (Job each: jobs){
                res.add(each);
            }
            answer = "Добавлено в базу";
            res.add(answer);
            return;
        }
        if ("base".equals(button)){
            jobs = storekeeper.getAll();
            for (Job each: jobs){
                res.add(each);
            }
            answer = "Показана вся база";
            res.add(answer);
            return;
        }
        if ("cluster".equals(button)){
            jobs = storekeeper.getClusters();
            for (Job each: jobs){
                res.add(each);
            }
            answer = "Показаны похожие записи";
            res.add(answer);
            return;
        }

    }

    private void checkQuery(){
        ConfigMakerByQuery configMakerByQuery = new ConfigMakerByQuery(loop, query, "../resources/" + "miner_config_template.xml");
        configMakerByQuery.makeConfig();
        miner = new Miner("../resources/", "miner_config_template.xml");
        miner.minerStart();
        parser = new Parser("../resources/" + "jobsoutput.xml");
        storekeeper.putJobs(parser.parseIntoJobs());
        List<Job> jobsNotClusterized = storekeeper.getNotClusterizedJobs();
        List<Job> jobsClusterized = storekeeper.getClusterizedJobs();
        Clusterizer clusterizer = new Clusterizer(storekeeper.getLastRootNumber());
        List<Job> testResult = clusterizer.clusterizeAll(jobsNotClusterized, jobsClusterized);
        storekeeper.putJobs(testResult);
        storekeeper.setLastRootNumber(clusterizer.getLastRootNumber());
    }

}
