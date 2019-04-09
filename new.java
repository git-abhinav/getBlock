import java.util.*;         //for random and scanner 
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
        int hashQueueSizes[] = new int[4];
    public 
        Random rand = new Random();
        bufferCache()
        {
            this.freeListSize = 0;
            for(int i=0;i<4;++i)
            {    
                this.hashQueue[i] = new bufferHeader(-1);
                this.hashQueueSizes[i] = 0;
            }
        }
        void showRow(int whichQueue)
        {
            bufferHeader b = hashQueue[whichQueue].hashQueueNext;
            System.out.print("- ");
            b = hashQueue[whichQueue].hashQueueNext;
            while(b!=hashQueue[whichQueue])
            {
                System.out.print(b.blockNumber+" ");
                b = b.hashQueueNext;
            }
        }
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
                    return true;
                tempNode = tempNode.freeNext;
            }
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
            // for(int i=0;i<16;++i)
            //     System.out.println(arr[i]+" ");
            return arr;
        }


        void populateFreeList()     //init step and should be called at the 
                                    //beginning only
        
        {
            int randomUnique[] = generateRandomUnique();
            for(int i=0;i<16;++i)
            {    
                // r = rand.nextInt(50); ////////////////////////////
                insertInFreeList(randomUnique[i]);     // random number from 0-49//   
                                         ////////////////////////////
            }
            System.out.println("Now mapping");
            mapFreeListOnHashQueues();
            for(int i=0;i<4;++i)
            {
                System.out.print("Showing row - "+i+"->");
                showRow(i);   
                System.out.println("");
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
                throw new IllegalArgumentException("Free List Empty");
            else if(freeListSize == 1)
                freeList.setFreePointers(freeList, freeList);
            else{
                freeList.freeNext = firstNode.freeNext;
                firstNode.freeNext.freePrevious = freeList;
            }
            freeListSize--;
            System.out.println("Dec size");
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
            System.out.println("Printing Queues");
            for(int i=0;i<4;++i)
            {
                System.out.print("Row "+i);    
                showRow(i);
                System.out.println("");
            }
            System.out.print("Sizes - ");
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
                    return true;
                b = b.hashQueueNext;
            }
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
            Thread.sleep(seconds);
            System.out.println("Took "+seconds+" m-sec(s)");
            System.out.println("Reading done");
            node.free = false;
            System.out.println("Freeing up the header");
        }
        void blockRequest(int blockNumber) throws InterruptedException
        {
            System.out.println("In BlockRequest");
            System.out.println("Requesting for "+blockNumber);
            if(isFreeListEmpty()==false)      //freelist not empty 
            {
                System.out.println("Free List not empty");
                if(inFreeList(blockNumber)==true)                    // block in freelist 
                                                                     // case 1 working fine.
                {
                    if(inHashQueue(blockNumber%4, blockNumber) == true)  
                    //case 1 when block is in freelist and hashqueue also .REMARK WORKING FINE.                                                              
                    {
                        System.out.println(blockNumber+" in hash queue"+blockNumber%4);
                        System.out.println("Taking out it from freelist");
                        bufferHeader node = getFreeNode(blockNumber);
                        System.out.println(blockNumber+" inserted in hashqueue "+blockNumber%4);
                    }
                    else 
                    {
                        // soon
                    }
                }

                else if(inFreeList(blockNumber) == false)               // case 2 
                                                                        // REMARK 
                                                                        // WORKING FINE :)
                // when the required block is not in the freelist 
                // and free list is not empty, so what we do is 
                // get the first node from the first of the free list 
                // and then insert the block in the approriate hash queue.
                {
                    if()//
                    /*
                      ----------------------------------------------------
                      ----------------------------------------------------
                      ----------------------------------------------------
                      ----------------------------------------------------
                      --------------------  FROM HERE --------------------
                      ----------------------------------------------------
                      ----------------------------------------------------
                      ----------------------------------------------------
                      ----------------------------------------------------

                    */
                    {
                        System.out.println(blockNumber+" is not in the freelist");
                        bufferHeader node = getFirstFreeNode();
                        System.out.println(node.blockNumber+" got from the freelist as a replacement");
                        deleteFreeNode(node);
                        insertHashQueue(blockNumber%4, new bufferHeader(blockNumber));
                        System.out.println(node.blockNumber+" inserted in the hashqueue "+blockNumber%4);
                        doRead(blockNumber);
                    }
                }   
            }
            else                        // freelist empty
                                        // 4th case when the freelist is empty.
            {
                //
            }
        }
        boolean free = false;
        synchronized void check() throws InterruptedException
        {
            System.out.println("Checking");
            if(!free)
            {
                System.out.println("Waiting it to be free");
                wait();
                System.out.println("Done");
            }
            else{
                System.out.println("All done"+free);
            }
        }

        synchronized void redeem()throws InterruptedException
        {
            System.out.println("Redeeming");
            free = true;
            notifyAll();
        }

}
class j{
    public static void main(String args[])throws InterruptedException
    {
        bufferCache b = new bufferCache();
        Scanner sc = new Scanner(System.in);
        b.populateFreeList();
        b.showFreeList();
        b.showHashQueues();
        // b.blockRequest(12);
        // b.showFreeList();
        // b.generateRandomUnique();
        System.out.print("Which block number to get : ");
        int blockNumber = sc.nextInt();
        b.blockRequest(blockNumber);
        b.showHashQueues();
        b.showFreeList();
        System.out.println("");
        // b.showHashQueues();
    }
}