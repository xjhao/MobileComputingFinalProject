package edu.wpi.zqinxhao.playerpooling.DB;

public class DynamoDBManagerTaskResult {

    private DynamoDBManagerType taskType;
    private String tableStatus;

    public boolean isTaskSuccess() {
        return taskSuccess;
    }

    public void setTaskSuccess(boolean taskSuccess) {
        this.taskSuccess = taskSuccess;
    }

    private boolean taskSuccess = true;

    public DynamoDBManagerType getTaskType() {
        return taskType;
    }

    public void setTaskType(DynamoDBManagerType taskType) {
        this.taskType = taskType;
    }

    public String getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

}
