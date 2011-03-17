package ru.yandex.collector.model;

/**
 * Created by IntelliJ IDEA.
 * User: system.29a
 * Date: 12.03.11
 * Time: 23:21
 * Description:
 */
public class Job {
    private String title;
    private String description;
    private String link;
    private String salary;
    private boolean fullDay;
    private int clusterId = 0;

    public Job(String title, String description, String link, String salary, boolean fullDay, int clusterId){
        this.title = title;
        this.description = description;
        this.link = link;
        this.salary = salary;
        this.fullDay =  fullDay;
        this.clusterId = clusterId;
    }


    public Job(){
        fullDay = false;

    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getLink(){
        return link;
    }

    public String getSalary(){
        return salary;
    }

    public boolean getFullDay(){
        return fullDay;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setFullDay(boolean fullDay) {
        this.fullDay = fullDay;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }


    @Override
    public String toString() {
        return "Job{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", salary='" + salary + '\'' +
                ", fullDay=" + fullDay +
                '}';
    }
}
