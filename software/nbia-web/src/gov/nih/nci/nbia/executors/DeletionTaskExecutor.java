package gov.nih.nci.nbia.executors;


import org.springframework.core.task.TaskExecutor;

public class DeletionTaskExecutor {

    private TaskExecutor taskExecutor;
    private DeletionTask deletionTask;

    public DeletionTaskExecutor(TaskExecutor taskExecutor,
    		DeletionTask deletionTask) {
         this.taskExecutor = taskExecutor;
         this.deletionTask = deletionTask;
    }

    public void fire(final ImageDeletionMessage parameter) {
         taskExecutor.execute( new Runnable() {
              public void run() {
            	  deletionTask.deleteImages( parameter );
              }
         });
    }

}
