package gov.nih.nci.nbia.executors;


import org.springframework.core.task.TaskExecutor;

public class ZippingTaskExecutor {

    private TaskExecutor taskExecutor;
    private ZippingTask zippingTask;

    public ZippingTaskExecutor(TaskExecutor taskExecutor,
    		ZippingTask zippingTask) {
         this.taskExecutor = taskExecutor;
         this.zippingTask = zippingTask;
    }

    public void fire(final ImageZippingMessage parameter) {
         taskExecutor.execute( new Runnable() {
              public void run() {
            	  zippingTask.zipImages( parameter );
              }
         });
    }

}
