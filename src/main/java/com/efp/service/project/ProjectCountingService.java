package com.efp.service.project;

public class ProjectCountingService {

    private final static int MAX_OPEN_PRJ_LIMIT = 3;

    private int myOpenProjectCount = 0;

    public void incrProjectCount() {
        validateProjectCount();
        myOpenProjectCount++;
    }

    public void decrProjectCount() {
        myOpenProjectCount--;
        validateProjectCount();
    }

    public boolean projectLimitExceeded() {
        return myOpenProjectCount > MAX_OPEN_PRJ_LIMIT;
    }

    public int getMyOpenProjectCount() {
        return myOpenProjectCount;
    }

    private void validateProjectCount() {
        myOpenProjectCount = Math.max(myOpenProjectCount, 0);
    }

}
