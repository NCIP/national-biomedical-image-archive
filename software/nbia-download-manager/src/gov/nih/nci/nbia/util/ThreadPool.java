/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Java Thread Pool
 * 
 * This is a thread pool that for Java, it is
 * simple to use and gets the job done. This program and
 * all supporting files are distributed under the Limited
 * GNU Public License (LGPL, http://www.gnu.org).
 * 
 * This is the main class for the thread pool. You should
 * create an instance of this class and assign tasks to it.
 * 
 * For more information visit http://www.jeffheaton.com.
 * 
 * @author Jeff Heaton (http://www.jeffheaton.com)
 * @version 1.0
 */
public class ThreadPool { 
    /**
     * The threads in the pool.
     */
    protected Thread threads[] = null;
    /**
     * The backlog of assignments, which are waiting
     * for the thread pool.
     */
    Collection<Runnable> assignments = new ArrayList<Runnable>();
    /**
     * The is similar to assignments except that it will be removed
     * from the pool after it's complete execution. This is used to fire 
     * event after all threads in the pool are done.
     */
    Collection<Runnable> tempAssignments = new ArrayList<Runnable>();
    /**
     * A Done object that is used to track when the
     * thread pool is done, that is has no more work
     * to perform.
     */
    protected Done done = new Done(); 
    private boolean paused = false;

    /**
     * The constructor.
     *  
     * @param size  How many threads in the thread pool.
     */
    public ThreadPool(int size) {
        threads = new WorkerThread[size];
        for (int i=0;i<threads.length;i++) {
            threads[i] = new WorkerThread(this);
            threads[i].start();
       }
    } 

    /**
     * Add a task to the thread pool. Any class
     * which implements the Runnable interface
     * may be assigned. When this task runs, its
     * run method will be called.
     * 
     * @param r   An object that implements the Runnable interface
     */
    public synchronized void assign(Runnable r)	{
        paused = false;
        done.workerBegin();
        assignments.add(r);
        tempAssignments.add(r); 
        notify();
    }
    public synchronized boolean isPaused(){
        return paused;
    }

    /**
     * Get a new work assignment.
     * 
     * @return A new assignment
    */
    public synchronized Runnable getAssignment(){
        try {
            while ( !assignments.iterator().hasNext() ){
                wait();
            }
            Runnable r = (Runnable)assignments.iterator().next();
            assignments.remove(r);
            return r;
        } catch (InterruptedException e) {
            done.workerEnd();
            return null;
        }
    }

    /**
     * Called to block the current thread until
     * the thread pool has no more work.
     */
    public void complete(){
        done.waitBegin();
        done.waitDone();
    }

    public synchronized void pause(){
        assignments.clear();
        tempAssignments.clear();
        paused = true;
    }

    protected void finalize(){
        done.reset();
        for (int i=0;i<threads.length;i++) {
            threads[i].interrupt();
            done.workerBegin();
            threads[i].destroy();
        }
        done.waitDone();
    }

    public void addThreadPoolListener(ThreadPoolListener tpl ){
        listeners.add(tpl);
    }
    private List<ThreadPoolListener> listeners= new ArrayList<ThreadPoolListener>();
    public void fireUpdateEvent(){
        for(ThreadPoolListener listener: listeners){
            listener.update();
        }
    }
}

/**
 * The worker threads that make up the thread pool.
 * 
 * @author Jeff Heaton
 * @version 1.0
 */
class WorkerThread extends Thread {
    /**
     * True if this thread is currently processing.
     */
    public boolean busy;
    /**
     * The thread pool that this object belongs to.
     */
    public ThreadPool owner;

    /**
     * The constructor.
     * 
     * @param o the thread pool 
     */
    WorkerThread(ThreadPool o){
        owner = o;
    }

    /**
     * Scan for and execute tasks.
     */
    public void run(){
        Runnable target = null;
        do {
            target = owner.getAssignment();
            if (target!=null) {
                target.run();      
                owner.done.workerEnd();
                fireListener(target);
            }
        } while (target!=null);
    }

    private void fireListener(Runnable target){
        if(owner.tempAssignments.contains(target)){
            owner.tempAssignments.remove((target));
        }
        if(!owner.isPaused() && owner.tempAssignments.size() == 0){
            owner.fireUpdateEvent();
        }
    }
}