class bufferHeader
{   
    // normal buffer header class
    // function names are quite self explanatory
    bufferHeader freeNext;                  
    bufferHeader freePrevious;
    bufferHeader hashQueueNext;
    bufferHeader hashQueuePrevious;
    String aquiredBy;
    // the normal pointer for managing the data structure 
    // as mentioned in AOS books  
    boolean free;           // is requesting block free or not  
    boolean delayedWrite;   // true = first write to disk else ignore
    int blockNumber;        // blocknumber of the bufferheader
    bufferHeader(int blockNumber) // normal 1 parameter constructor 
    {
        this.blockNumber = blockNumber;
        this.free = true;
        this.delayedWrite = false;
        this.aquiredBy = "none";
        freeNext = freePrevious = hashQueueNext = hashQueuePrevious = null;
    }
}