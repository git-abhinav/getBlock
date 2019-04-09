// import java.util.*;         //for random and scanner 
// class bufferHeader{
//     public 
//         static bufferHeader freeNext;               
//         static bufferHeader freePrevious;
//         static bufferHeader hashQueueNext;
//         static bufferHeader hashQueuePrevious;
//         static boolean free; 
//         static boolean delayedWrite;
//         static int blockNumber;
//     public
//         bufferHeader(int blockNumber)
//         {
//             this.blockNumber = blockNumber;
//             this.free = true;
//             this.delayedWrite = false;
//             freeNext = freePrevious = hashQueueNext = hashQueuePrevious = null;
//         }
//         void setFreePointers(bufferHeader previous, bufferHeader next)
//         {
//             this.freePrevious = previous;
//             this.freeNext = next;
//         }
//         void setHashPointers(bufferHeader previous, bufferHeader next)
//         {
//             this.hashQueueNext = previous;
//             this.hashQueuePrevious = next;
//         }
//         bufferHeader[] getFreePointers()
//         {
//             bufferHeader bh[] = new bufferHeader[2];
//             bh[0] = this.freePrevious;
//             bh[1] = this.freeNext;
//             return bh;
//         }
//         bufferHeader[] getHashPointers()
//         {
//             bufferHeader bh[] = new bufferHeader[2];
//             bh[0] = this.hashQueueNext;
//             bh[1] = this.hashQueueNext;
//             return bh;
//         }
// }
// class bufferCache
// {
//     public  
//         final static bufferHeader hashQueue[] = new bufferHeader[4];
//         final static bufferHeader freeList = new bufferHeader(-1);
//          int freeListSize;
//          int hashQueueSizes[] = new int[4];
//     public 
//         Random rand = new Random();
//         bufferCache()                   //working fine 
//         {
//             this.freeListSize = 0;
//             for(int i=0;i<4;++i)
//             {    
//                 this.hashQueue[i] = new bufferHeader(-1);
//                 this.hashQueueSizes[i] = 0;
//             }
//         }           
//         void showRow(int whichQueue)    //working fine
//         {
//             bufferHeader b = hashQueue[whichQueue].hashQueueNext;
//             System.out.print("");
//             b = hashQueue[whichQueue].hashQueueNext;
//             while(b!=hashQueue[whichQueue])
//             {
//                 System.out.print(b.blockNumber+" ");
//                 b = b.hashQueueNext;
//             }
//         }
//                                         //working fine
//         void insertHashQueue(int whichQueue, bufferHeader node)
//         {
//             if(hashQueueSizes[whichQueue] == 0)
//             {
//                 hashQueue[whichQueue].setHashPointers(node, node);
//                 node.setHashPointers(hashQueue[whichQueue],hashQueue[whichQueue]);
//                 hashQueueSizes[whichQueue]++;
//             }
//             else 
//             {
//                 bufferHeader lastNode = hashQueue[whichQueue].hashQueuePrevious;
//                 lastNode.hashQueueNext = node;
//                 node.hashQueuePrevious = lastNode;
//                 node.hashQueueNext = hashQueue[whichQueue];
//                 hashQueue[whichQueue].hashQueuePrevious = node;
//                 hashQueueSizes[whichQueue]++;
//             }
//         }
//                                         //working fine
//         void insertInFreeList(int blockNumber)
//         {
//             bufferHeader newHeader = new bufferHeader(blockNumber);
//             if(freeListSize == 0)
//             {
//                 freeList.setFreePointers(newHeader, newHeader);
//                 newHeader.setFreePointers(freeList, freeList);
//                 ++freeListSize;
//             }
//             else if(freeListSize < 20)
//             {
//                 bufferHeader lastNode[] = new bufferHeader[2];
//                 System.out.println("---1-----");
//                 lastNode = freeList.getFreePointers();
//                 System.out.println("---2-----");
//                 bufferHeader FreeListlastNode = lastNode[0];         //previosu at the 0th index.
//                 System.out.println("---3-----");
//                 FreeListlastNode.freeNext = newHeader;
//                 FreeListlastNode.freePrevious = FreeListlastNode.freePrevious;
//                 FreeListlastNode.setFreePointers(FreeListlastNode.freePrevious, newHeader);
//                 System.out.println("---4-----");
//                 newHeader.setFreePointers(FreeListlastNode, freeList);
//                 freeList.setFreePointers(newHeader, freeList.freeNext);
//                 ++freeListSize;
//             }
//             else
//             {
//                 throw new IllegalArgumentException("Max Free List size attained");
//             }
//         }
//         void showFreeList()
//         {
//             bufferHeader startNode = freeList.freeNext;
//             bufferHeader lastNode = freeList.freeNext;
//             bufferHeader i = startNode;
//             System.out.print("Free list is : ");
//             while(i!=freeList)
//             {
//                 System.out.print(i.blockNumber+" ");
//                 i = i.freeNext;
//             }
//             System.out.print("\nSize of freelist is - "+freeListSize);
//             System.out.println("");
//         }
//         boolean inFreeList(int blockNumber)
//         {
//             bufferHeader tempNode = freeList.freeNext;
//             while(tempNode!=freeList)
//             {
//                 if(blockNumber == tempNode.blockNumber)
//                 {    
//                     System.out.println(blockNumber+" Found in free list");
//                     return true;
//                 }
//                 tempNode = tempNode.freeNext;
//             }
//             System.out.println("Not found in free list");
//             return false;
//         }
//         boolean presentIn(int arr[], int item, int size)
//         {
//             for(int i=0;i<size;++i)
//                 if(arr[i] == item)
//                     return true;
//             return false;
//         }
//         int[] generateRandomUnique()
//         {
//             int arr[] = new int[16];
//             int r;
//             for(int i=0;i<16;++i)
//             {
//                 if(i==0)
//                     arr[i] = rand.nextInt(50);
//                 else 
//                 {
//                     r = rand.nextInt(50);
//                     while(presentIn(arr, r, i+1))
//                         r = rand.nextInt(50);
//                     arr[i] = r;
//                 }
//             }
//             System.out.println("Initial env created");
//             return arr;
//         }
//         void populateFreeList()     //init step and should be called at the 
//                                     //beginning only
        
//         {
//             int randomUnique[] = generateRandomUnique();
//             // to simulate the initial boot up buffer.
//             for(int i=0;i<16;++i)
//             {    
//                 // r = rand.nextInt(50); ////////////////////////////
//                 insertInFreeList(randomUnique[i]);     // random number from 0-49//   
//                                          ////////////////////////////
//             }
//             System.out.println("Mapping env on queues");
//             mapFreeListOnHashQueues();
//             for(int i=0;i<4;++i)
//             {
//                 System.out.print("Showing row - "+i+"[ ");
//                 showRow(i);   
//                 System.out.println("]");
//             }
//         }
//         bufferHeader getFreeNode(int blockNumber)
//         {
//             bufferHeader tempNode = freeList.freeNext;
//             while(tempNode!=freeList)
//             {
//                 if(tempNode.blockNumber == blockNumber)
//                 {
//                     bufferHeader p = tempNode.freePrevious;
//                     bufferHeader n = tempNode.freeNext;
//                     p.freeNext = n;
//                     n.freePrevious = p;
//                     freeListSize--;
//                     System.out.println("Taking out node "+tempNode.blockNumber+" from freelist");
//                     return tempNode;
//                 }
//                 tempNode = tempNode.freeNext;
//             }
//             return null;
//         }
//         bufferHeader getFirstFreeNode()
//         {
//             bufferHeader firstNode = freeList.freeNext;
//             if(freeListSize == 0)
//                 throw new IllegalArgumentException("Free List to Empty hai");
//             else if(freeListSize == 1)
//                 freeList.setFreePointers(freeList, freeList);
//             else
//             {
//                 freeList.freeNext = firstNode.freeNext;
//                 firstNode.freeNext.freePrevious = freeList;
//             }
//             freeListSize--;
//             System.out.println("Node "+firstNode.blockNumber+" retrieved");
//             return firstNode;
//         }
//         void mapFreeListOnHashQueues()  //map the new gernerated freelist on to
//                                         //the 4 of the hashqueues
//         {
//             bufferHeader startNode = freeList.freeNext;
//             bufferHeader i = startNode;
//             while(i!=freeList)
//             {
//                 insertHashQueue(((i.blockNumber)%4), i);
//                 i = i.freeNext;
//             }
//         }
//         void showHashQueues()
//         {
//             System.out.println("Printing Hash Queues");
//             for(int i=0;i<4;++i)
//             {
//                 System.out.print("Hash Queue "+i+"   ");    
//                 showRow(i);
//                 System.out.println("");
//             }
//             System.out.println("Hash Queue sizes are : ");
//             for (int i=0;i<4;++i)
//                 System.out.print(hashQueueSizes[i]+" ");
//             System.out.println("");
//         }
//         boolean inHashQueue(int whichQueue, int blockNumber)
//         {
//             bufferHeader b = hashQueue[whichQueue].hashQueueNext;
//             while(b!=hashQueue[whichQueue])
//             {
//                 if(b.blockNumber==blockNumber)
//                 {
//                     // System.out.println(blockNumber+" in hash queue "+ whichQueue);
//                     return true;
//                 }
//                 b = b.hashQueueNext;
//             }
//             // System.out.println(blockNumber+" not in hash queue "+ whichQueue);
//             return false;
//         }
//         void addInHashQueue(int whichQueue, bufferHeader node)
//         {
//             bufferHeader tempNode = hashQueue[whichQueue].hashQueueNext;
//             if(hashQueueSizes[whichQueue] == 0)
//             {
//                 hashQueue[whichQueue].setHashPointers(node, node);
//                 node.setHashPointers(hashQueue[whichQueue], hashQueue[whichQueue]);
//                 hashQueueSizes[whichQueue]++;
//                 System.out.println("Added in hashqueue "+whichQueue);
//             }
//             else 
//             {
//                 bufferHeader lastNode = hashQueue[whichQueue].hashQueuePrevious;
//                 lastNode.hashQueueNext = node;
//                 node.hashQueuePrevious = lastNode;
//                 hashQueue[whichQueue].hashQueuePrevious = node;
//                 node.hashQueueNext = hashQueue[whichQueue];
//                 hashQueueSizes[whichQueue]++;
//                 System.out.println("Added in hashqueue "+whichQueue);
//             }
//         }
//         boolean isFreeListEmpty()
//         {
//             if(freeList.freeNext == freeList)
//                 return true;
//             else 
//                 return false;
//         }
//         void insertAtEnd(int whichQueue, bufferHeader node)
//         {
//             bufferHeader lastNode = hashQueue[whichQueue].hashQueuePrevious;
//             lastNode.hashQueueNext = node;
//             node.hashQueuePrevious = lastNode;
//             node.hashQueueNext = hashQueue[whichQueue];
//             hashQueue[whichQueue].hashQueuePrevious = node;
//             node.freeNext = null;
//             node.freePrevious = null;
//         }
//         void deleteFreeNode(bufferHeader node)
//         {
//             System.out.println("Deleting freelist node "+node.blockNumber);
//             node = null;
//         }
//         void doRead(int blockNumber) throws InterruptedException
//         {   
//             int whichQueue = blockNumber%4;
//             bufferHeader node = hashQueue[whichQueue].hashQueuePrevious;
//             while(node.blockNumber!=blockNumber)
//                 node = node.hashQueueNext;
//             node.free = false;
//             System.out.println("Now reading for 1-5 seconds");
//             int seconds = (1+rand.nextInt(5))*1000;
//             // Thread.sleep(seconds);
//             System.out.println("Took "+seconds+" m-sec(s)");
//             System.out.println("Reading done");
//             node.free = false;
//             System.out.println("Freeing up the header");
//         }
//         void makeNodeBusy(int blockNumber)
//         {
//             int whichQueue = blockNumber%4;
//             bufferHeader node = hashQueue[whichQueue].hashQueueNext;
//             while(node!=hashQueue[whichQueue])
//             {
//                 if(node.blockNumber == blockNumber)
//                 {    
//                     node.free = false;
//                     System.out.println(node.blockNumber+" busy now");
//                 }
//                 node = node.hashQueueNext;
//             }
//             // node.free = false;
//         }
//         bufferHeader getHashRef(int blockNumber)
//         {
//             int whichQueue = blockNumber%4;
//             bufferHeader node = hashQueue[whichQueue].hashQueueNext;
//             while(node!=hashQueue[whichQueue])
//             {
//                 if(node.blockNumber == blockNumber)
//                     return node;
//                 node = node.hashQueueNext;
//             }
//             return null;
//         }
//         void releaseBuffer(int blockNumber, String threadName)             //formally knowns as
//                                                         //brelse
//         {
//             //wake up - notifyAll()
//             bufferHeader node = getHashRef(blockNumber);
//             synchronized(node)
//             {   
//                 if(node.delayedWrite == true)
//                 {
//                     System.out.println("Marked delayed adding to the head of the freelist");
//                     bufferHeader firstFreeNode = freeList.freeNext;
//                     freeList.freeNext = node;
//                     node.freePrevious = freeList;
//                     node.freeNext = firstFreeNode;
//                     firstFreeNode.freePrevious = node;
//                     freeListSize = freeListSize + 1;
//                     System.out.println(threadName+" gave up block "+blockNumber);
//                     node.notifyAll();  
//                 }
//                 else 
//                 {
//                     System.out.println("Not marked delayed write adding to the tail of the freelist");
//                     bufferHeader lastFreeNode = freeList.freePrevious;
//                     lastFreeNode.freeNext = node;
//                     node.freePrevious = lastFreeNode;
//                     node.freeNext = freeList;
//                     freeList.freePrevious = node;
//                     freeListSize++;
//                     System.out.println(threadName+" gave up block "+blockNumber);
//                     node.notifyAll();
//                 }
//             }
//         }
//         void addDelayedWrite(int blockNumber, String threadName)
//         {
//             int whichQueue = blockNumber%4;
//             bufferHeader node = hashQueue[whichQueue].hashQueueNext;
//             while(node!=hashQueue[whichQueue])
//             {
//                 if(node.blockNumber == blockNumber)
//                 {
//                     node.delayedWrite = true;
//                     System.out.println("Delay marked by "+threadName);
//                     return;
//                 }
//                 node = node.hashQueueNext;
//             }
//         }
//         void blockRequest(int blockNumber, String threadName) throws InterruptedException
//         {
//             System.out.println("Requesting for "+blockNumber);
//             if(isFreeListEmpty()==false)
//             {
//                 System.out.println("Free list not empty");
//                 if(inHashQueue(blockNumber%4, blockNumber))
//                 {
//                         if(inFreeList(blockNumber))             //case1 REMARKS FINE!
//                         {
//                             System.out.println(blockNumber+"In free list");
//                             bufferHeader node = getFreeNode(blockNumber);
//                             deleteFreeNode(node);
//                         }
//                         else 
//                         {
//                             bufferHeader ref = getHashRef(blockNumber);
//                             synchronized(ref)
//                             {
//                                 if(ref.free == false)                //just read
//                                 {
//                                     System.out.println(threadName+" is wait for blocknumber "+blockNumber+" to be free"); 
//                                     while(ref.free == false)
//                                     {
//                                         ref.wait();
//                                     }
//                                     System.out.println("Wait is over for thread "+threadName);
//                                 }
//                             }
//                         }
//                 }
//                 else                                         
//                 {
//                     //get first node
//                     System.out.println(blockNumber+" not in hashqueue");
//                     System.out.println("Getting a the first node from the freelist");
//                     bufferHeader node = getFirstFreeNode();
//                     if(node.delayedWrite == true)          //case2
//                     {
//                         deleteFreeNode(node);
//                         System.out.println("The node was marked dalayed write se writing it first");
//                         insertHashQueue(blockNumber%4, new bufferHeader(blockNumber));
//                     }
//                     else                                   //case3
//                     {
//                         System.out.println("The node was not  marked dalayed write");
//                         deleteFreeNode(node);
//                         insertHashQueue(blockNumber%4, new bufferHeader(blockNumber));
//                     }
//                 }
//             }
//             else                                            //case5
//             {
//                 System.out.println("Free list to empty hai");
//             }
//         }

// }
// class j extends Thread
// {
//     String name;
//     static bufferCache b = new bufferCache();
//     Scanner sc = new Scanner(System.in);
//     j()
//     {
//         System.out.println("Init Env");
//         b.populateFreeList();
//         b.showFreeList();
//     }
//     public void run()
//     {
//         try
//         {
//             System.out.println("Thread "+this.name+" running");
//             System.out.println("Which block to get : ");
//             int blockNumber = sc.nextInt();
//             System.out.println("Requesting for block "+blockNumber); 
//             b.blockRequest(blockNumber, this.name);
//         }
//         catch(Exception e)
//         {}
//     }
//     public void releaseBuffer(int blockNumber)
//     {
//         try
//         {
//             b.releaseBuffer(blockNumber, this.name);
//         }
//         catch(Exception e)
//         {}
//     }
//     public void markDelay(int blockNumber)
//     {
//         try
//         {    
//             b.addDelayedWrite(blockNumber, this.name);
//         }
//         catch(Exception e)
//         {}
//     }
//     public void showHashQueues()
//     {
//         try
//         {
//             b.showHashQueues();
//         }
//         catch(Exception e)
//         {}
//     }
//     public void showFreeList()
//     {
//         try
//         {
//             b.showFreeList();
//         }
//         catch(Exception e)
//         {}
//     }
// }   




















import java.util.*;         //for random and scanner 

class criticalSection
{
    boolean freeYesOrNo;
    boolean getYesOrNo()
    {
        return this.freeYesOrNo;
    }
    void makeitfree()
    {
        System.out.println("free now ");
        this.freeYesOrNo = true;
    }
    void makeitbusy()
    {
        System.out.println("busy now ");
        this.freeYesOrNo = false;
    }
}
class bufferHeader{
    public 
        bufferHeader freeNext;               
        bufferHeader freePrevious;
        bufferHeader hashQueueNext;
        bufferHeader hashQueuePrevious;
        boolean free; 
        boolean delayedWrite;
        int blockNumber;
    public
        bufferHeader(int blockNumber)
        {
            this.blockNumber = blockNumber;
            this.free = true;
            this.delayedWrite = false;
            freeNext = freePrevious = hashQueueNext = hashQueuePrevious = null;
        }
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
            bh[0] = this.hashQueueNext;
            bh[1] = this.hashQueueNext;
            return bh;
        }
}
class bufferCache{
    public  
        bufferHeader hashQueue[] = new bufferHeader[4];
        bufferHeader freeList = new bufferHeader(-1);
        int freeListSize;
        // static boolean isNodeFree;
        int hashQueueSizes[] = new int[4];
        final criticalSection cs = new criticalSection();
    public 
        Random rand = new Random();
        // boolean isNodeFreeStatus()
        // {
        //     return isNodeFree;
        // }
        bufferCache()                   //working fine 
        {
            this.freeListSize = 0;
            for(int i=0;i<4;++i)
            {    
                this.hashQueue[i] = new bufferHeader(-1);
                this.hashQueueSizes[i] = 0;
            }
        }           
        void showRow(int whichQueue)    //working fine
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
                                        //working fine
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
                                        //working fine
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
                throw new IllegalArgumentException("Max Free List size attained");
            }
        }
        void showFreeList()
        {
            bufferHeader startNode = freeList.freeNext;
            bufferHeader lastNode = freeList.freeNext;
            bufferHeader i = startNode;
            System.out.print("Free list is : ");
            while(i!=freeList)
            {
                System.out.print(i.blockNumber+" ");
                i = i.freeNext;
            }
            System.out.print("\nSize of freelist is - "+freeListSize);
            System.out.println("");
        }
        boolean inFreeList(int blockNumber)
        {
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
            for(int i=0;i<size;++i)
                if(arr[i] == item)
                    return true;
            return false;
        }
        int[] generateRandomUnique()
        {
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
        void populateFreeList()     //init step and should be called at the 
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
            if(freeList.freeNext == freeList)
                return true;
            else 
                return false;
        }
        void insertAtEnd(int whichQueue, bufferHeader node)
        {
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
            System.out.println("Deleting freelist node "+node.blockNumber);
            node = null;
        }
        void doRead(int blockNumber) throws InterruptedException
        {   
            int whichQueue = blockNumber%4;
            bufferHeader node = hashQueue[whichQueue].hashQueuePrevious;
            while(node.blockNumber!=blockNumber)
                node = node.hashQueueNext;
            node.free = false;
            System.out.println("Now reading for 1-5 seconds");
            int seconds = (1+rand.nextInt(5))*1000;
            // Thread.sleep(seconds);
            System.out.println("Took "+seconds+" m-sec(s)");
            System.out.println("Reading done");
            node.free = false;
            System.out.println("Freeing up the header");
        }
        void makeNodeBusy(int blockNumber)
        {
            int whichQueue = blockNumber%4;
            bufferHeader node = hashQueue[whichQueue].hashQueueNext;
            while(node!=hashQueue[whichQueue])
            {
                if(node.blockNumber == blockNumber)
                {    
                    node.free = false;
                    System.out.println(node.blockNumber+" busy now");
                    synchronized(cs){cs.makeitbusy();}
                }
                node = node.hashQueueNext;
            }
        }
        bufferHeader getHashRef(int blockNumber)
        {
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
        void releaseBuffer(int blockNumber, String threadName)             //formally knowns as
                                                        //brelse
        {
            //wake up - notifyAll()
            System.out.println(threadName+" releasing buffer");
            final bufferHeader node = getHashRef(blockNumber);
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
                    // System.out.println("Lat");
                }
            }
        }
        void addDelayedWrite(int blockNumber)
        {
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
        void blockRequest(int blockNumber, String threadName) throws InterruptedException
        {
            System.out.println(threadName+" Requesting for "+blockNumber);
            if(isFreeListEmpty()==false)
            {
                System.out.println("Free list not empty");
                if(inHashQueue(blockNumber%4, blockNumber))
                {
                    if(inFreeList(blockNumber))             //case1 REMARKS FINE!
                    {
                        System.out.println(blockNumber+"In free list");
                        bufferHeader node = getFreeNode(blockNumber);
                        deleteFreeNode(node);
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
                                    System.out.println("waiting");
                                    cs.wait();
                                    
                                }
                                cs.makeitfree();
                                System.out.println(threadName+"\'s Wait over finally");
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
                    if(node.delayedWrite == true)          //case2
                    {
                        deleteFreeNode(node);
                        System.out.println("The node was marked dalayed write se writing it first");
                        insertHashQueue(blockNumber%4, new bufferHeader(blockNumber));
                    }
                    else                                   //case3
                    {
                        System.out.println("The node was not  marked dalayed write");
                        deleteFreeNode(node);
                        insertHashQueue(blockNumber%4, new bufferHeader(blockNumber));
                    }
                }
            }
            else                                            //case5
            {
                System.out.println("Free list to empty hai");
            }
        }
}
class j extends Thread{
    static bufferCache b = new bufferCache();
    String threadName;
    static boolean freelistpopulated = false;
    static Scanner sc = new Scanner(System.in);
    j(String threadName){   
        this.threadName = threadName;
    }
    void populateFreeList()
    {
        b.populateFreeList();
    }
    public void run()
    {
        System.out.println("This is Thread - "+threadName);
        if(this.threadName == "kernel")
            b.populateFreeList();
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
        if(this.threadName=="kernel")
            b.showFreeList();
        else
            System.out.println("Permission denied");
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
        b.makeNodeBusy(blockNumber);
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
}