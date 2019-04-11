import java.util.*;
class myThread extends Thread
{
	static bufferCache b;
	String threadName;
	static myThread kernel_reference;
	static Scanner sc = new Scanner(System.in);
	static Random rand = new Random();
	static boolean dontGiveUpLock = false;
	myThread(String threadName)
	{
		this.threadName = threadName;
	}
	void kernel_set_parameter(int maxFreeListSize, int numberOfHashQueues)
	{
		if(threadName == "kernel")
			b = new bufferCache(maxFreeListSize, numberOfHashQueues);
		else
			System.out.println(threadName+" don't have the privilages to set these parameters");
	}
	void save_kernel_reference()
	{
		this.kernel_reference = this;
	}
	void intializeFreeList() throws InterruptedException
	{
		b.intializeFreeList();
	}
    void showHashQueue()
    {
    	if(this.threadName == "kernel")
    		b.showHashQueue();
    	else 
    		System.out.println("Ask kernel to print HQ");
    }
    void showFreeList()
    {
    	if(this.threadName == "kernel")
			kernel_reference.b.showFreeList();
    	else 
    		System.out.println("Ask kernel to print FL");
    }
    public void run() 
    {
    	System.out.println("Running "+this.threadName);
    	try
    	{
	    	int blockNumber;
	    	blockNumber = rand.nextInt(5)+1;
	    	String typeOpertionString[] = {"read", "write"};
	    	int typeOperationInt = rand.nextInt(2);
	    	System.out.println(threadName+"'s request type is : "+typeOpertionString[typeOperationInt]);
	    	bufferHeader retreivedBuffer = kernel_reference.b.getBlock(blockNumber, this.threadName);
	    	System.out.println(threadName+" performing I/O with blockNumber ["+blockNumber+"]");
    		if(dontGiveUpLock == false) 
    			kernel_reference.b.brelease(retreivedBuffer, typeOpertionString[typeOperationInt], this.threadName);
    		else {
    			sleep(1000);
    			kernel_reference.b.brelease(retreivedBuffer, typeOpertionString[typeOperationInt], this.threadName);
    		}

	    }
	    catch(Exception e){}
    }
}