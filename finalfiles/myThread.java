import java.util.*;         //for random and scanner 
class freelistempty         //for handling the last case
{
    // this class helps us get notified when 
    // the status of freelist changes from empty 
    // atleast one node free in the list.
    boolean empty;
    boolean getEmptyStatus()
    {
        // this function returns the status of the 
        // status 
        return this.empty;
    }
    void someBlockIsFreeNow()
    {
        // this function will be called when when the kernel 
        // notifies the process that wake up someBlock 'x' is 
        // free now 
        this.empty = false;
    }
    void freeListEmptyNow()
    {
        // this function will make threads to go to sleep 
        // when the freelist is empty.
        this.empty = true;
    }
}
class criticalSection      //for handling the buffer busy case 
{
    // this class is defined to simulate the behaviour of the 
    // critical section when multiple threads are requesting 
    // for same block

    boolean freeYesOrNo;       // block is free or not
    String lockAttainedBy = ""; 
    boolean getYesOrNo()
    {
        // this function returns the 
        // status of the block (free or not free)
        return this.freeYesOrNo;
    }
    void makeitfree()
    {
        // make that node free for other threads to use
        System.out.println("free now ");
        this.lockAttainedBy = "";
        this.freeYesOrNo = true;
    }
    String getLockAttainedBy()
    {
        return this.lockAttainedBy;
    }
    void makeitbusy(String threadName)
    {
        // make other threads go to sleep 
        // that are requesting for the same block
        this.lockAttainedBy = threadName;
        System.out.println("busy now ");
        this.freeYesOrNo = false;
    }
}
class bufferHeader{          // normal buffer header class
                             // function names are quite self explanatory
    public 
        bufferHeader freeNext;                  
        bufferHeader freePrevious;
        bufferHeader hashQueueNext;
        bufferHeader hashQueuePrevious;
        // the normal pointer for managing the data structure 
        // as mentioned in AOS books  

        boolean free;        // is requesting block free or not  
        boolean delayedWrite;// true = first write to disk else ignore
        int blockNumber;     // blocknumber of the bufferheader
    public
        bufferHeader(int blockNumber) // normal 1 parameter constructor 
        {
            this.blockNumber = blockNumber;
            this.free = true;
            this.delayedWrite = false;
            freeNext = freePrevious = hashQueueNext = hashQueuePrevious = null;
        }
        


        // function for setting the next and previous pointer of the hash queue and 
        // free list
        void setFreePointers(bufferHeader previous, bufferHeader next)
        {
            this.freePrevious = previous;
            this.freeNext = next;
        }
        void setHashPointers(bufferHeader previous, bufferHeader next)
        {
            this.hashQueueNext = previous;
            this.hashQueuePrevious = next;
        }

        // get the pointer set of the buffernode (part of freelist or hashqueue)
        // index 0 -> previous 
        // index 1 -> next 
        bufferHeader[] getFreePointers()
        {
            bufferHeader bh[] = new bufferHeader[2];
            bh[0] = this.freePrevious;
            bh[1] = this.freeNext;
            return bh;
        }
        bufferHeader[] getHashPointers()
        {
            bufferHeader bh[] = new bufferHeader[2];
            bh[0] = this.hashQueuePrevious;
            bh[1] = this.hashQueueNext;
            return bh;
        }
}
class bufferCache{              //buffer cache class which is going to be used by the 
                                //thread class
    public  
        bufferHeader hashQueue[] = new bufferHeader[4]; // 4 hash queues 
        bufferHeader freeList = new bufferHeader(-1);   // -1 means that header is just used as the head
        int freeListSize;       //denoting size
        //for handling freelist empty case
        final freelistempty f = new freelistempty();
        int hashQueueSizes[] = new int[4];
        //for handling the buffer busy case
        final criticalSection cs = new criticalSection();   
        myThread kernel_reference;
    public 
        Random rand = new Random();     //for random freelist generation
        bufferCache()                   //working fine 
        {
            this.freeListSize = 0;
            for(int i=0;i<4;++i)
            {    
                this.hashQueue[i] = new bufferHeader(-1);
                this.hashQueueSizes[i] = 0;
            }
        }
        void store_kernel_reference(myThread kernel_reference)
        {
            this.kernel_reference = kernel_reference;
        }           
        void showRow(int whichQueue)    //this function show hash queue row by row 
        {
            bufferHeader b = hashQueue[whichQueue].hashQueueNext;
            System.out.print("");
            b = hashQueue[whichQueue].hashQueueNext;
            while(b!=hashQueue[whichQueue])
            {
                System.out.print(b.blockNumber+" ");
                b = b.hashQueueNext;
            }
        }
                                        //this function inserts a node to the hashqueue
        void insertHashQueue(int whichQueue, bufferHeader node)
        {
            if(hashQueueSizes[whichQueue] == 0)
            {
                hashQueue[whichQueue].setHashPointers(node, node);
                node.setHashPointers(hashQueue[whichQueue],hashQueue[whichQueue]);
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
                                        //inserts in the free list 
        void insertInFreeList(int blockNumber)
        {
            bufferHeader newHeader = new bufferHeader(blockNumber);
            if(freeListSize == 0)
            {
                freeList.setFreePointers(newHeader, newHeader);
                newHeader.setFreePointers(freeList, freeList);
                ++freeListSize;
            }
            else if(freeListSize < 20)
            {
                bufferHeader lastNode[] = new bufferHeader[2];
                lastNode = freeList.getFreePointers();
                bufferHeader FreeListlastNode = lastNode[0];         //previosu at the 0th index.
                FreeListlastNode.setFreePointers(FreeListlastNode.freePrevious, newHeader);
                newHeader.setFreePointers(FreeListlastNode, freeList);
                freeList.setFreePointers(newHeader, freeList.freeNext);
                ++freeListSize;
            }
            else
            {
                // else throw exception 
                throw new IllegalArgumentException("Max Free List size attained");
            }
        }
        void showFreeList()
        {
            // this function show the freelist 
            // null when size is zero
            bufferHeader startNode = freeList.freeNext;
            bufferHeader lastNode = freeList.freeNext;
            bufferHeader i = startNode;
            System.out.print("Free list is ("+freeListSize+") [ ");
            if(freeListSize==0)
            {
                System.out.print("null ");
            }
            while(i!=freeList)
            {
                System.out.print(i.blockNumber+" ");
                i = i.freeNext;
            }
            System.out.print("]");
            System.out.print("\nSize of freelist is - "+freeListSize);
            System.out.println("");
        }
        boolean inFreeList(int blockNumber)
        {
            // this function is used to check whether the block number 
            // is in the free list or not 
            bufferHeader tempNode = freeList.freeNext;
            while(tempNode!=freeList)
            {
                if(blockNumber == tempNode.blockNumber)
                {    
                    System.out.println(blockNumber+" Found in free list");
                    return true;
                }
                tempNode = tempNode.freeNext;
            }
            System.out.println("Not found in free list");
            return false;
        }
        boolean presentIn(int arr[], int item, int size)
        {
            // this function is used to find whether the 'int item'
            // is present in 'int arr[]' of size 'size', it is used 
            // to create a random list of non repeating numbers
            for(int i=0;i<size;++i)
                if(arr[i] == item)
                    return true;
            return false;
        }
        int[] generateRandomUnique()
        {
            // this function returns a random array of size 16 
            // with no repetition 
            int arr[] = new int[16];
            int r;
            for(int i=0;i<16;++i)
            {
                if(i==0)
                    arr[i] = rand.nextInt(50);
                else 
                {
                    r = rand.nextInt(50);
                    while(presentIn(arr, r, i+1))
                        r = rand.nextInt(50);
                    arr[i] = r;
                }
            }
            System.out.println("Initial env created");
            return arr;
        }
        void populateFreeList(boolean mapToHashQueue)     //init step and should be called at the 
                                    //beginning only
        
        {
            int randomUnique[] = generateRandomUnique();
            // to simulate the initial boot up buffer.
            for(int i=0;i<16;++i)
            {    
                // r = rand.nextInt(50); ////////////////////////////
                insertInFreeList(randomUnique[i]);     // random number from 0-49//   
                                         ////////////////////////////
            }
            System.out.println("Mapping env on queues");
            if(mapToHashQueue)
                mapFreeListOnHashQueues();
            for(int i=0;i<4;++i)
            {
                System.out.print("Showing row - "+i+"[ ");
                showRow(i);   
                System.out.println("]");
            }
        }
        bufferHeader getFreeNode(int blockNumber)
        {
            //this functions returns the reference of blocknumber
            //from the freelist and deletes the node from the freelist.
            bufferHeader tempNode = freeList.freeNext;
            while(tempNode!=freeList)
            {
                if(tempNode.blockNumber == blockNumber)
                {
                    bufferHeader p = tempNode.freePrevious;
                    bufferHeader n = tempNode.freeNext;
                    p.freeNext = n;
                    n.freePrevious = p;
                    freeListSize--;
                    System.out.println("Taking out node "+tempNode.blockNumber+" from freelist");
                    return tempNode;
                }
                tempNode = tempNode.freeNext;
            }
            return null;
        }
        bufferHeader getFirstFreeNode()
        {
            //this function return the reference of the first node from the 
            //free list and deletes it from the freelist 
            bufferHeader firstNode = freeList.freeNext;
            if(freeListSize == 0)
                throw new IllegalArgumentException("Free List to Empty hai");
            else if(freeListSize == 1)
                freeList.setFreePointers(freeList, freeList);
            else
            {
                freeList.freeNext = firstNode.freeNext;
                firstNode.freeNext.freePrevious = freeList;
            }
            freeListSize--;
            System.out.println("Node "+firstNode.blockNumber+" retrieved");
            return firstNode;
        }
        void mapFreeListOnHashQueues()  //map the new gernerated freelist on to
                                        //the 4 of the hashqueues
        {
            bufferHeader startNode = freeList.freeNext;
            bufferHeader i = startNode;
            while(i!=freeList)
            {
                insertHashQueue(((i.blockNumber)%4), i);
                i = i.freeNext;
            }
        }
        void showHashQueues()
        {
            // this function is callled to show the 
            // hash queue row by row 
            System.out.println("Printing Hash Queues");
            for(int i=0;i<4;++i)
            {
                System.out.print("Hash Queue "+i+"   ");    
                showRow(i);
                System.out.println("");
            }
            System.out.println("Hash Queue sizes are : ");
            for (int i=0;i<4;++i)
                System.out.print(hashQueueSizes[i]+" ");
            System.out.println("");
        }
        boolean inHashQueue(int whichQueue, int blockNumber)
        {
            // this function checks whether the block number 
            // is present in the hashqueue 
            bufferHeader b = hashQueue[whichQueue].hashQueueNext;
            while(b!=hashQueue[whichQueue])
            {
                if(b.blockNumber==blockNumber)
                {
                    // System.out.println(blockNumber+" in hash queue "+ whichQueue);
                    return true;
                }
                b = b.hashQueueNext;
            }
            // System.out.println(blockNumber+" not in hash queue "+ whichQueue);
            return false;
        }
        void addInHashQueue(int whichQueue, bufferHeader node)
        {
            // this functions adds the bloclnumber(binded in form of bufferheader) 
            // to the hashqueue 
            bufferHeader tempNode = hashQueue[whichQueue].hashQueueNext;
            if(hashQueueSizes[whichQueue] == 0)
            {
                hashQueue[whichQueue].setHashPointers(node, node);
                node.setHashPointers(hashQueue[whichQueue], hashQueue[whichQueue]);
                hashQueueSizes[whichQueue]++;
                System.out.println("Added in hashqueue "+whichQueue);
            }
            else 
            {
                bufferHeader lastNode = hashQueue[whichQueue].hashQueuePrevious;
                lastNode.hashQueueNext = node;
                node.hashQueuePrevious = lastNode;
                hashQueue[whichQueue].hashQueuePrevious = node;
                node.hashQueueNext = hashQueue[whichQueue];
                hashQueueSizes[whichQueue]++;
                System.out.println("Added in hashqueue "+whichQueue);
            }
        }
        boolean isFreeListEmpty()
        {
            // returns true false depending upon 
            // whether the freelist is empty or not
            if(freeListSize == 0)
                return true;
            else 
                return false;
        }
        void insertAtEnd(int whichQueue, bufferHeader node)
        {
            // this functon adds the buffernode to the end of the hashqueue
            bufferHeader lastNode = hashQueue[whichQueue].hashQueuePrevious;
            lastNode.hashQueueNext = node;
            node.hashQueuePrevious = lastNode;
            node.hashQueueNext = hashQueue[whichQueue];
            hashQueue[whichQueue].hashQueuePrevious = node;
            node.freeNext = null;
            node.freePrevious = null;
        }
        void deleteFreeNode(bufferHeader node)
        {
            // called when we need to delete a node from the feelist or hashqueue
            System.out.println("Deleting freelist node "+node.blockNumber);
            node = null;
        }
        void makeNodeBusy(int blockNumber, String threadName)
        {
            // this function is called to make a node 
            // busy that is tell other threads that someother 
            // thread is currently executing the node
            // this function alos uses the synchronized block
            // and 'cs' is final static variable that is used 
            // among all the concurrently executing threads
            int whichQueue = blockNumber%4;
            bufferHeader node = hashQueue[whichQueue].hashQueueNext;
            while(node!=hashQueue[whichQueue])
            {
                if(node.blockNumber == blockNumber)
                {    
                    node.free = false;
                    System.out.println(node.blockNumber+" busy now");
                    synchronized(cs){cs.makeitbusy(threadName);}
                }
                node = node.hashQueueNext;
            }
        }
        bufferHeader getHashRef(int blockNumber)
        {
            // this function returns the reference of blocknumber
            // from the hashqueue 
            int whichQueue = blockNumber%4;
            bufferHeader node = hashQueue[whichQueue].hashQueueNext;
            while(node!=hashQueue[whichQueue])
            {
                if(node.blockNumber == blockNumber)
                    return node;
                node = node.hashQueueNext;
            }
            return null;
        }
        int getFreeListSize()
        {
            // returns the size of the freelist 
            return this.freeListSize;
        }
        void releaseBuffer(int blockNumber, String threadName)             //formally knowns as
                                                        //brelse
        {
            //this function is used to release a buffer that is to 
            //set the status of the cs to free 
            //and notify 'all' the threads that are waiting for that 
            //buffer to become free
            //wake up - notifyAll()
            System.out.println(threadName+" releasing buffer");
            final bufferHeader node = getHashRef(blockNumber);
            System.out.println("Kernel is performing");
            if(freeListSize == 0)
            {
                synchronized(f)
                {
                    bufferHeader lastFreeNode = freeList.freePrevious;
                    lastFreeNode.freeNext = node;
                    node.freePrevious = lastFreeNode;
                    node.freeNext = freeList;
                    freeList.freePrevious = node;
                    ++freeListSize;
                    System.out.println("Free list size now "+freeListSize);
                    f.empty = false;
                    f.notifyAll();
                }
            }
            synchronized(cs)
            {   
                if(node.delayedWrite == true)
                {
                    System.out.println("Marked delayed adding to the head of the freelist");
                    bufferHeader firstFreeNode = freeList.freeNext;
                    freeList.freeNext = node;
                    node.freePrevious = freeList;
                    node.freeNext = firstFreeNode;
                    firstFreeNode.freePrevious = node;
                    freeListSize = freeListSize + 1;
                    node.free = true;
                    cs.makeitfree();
                    cs.notifyAll();
                }
                else 
                {
                    
                    System.out.println("Not marked delayed write adding to the tail of the freelist");
                    bufferHeader lastFreeNode = freeList.freePrevious;
                    lastFreeNode.freeNext = node;
                    node.freePrevious = lastFreeNode;
                    node.freeNext = freeList;
                    freeList.freePrevious = node;
                    freeListSize++;
                    cs.makeitfree();
                    cs.notifyAll();
                }
            }
        }
        void addDelayedWrite(int blockNumber)
        {
            //this function is used to add 
            //delayed write to a node 
            //so that next time the kernel
            //will first write tha block to 
            //the disk
            int whichQueue = blockNumber%4;
            bufferHeader node = hashQueue[whichQueue].hashQueueNext;
            while(node!=hashQueue[whichQueue])
            {
                if(node.blockNumber == blockNumber)
                {
                    node.delayedWrite = true;
                    return;
                }
                node = node.hashQueueNext;
            }
        }
        bufferHeader getFirstNodeRefOnly()
        {
            //get reference only remarks not delete it
            return freeList.freeNext;
        }
        void kernel_show_message(String message) throws InterruptedException
        {
            // System.out.println(message);
            // System.out.println("Wait for few seconds kernel is writing to disk");
            // Thread.sleep(3);
            // System.out.println("Wait over kernel_reference done it's part(i.e. writing to disk)");
        }
        void blockRequest(int blockNumber, String threadName) throws InterruptedException
        {
            //this function is the main function 
            //that is called to request a buffer
            //a thread calls this function when 
            //it needs to request a block
            System.out.println("Thread"+threadName+" Requesting for "+blockNumber);
            if(isFreeListEmpty()==false)
            {
                System.out.println("Free list not empty");
                if(inHashQueue(blockNumber%4, blockNumber))
                {
                    if(inFreeList(blockNumber))             //case1 REMARKS FINE!
                    {
                        bufferHeader tempNode = getFirstNodeRefOnly();
                        if(tempNode.blockNumber == -1)
                        {
                            System.out.println("Error has occured");
                            System.exit(1);
                        }
                        if(tempNode.delayedWrite == false)
                        {
                            System.out.println(blockNumber+" in free list");
                            bufferHeader node = getFreeNode(blockNumber);
                            deleteFreeNode(node);
                        }
                        else 
                        {
                            deleteFreeNode(tempNode);
                            System.out.println("The BLOCK (" +tempNode.blockNumber+ ") was marked dalayed write se writing it first");
                            System.out.println("KERNEL WRITING THE DISK BLOCK TO THE DISK FIRST");
                            //storing data to disk simulated 
                            kernel_reference.kernel_show_message("This is done by kernel thread only");
                            insertHashQueue(blockNumber%4, new bufferHeader(blockNumber));
                            System.out.println("Thread"+threadName+" got the block");
                        }
                    }
                    else 
                    {
                        final bufferHeader ref = getHashRef(blockNumber);
                        synchronized(cs)
                        {
                            if(ref.free == false)                //just read
                            {
                                System.out.println(threadName+" Waiting for "+blockNumber+" to be free");
                                while(cs.getYesOrNo() == false)
                                {
                                    System.out.println("Waiting");
                                    System.out.println("Thread"+threadName+" has to wait as lock for block number "+blockNumber+" is already attained by, thread "+cs.getLockAttainedBy());
                                    cs.wait();
                                    
                                }
                                cs.makeitfree();
                                System.out.println("Thread"+threadName+"\'s Wait over finally");
                            }
                        }
                    }
                }
                else                                         
                {
                    //get first node
                    System.out.println(blockNumber+" not in hashqueue");
                    System.out.println("Getting a the first node from the freelist");
                    bufferHeader node = getFirstFreeNode();
                    System.out.println("------------------");
                    if(node.delayedWrite == true)          //case2
                    {
                        deleteFreeNode(node);
                        System.out.println("The BLOCK (" +node.blockNumber+ ") was marked dalayed write se writing it first");
                        System.out.println("KERNEL WRITING THE DISK BLOCK TO THE DISK FIRST");
                        insertHashQueue(blockNumber%4, new bufferHeader(blockNumber));
                        System.out.println("Thread"+threadName+" got the block");
                    }
                    else                                   //case3
                    {
                        System.out.println("The node was not  marked dalayed write");
                        deleteFreeNode(node);
                        insertHashQueue(blockNumber%4, new bufferHeader(blockNumber));
                    }
                }
            }
            else 
            {
                System.out.println(threadName+" is requesting for block number "+blockNumber);
                System.out.println("But freelist is empty, now Thread "+threadName+" is waiting for some buffer to be free");
                synchronized(f)
                {
                    if(isFreeListEmpty())
                        f.empty = true;
                    while(f.empty==true)
                    {
                        System.out.println("In thread's waiting function");
                        f.wait();
                    }
                    System.out.print("Wait is over for thread "+threadName);
                }
            }
        }
}
class myThread extends Thread{
    // this class is the custom defined thread class 
    static bufferCache b = new bufferCache();       
                                                    //main bufferCache oject that 
                                                    //is shared among all the threads 
    
    String threadName;                              //name of thread
    static myThread kernel_reference;
    static boolean freelistpopulated = false;       
    static Scanner sc = new Scanner(System.in);     // for input 
    myThread(String threadName){                    //thread constructor that need 
                                                    //thread name
        this.threadName = threadName;
    }

    void storeKernelReference(myThread kernel_reference)
    {
        b.kernel_reference = kernel_reference;
    }

    void populateFreeList(boolean mapToHashQueue)
    {
        //randomly initialize the freelist
        b.populateFreeList(mapToHashQueue);
    }
    int getFreeListSize()
    {
        // if(this.threadName == "kernel")
        return b.getFreeListSize();
    }
    public void run()
    {
        //function that is called when the 
        //thread.start() is called
        System.out.println("This is Thread - "+threadName);
        if(this.threadName == "kernel")
            b.populateFreeList(true);
        else if(this.threadName == "kernel_a")
            b.populateFreeList(false);
        else 
        {
            try
            {
                System.out.print("Enter block number to get : ");
                int blockNumber = sc.nextInt();
                b.blockRequest(blockNumber, this.threadName);
            }
            catch(Exception e)
            {

            }
        }
    }
    void blockRequest(int blockNumber)
    {
        try{b.blockRequest(blockNumber, this.threadName);}
        catch(Exception e){}
    }
    void showFreeList()
    {
        if(this.threadName=="kernel" || this.threadName=="kernel_a")
            b.showFreeList();
        else
            System.out.println("Permission denied for THREAD "+this.threadName+" ask kernel to print the Free List");

    }
    void showHashQueues()
    {
        if(this.threadName=="kernel")
            b.showHashQueues();
        else
            System.out.println("Permission denied");
    }
    void makeNodeBusy(int blockNumber)
    {
        b.makeNodeBusy(blockNumber,this.threadName);
    }
    void addDelayedWrite(int blockNumber)
    {
        b.addDelayedWrite(blockNumber);
    }
    void releaseBuffer(int blockNumber)
    {
        try{b.releaseBuffer(blockNumber, this.threadName);}
        catch(Exception e){}
    }
    void kernel_show_message(String m) throws InterruptedException
    {
            System.out.println(m);
            System.out.println("Wait for few seconds kernel is writing to disk");
            for(int i=3;i>=1;--i)
            {
                System.out.println(threadName+" says - "+i+" seconds are left");
                Thread.sleep(1000);               
            }
            System.out.println("Wait over kernel_reference done it's part(i.e. writing to disk)");
    }
}