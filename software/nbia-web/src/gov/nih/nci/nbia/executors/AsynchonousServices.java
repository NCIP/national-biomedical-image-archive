package gov.nih.nci.nbia.executors;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import org.springframework.core.task.TaskExecutor;


public class AsynchonousServices {

	public static void performImageDeletion(ImageDeletionMessage parameter){
		TaskExecutor taskExecutor=(TaskExecutor)SpringApplicationContext.getBean("taskExecutor");
		DeletionTask task = new DeletionTask();
		DeletionTaskExecutor deletionTaskExecutor = new DeletionTaskExecutor(taskExecutor, task);
		deletionTaskExecutor.fire(parameter);
		
	}

	public static void performImageZipping(ImageZippingMessage parameter){
		TaskExecutor taskExecutor=(TaskExecutor)SpringApplicationContext.getBean("taskExecutor");
		ZippingTask task = new ZippingTask();
		ZippingTaskExecutor zippingTaskExecutor = new ZippingTaskExecutor(taskExecutor, task);
		zippingTaskExecutor.fire(parameter);
		
	}
	
}
