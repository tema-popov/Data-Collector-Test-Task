package ru.yandex.collector.util;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import ru.yandex.collector.model.Job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: system.29a
 * Date: 15.03.11
 * Time: 18:47
 * Description:
 */
public class Storekeeper {
    private SimpleJdbcTemplate jdbcTemplate;
    private ShowJobsMapper mapper = new ShowJobsMapper();

     private static class ShowJobsMapper implements ParameterizedRowMapper<Job> {
        public Job mapRow(final ResultSet rs, final int i) throws SQLException {
            return new Job(rs.getString("title"), rs.getString("description"), rs.getString("link"),
                    rs.getString("salary"), (rs.getInt("full_day") ==  1), rs.getInt("cluster_id"));

        }
    }

    public Storekeeper(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void cleanDB() {
        jdbcTemplate.getJdbcOperations().execute("TRUNCATE TABLE all_jobs");
        setLastRootNumber(0);
    }

    public List<Job> getNotClusterizedJobs() {
        List<Job> jobsQuery;
        jobsQuery = jdbcTemplate.getJdbcOperations().query("select * from all_jobs where cluster_id = 0 order by title", mapper);
        return jobsQuery;
    }

    public List<Job> getClusterizedJobs() {
        List<Job> jobsQuery;
        jobsQuery = jdbcTemplate.getJdbcOperations().query("select * from all_jobs where cluster_id <> 0 order by title", mapper);
        return jobsQuery;
    }

    public List<Job> getAll() {
        List<Job> jobsQuery;
        jobsQuery = jdbcTemplate.getJdbcOperations().query("select * from all_jobs order by title", mapper);
        return jobsQuery;
    }

    public List<List<Job>> getClusters() {
        List<Job> jobsClusters;
        jobsClusters = jdbcTemplate.getJdbcOperations().query("select * from all_jobs " +
                "GROUP BY cluster_id " +
                "having count(cluster_id) > 1" +
                " order by cluster_id", mapper);
        List<List<Job>> jobsQuery = new ArrayList<List<Job>>();
        for (Job job: jobsClusters){
            jobsQuery.add(jdbcTemplate.getJdbcOperations().query("select * from all_jobs " +
                "where cluster_id = '" + job.getClusterId() + "'" +
                "order by title", mapper));
        }
        return jobsQuery;
    }

    public void putJobs(List<Job> jobs) {
        for (Job each : jobs) {
            String changedDsc = each.getDescription();
            jdbcTemplate.getJdbcOperations().execute("DELETE FROM all_jobs WHERE link = '" + each.getLink() + "' ");
            jdbcTemplate.getJdbcOperations().execute("INSERT INTO all_jobs (`title` ," +
                    "`description` ," +
                    "`link` ," +
                    "`salary` ," +
                    "`full_day`, " +
                    "`cluster_id`) " +
                    "VALUES('" + each.getTitle() +
                    "', '" + each.getDescription().replace("\'","\'\'") +
                    "', '" + each.getLink() +
                    "', '" + each.getSalary() +
                    "', '" + ((each.getFullDay()) ? "1" : "0") +
                    "', '" + each.getClusterId() + "')");
        }

    }

    public int getLastRootNumber(){
        return jdbcTemplate.getJdbcOperations().queryForInt("select var_value from var where var_name = 'last_cluster'");
    }

    public void setLastRootNumber(int lastRootNumber){
        jdbcTemplate.getJdbcOperations().execute("UPDATE var SET var_value = " + lastRootNumber +
                " WHERE var_name = 'last_cluster' ");
    }
}
