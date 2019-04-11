import java.util.*;
class bufferCache
{
	int numberOfHashQueues;				//done
	final bufferHeader hashQueue[];		//done
	int hashQueueSizes[];				//done
	bufferHeader freeList;				//done
	int maxFreeListSize;				//done
	int freeListSize;					//done
	final freeListEmptyEvent f = new freeListEmptyEvent();
	Random rand = new Random(); 
	bufferCache(int maxFreeListSize, int numberOfHashQueues)
	{
		this.maxFreeListSize = maxFreeListSize;
		this.numberOfHashQueues = numberOfHashQueues;
		this.freeListSize = 0;
		this.hashQueue = new bufferHeader[numberOfHashQueues];
		this.freeList = new bufferHeader(-1);
		this.freeList.freeNext = this.freeList;
		this.freeList.freePrevious = this.freeList;
		this.hashQueueSizes = new int[numberOfHashQueues];
		for(int i=0;i<numberOfHashQueues;++i)
		{

			this.hashQueue[i] = new bufferHeader(-1);
			this.hashQueue[i].hashQueueNext = this.hashQueue[i];
			this.hashQueue[i].hashQueuePrevious = this.hashQueue[i];
			this.hashQueueSizes[i] = 0;
		}
	}
	void showRow(int whichQueue)
	{

		bufferHeader b = hashQueue[whichQueue].hashQueueNext;
        b = hashQueue[whichQueue].hashQueueNext;
        if(hashQueue[whichQueue].hashQueueNext == hashQueue[whichQueue])
        	System.out.print("NULL ");
        else 
	        while(b!=hashQueue[whichQueue])
	        {
	            System.out.print(b.blockNumber+" ");
	            b = b.hashQueueNext;
	        }
	}
	void showHashQueue()
	{
		System.out.println("Hash Queues are : ");
		for(int i=0;i<numberOfHashQueues;++i)
		{
			System.out.print("HashQueue "+i+" - [ ");
			showRow(i);
			System.out.println("]");
		}
		System.out.print("Hash Queue sizes are : [ ");
		for(int i=0;i<numberOfHashQueues;++i)
			System.out.print(hashQueueSizes[i]+" ");	
		System.out.println("]");
	}
	void insertHashQueue(bufferHeader node)
    {
        int blockNumber = node.blockNumber;
        int whichQueue = blockNumber%numberOfHashQueues;
        if(hashQueueSizes[whichQueue]==0)
        {
        	hashQueue[whichQueue].hashQueueNext = node;
        	node.hashQueuePrevious = hashQueue[whichQueue];
        	node.hashQueueNext = hashQueue[whichQueue];
        	hashQueue[whichQueue].hashQueuePrevious = node;
        	hashQueueSizes[whichQueue]++;
        }
        else 
        {		
        	bufferHeader lastNode = hashQueue[whichQueue].hashQueuePrevious;
        	lastNode.hashQueueNext = node;
        	node.hashQueuePrevious = lastNode;
        	node.hashQueueNext = hashQueue[whichQueue];
        	hashQueue[whichQueue].hashQueuePrevious = node;
            hashQueueSizes[whichQueue]++;
        }
    }
    void insertFreeList(bufferHeader node)
    {
    	if(freeListSize == 0)
    	{
    		freeList.freeNext = node;
    		node.freePrevious = freeList;
    		node.freeNext = freeList;
    		freeList.freePrevious = node;
    		freeListSize++;
    	}	
    	else 
    	{
    		freeList.freePrevious.freeNext = node;
    		node.freePrevious = freeList.freePrevious;
    		node.freeNext = freeList;
    		freeList.freePrevious = node;
    		freeListSize++;
    	}
    }
    bufferHeader getHashQueueReference(int blockNumber)
    {
    	bufferHeader i = hashQueue[blockNumber%numberOfHashQueues].hashQueueNext;
    	while(i!=hashQueue[blockNumber%numberOfHashQueues])
    	{
    		if(i.blockNumber==blockNumber)
    			return i;
    		i = i.hashQueueNext;
    	}
    	return null;
    }
    bufferHeader getFreeListReference(int blockNumber)
    {
    	if(freeListSize == 0)
    		System.exit(1);
    	else
    	{
    		bufferHeader i = freeList.freeNext;
    		while(i!=freeList)
    		{
    			if(i.blockNumber == blockNumber)
    				return i;
    			i = i.freeNext;
    		}	
    	}	
    	// System.out.println("Returning null");
    	return null;
    }
    void showFreeList()
    {
    	System.out.print("Free list is : ");
    	if(freeListSize == 0)
    		System.out.println("empty [ null ]");
    	else 
    	{
    		bufferHeader i = freeList.freeNext;
    		System.out.print("[ ");
    		while(i!=freeList)
    		{
    			System.out.print(i.blockNumber+" ");
    			i = i.freeNext;
    		}	
    		System.out.println("], free list size is : "+freeListSize);
    	}
    }
    boolean presentInFreeList(int blockNumber)
    {
    	bufferHeader i = freeList.freeNext;
        if(freeListSize == 0)
            return false;
		while(i!=freeList)
		{
			if(i.blockNumber == blockNumber)
				return true;
			i = i.freeNext;
		}	
		return false;
    }
    boolean presentInHashQueue(int blockNumber)
    {
    	bufferHeader i = hashQueue[blockNumber%numberOfHashQueues].hashQueueNext;
    	while(i!=hashQueue[blockNumber%numberOfHashQueues])
    	{
    		if(i.blockNumber==blockNumber)
    			return true;
    		i = i.hashQueueNext;
    	}
    	return false;
    }
    bufferHeader takeOutFirstFreeBuffer()
    {
    	bufferHeader buffer = null;
    	if(freeList.freeNext!=freeList)
    	{
    		buffer = freeList.freeNext;
    		freeList.freeNext = freeList.freeNext.freeNext;
    		freeList.freeNext.freePrevious = freeList;
    		freeListSize--;
    		if(freeListSize==0)
    		{   
                    f.freeListEmptyNow();
            }
    	}
    	return buffer;
    }
    bufferHeader takeOutFreeBuffer(int blockNumber)
    {
    	bufferHeader buffer = null;
    	bufferHeader i = freeList.freeNext;
		while(i!=freeList)
		{
			if(i.blockNumber == blockNumber)
			{	
				buffer = i;
				break;
			}
			i = i.freeNext;
		}
		buffer.freePrevious.freeNext = buffer.freeNext;
		buffer.freeNext.freePrevious = buffer.freePrevious;
		freeListSize--;
        if(freeListSize==0)
        {        
                    f.freeListEmptyNow();
        }
		return buffer;	
    }
    bufferHeader takeOutHashQueueBuffer(int blockNumber)
    {
        bufferHeader i = hashQueue[blockNumber%numberOfHashQueues].hashQueueNext;
        while(i!=hashQueue[blockNumber%numberOfHashQueues])
        {
            if(i.blockNumber==blockNumber)
                return i;
            i = i.hashQueueNext;
        }
        return null;
    }
    bufferHeader getBlock(int blockNumber, String threadName) throws InterruptedException
    {
        System.out.println(threadName+" is aksing for blockNumber "+blockNumber);
    	bufferHeader buffer;
    	while(true)
    	{
    		if(presentInHashQueue(blockNumber))
    		{
    			System.out.println(blockNumber+" is on hashQueue");
    			buffer = getHashQueueReference(blockNumber);
                
                if(buffer.free == false && buffer.aquiredBy!= "none")
                {
                    System.out.println(blockNumber +" is not free");
                    synchronized(hashQueue)
                    {
    					while(buffer.free == false)
    					{
                            System.out.println("#########################################################");
                            System.out.println(threadName+" has to wait for ["+blockNumber+"], as lock is aquired by - "+buffer.aquiredBy);
                            System.out.println("#########################################################");
    						hashQueue.wait();
    					}  
        			}
                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                    System.out.println(threadName+"\'s wait is over ["+threadName+"] got the blockNumber "+blockNumber);
                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
    			}
    			System.out.println(blockNumber+" is free for "+threadName);
    			buffer.free = false;
    			buffer = takeOutFreeBuffer(blockNumber);
                buffer.aquiredBy = threadName;
    			return buffer;	
    		}
    		else 
    		{
                System.out.println(blockNumber+" is not in hashQueue");
                char[] animationChars = new char[]{'|', '/', '-', '\\'};
                for (int i=0;i<=100;i++)
                {
                    System.out.print("Reading blockNumber "+blockNumber+" from disk "+ i + "% " + animationChars[i % 4] + "\r");
                    Thread.sleep(2);
                }
    			if(f.freeListEmpty == true)
    			{
    				synchronized(f)
    				{
    					while(f.freeListEmpty == true)
    					{
                            System.out.println("---------------------------------------------------------");
                            System.out.println("Free List empty, wait for some block to become free");
                            System.out.println("---------------------------------------------------------");
    						f.wait();
    					}
    				}
    			}
    			buffer = takeOutFirstFreeBuffer();
    			if(buffer.delayedWrite == true)
    			{
                    kernel_do_asynchronous_write();
    			}
                bufferHeader tempNode = takeOutHashQueueBuffer(buffer.blockNumber);     //remove from old hash queue
    			insertHashQueue(new bufferHeader(blockNumber));
                buffer = getHashQueueReference(blockNumber);
                buffer.free = false;
                buffer.aquiredBy = threadName;
    			return buffer;
    		}
    	}
    }

    void kernel_do_asynchronous_write() throws InterruptedException
    {
        System.out.println("---------------------------------------------------------");
        System.out.println("Kernel Performing asynchronous write");
        char[] animationChars = new char[]{'|', '/', '-', '\\'};
        for (int i=0;i<=100;i++)
        {
            System.out.print("Asynchronous being done" + i + "% " + animationChars[i % 4] + "\r");
            Thread.sleep(10);
        }
        System.out.println("---------------------------------------------------------");
    }


    void enQueueBeginning(bufferHeader buffer)
    {
        bufferHeader firstNode = freeList.freeNext;
        freeList.freeNext = buffer;
        buffer.freePrevious = freeList;
        buffer.freeNext = firstNode;
        firstNode.freePrevious = buffer;
        freeListSize++;
        if(freeListSize == 1)
        {   
            f.freeListNotEmptyNow();
            synchronized(f)
            {
                f.notifyAll();
            }
        }
    }
    void enQueueEnd(bufferHeader buffer)
    {
        bufferHeader lastNode = freeList.freePrevious;
        lastNode.freeNext = buffer;
        buffer.freePrevious = lastNode;
        buffer.freeNext = freeList;
        freeList.freePrevious = buffer;
        freeListSize++;
        if(freeListSize == 1)
        {   
            f.freeListNotEmptyNow();
            synchronized(f)
            {
                f.notifyAll();
            }
        }
    }
    void brelease(bufferHeader buffer, String requestType, String threadName)
    {

            boolean delayedWriteOrNot[] = {true, false};
            System.out.println(threadName+" releasing buffer ["+buffer.blockNumber+"]");
            if(requestType == "write")
            {
                buffer.delayedWrite = true;
                if(buffer.delayedWrite == true)
                {
                    System.out.println("Inserting ["+buffer.blockNumber+"] to start as delayedWrite = true");
                    enQueueBeginning(buffer);
                }
                else
                {
                    System.out.println("Inserting ["+buffer.blockNumber+"] to end as delayedWrite = false");   
                    enQueueEnd(buffer);
                }
            }
            else if(requestType == "read")
            {   
                System.out.println("Inserting ["+buffer.blockNumber+"] to end as requestType - "+requestType);       
                enQueueEnd(buffer);
            }  
            buffer.free = true;
            buffer.aquiredBy = "none";
            synchronized(hashQueue)
            {
              hashQueue.notifyAll();
            }
            System.out.println(threadName+" released buffer ["+buffer.blockNumber+"]");
    }
    void intializeFreeList() throws InterruptedException
    {
        System.out.println("STARTING UP BUFFER CACHE PROJECT");
        int randomFreeListSize = rand.nextInt(3)+4;
        int randomBlockNumber;
        char[] animationChars = new char[]{'|', '/', '-', '\\'};
        for (int i=0;i<=100;i++)
        {
            System.out.print("Loading FreeList of size "+randomFreeListSize+": " + i + "% " + animationChars[i % 4] + "\r");
            Thread.sleep(10);
        }
        while(freeListSize!=maxFreeListSize)
        {
            randomBlockNumber = rand.nextInt(5)+1;
            if(!presentInFreeList(randomBlockNumber))
                insertFreeList(new bufferHeader(randomBlockNumber));
        }
    }
}


