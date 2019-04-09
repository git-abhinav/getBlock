import java.util.*;
class bufferCache
{
	int numberOfHashQueues;				//done
	bufferHeader hashQueue[];			//done
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
		System.out.println(" ]");
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
    	//must be on hashQueue
    	bufferHeader i = hashQueue[blockNumber%numberOfHashQueues].hashQueueNext;
    	while(i!=hashQueue[blockNumber%numberOfHashQueues])
    	{
    		if(i.blockNumber==blockNumber)
    			return i;
    		i = i.hashQueueNext;
    	}
    	System.out.println("Returning null");
    	return null;
    }
    bufferHeader getFreeListReference(int blockNumber)
    {
    	//make sure it must be on the freelist 
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
    	System.out.println("Returning null");
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
    			f.freeListEmptyNow();
    	}
    	if(buffer==null)
    		System.out.println("Returning null");
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
		return buffer;	
    }
    bufferHeader getBlock(int blockNumber) throws InterruptedException
    {
    	bufferHeader buffer;
    	while(true)
    	{
    		if(presentInHashQueue(blockNumber))
    		{
    			System.out.println(blockNumber+" is on hashQueue");
    			buffer = getHashQueueReference(blockNumber);
    			if(buffer.free == false)
    			{
    				System.out.println(blockNumber +" is not free");
    				synchronized(buffer)
    				{
    					while(buffer.free == false)
    					while(buffer.free == false)
    					{
    						wait();
    					}
    				}
    			}
    			System.out.println(blockNumber+" is free");
    			buffer.free = false;
    			buffer = takeOutFreeBuffer(blockNumber);
    			return buffer;	
    		}
    		else 
    		{
    			if(f.freeListEmpty == true)
    			{
    				synchronized(f)
    				{
    					while(f.freeListEmpty == true)
    					{
    						wait();
    					}
    				}
    			}
    			buffer = takeOutFreeBuffer(blockNumber);
    			if(buffer.delayedWrite == true)
    			{
    				//invoke kernel for asynchronous write
    			}
    			bufferHeader tempNode = takeOutFirstFreeBuffer();
    			insertHashQueue(new bufferHeader(blockNumber));
    			return buffer;
    		}
    	}
    }
    void brelease(int blockNumber)
    {

    }
    public static void main(String args[]) throws InterruptedException
    {
    	bufferCache b = new bufferCache(8, 4);
    	b.insertHashQueue(new bufferHeader(28));
    	b.insertHashQueue(new bufferHeader(4));
    	b.insertHashQueue(new bufferHeader(64));
    	b.insertHashQueue(new bufferHeader(17));
    	b.insertHashQueue(new bufferHeader(5));
    	b.insertHashQueue(new bufferHeader(97));
    	b.insertHashQueue(new bufferHeader(98));
    	b.insertHashQueue(new bufferHeader(50));
    	b.insertHashQueue(new bufferHeader(20));
    	b.insertHashQueue(new bufferHeader(3));
    	b.insertHashQueue(new bufferHeader(35));
    	b.insertHashQueue(new bufferHeader(99));
    	b.showHashQueue();
    	b.insertFreeList(new bufferHeader(3));
    	b.insertFreeList(new bufferHeader(5));
    	b.insertFreeList(new bufferHeader(4));
    	b.insertFreeList(new bufferHeader(28));
    	b.insertFreeList(new bufferHeader(97));
    	b.insertFreeList(new bufferHeader(10));
    	b.showFreeList();
    	System.out.println(b.getBlock(4).blockNumber);
    	b.showFreeList();
    }
}

