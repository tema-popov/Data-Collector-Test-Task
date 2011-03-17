package ru.yandex.collector.util;

import ru.yandex.collector.model.Job;

import java.util.*;
import java.util.regex.Pattern;

import static java.lang.Math.max;

/**
 * Created by IntelliJ IDEA.
 * User: system.29a
 * Date: 16.03.11
 * Time: 17:20
 * Description:
 */
public class Clusterizer {
    private int lastRootNumber;
    private List<Job> result;
    private static final Pattern INT_PATTERN = Pattern.compile("[1-9]+[0-9]*");

    public Clusterizer(int lastRootNumber) {
        this.lastRootNumber = lastRootNumber;
    }

    public int getLastRootNumber() {
        return lastRootNumber;
    }

    public void setLastRootNumber(int lastRootNumber) {
        this.lastRootNumber = lastRootNumber;
    }

    private boolean isSimilar(Job a, Job b) {
        double result = compareTexts(a.getDescription(), b.getDescription()) * 0.35 +
                compareTexts(a.getTitle(), b.getTitle()) * 0.45 +
                compareSalaries(a.getSalary(), b.getSalary()) * 0.2;
        return result >= 0.80;
    }

    private double compareTexts(String stringA, String stringB) {
        List<String> setA = getSetWords(stringA);
        List<String> setB = getSetWords(stringB);
        double maxCountWords = max(setA.size(), setB.size());
        double countEqualWords = 0;
        for (String each : setA) {
            if (setB.contains(each)) {
                setB.remove(each);
                countEqualWords++;
            }
        }
        return countEqualWords / maxCountWords;
    }

    private double compareSalaries(String stringA, String stringB) {
        List<String> setA = getSetSalaries(stringA);
        List<String> setB = getSetSalaries(stringB);
        double maxCountWords = max(setA.size(), setB.size());
        double countEqualWords = 0;
        for (String each : setA) {
            if (setB.contains(each)) {
                setB.remove(each);
                countEqualWords++;
            }
        }
        return countEqualWords / maxCountWords;
    }

    private List<String> getSetWords(String s) {
        List<String> set = new ArrayList<String>();
        StringTokenizer stringTokenizer = new StringTokenizer(s, "\n .,-!@#$%^&*()/\t");
        String curString;
        while (stringTokenizer.hasMoreTokens()) {
            curString = stringTokenizer.nextToken();
            if (curString.length() > 2) {
                set.add(curString);
            }
        }
        return set;
    }

    private List<String> getSetSalaries(String s) {


        List<String> set = new ArrayList<String>();
        StringTokenizer stringTokenizer = new StringTokenizer(s, "\n .,!@#$%^&*()/\t");
        String curString;
        String buffer = "";
        while (stringTokenizer.hasMoreTokens()) {
            curString = stringTokenizer.nextToken();
            if (isMatch(curString)) {
                buffer += curString;
            } else {
                if ("".equals(buffer)) {
                    set.add(curString);
                } else {
                    set.add(buffer);
                }

            }
        }
        return set;
    }

    boolean isMatch(String str) {
        return INT_PATTERN.matcher(str).matches();
    }

    public List<Job> clusterizeAll(List<Job> notClusterizedYet, List<Job> clusterized) {
        result = new ArrayList<Job>();
        for (Job each : notClusterizedYet) {
            clusterizeJob(each, clusterized);
        }
        return result;
    }

    private void clusterizeJob(Job job, List<Job> clusterized) {
        for (Job clusterizedJob : clusterized) {
            if (isSimilar(job, clusterizedJob)) {
                job.setClusterId(clusterizedJob.getClusterId());
                result.add(job);
                return;
            }
        }
        for (Job clusterizedJob : result) {
            if (isSimilar(job, clusterizedJob)) {
                job.setClusterId(clusterizedJob.getClusterId());
                result.add(job);
                return;
            }
        }
        lastRootNumber++;
        job.setClusterId(lastRootNumber);
        result.add(job);
    }
}
