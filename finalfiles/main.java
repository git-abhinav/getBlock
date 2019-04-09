//Before submission final files
import java.util.*;
class log
{
    int arr[];
    int index = 0;
    log(int size)
    {  
        arr = new int[size];
    }
    void addLog(int item)
    {
        arr[index++] = item;
    }   
    void deleteLog(int item)
    {
        for(int i=0;i<index;++i)
        {
            if(arr[i] == item)  
            {
                arr[i] = arr[index];
                index--;
                return;
            }
        }
        System.out.println("Array storing error");
        System.exit(1);
    }
    boolean presentInLog(int item)
    {
        for(int i=0;i<index;++i)
        {
            if(arr[i]==item)
            {
                return true;
            }
        }
        return false;
    }
}
class main 
{
    static boolean repeatedOrNot(int arr[], int size, int item)
    {
        for(int i=0;i<size;++i)
            if(arr[i]==item)
                return true;
        return false;
    }
    public static void main(String args[]) throws InterruptedException
    {
        Scanner sc = new Scanner(System.in);
        myThread kernel = new myThread("kernel");
        int blockNumber;
        myThread t1 = new myThread("1");
        myThread t2 = new myThread("2");
        kernel.storeKernelReference(kernel);
        System.out.println("\n-------------------------------------");
        System.out.println("----------KERNEL PERFORMING----------");
        System.out.println("-------------------------------------\n");
        kernel.start();
        kernel.join();
        kernel.showFreeList();
        kernel.join();
        System.out.println("\n-------------------------------------");
        System.out.println("----KERNEL'S WORK IS DONE FOR NOW----");
        System.out.println("-------------------------------------\n");

        System.out.println("\n-------------------------------------");
        System.out.println("MAIN THREAD SHOWING THE MENU");
        System.out.println("-------------------------------------\n");
        System.out.println("Case 1, when block is in freelist as well as Hash Queue");
        System.out.println("Case 2, when block is not in freelist and first node from the FreeList is to be deleted");
        System.out.println("Case 3, when block is marked delayed write and kernel needs write it on disk first");
        System.out.println("Case 4, when 2 threads are requesting for same block");
        System.out.println("Case 5, when Free List is empty");
        System.out.println("Press 6, for automated simulation");
        System.out.println("Prees 7, for MENU DRIVEN PROGRAM");
        System.out.print("Which case you want to see : ");
        int choice = sc.nextInt();

        if(choice == 1)
        {
            System.out.println("\n-------------------------------------");
            System.out.println("\n-------------THREAD 1----------------");
            System.out.println("\n-------------------------------------");
            System.out.println("Please enter a block which is in the Free List");
            t1.start();
            t1.join();
            t1.showFreeList();
            t1.join();
            kernel.showFreeList();
            kernel.showHashQueues();
        }
        else if(choice == 2)
        {
            System.out.println("\n-------------------------------------");
            System.out.println("\n-------------THREAD 1----------------");
            System.out.println("\n-------------------------------------");
            System.out.println("Please enter the block which is not in the Free List");
            t1.start();
            t1.join();
            t1.showFreeList();
            kernel.showFreeList();
            kernel.showHashQueues();
        }
        else if(choice == 3)
        {
            t1.start();
            t1.join();
            System.out.println("Enter block number to add delay : ");
            blockNumber = sc.nextInt();
            t1.addDelayedWrite(blockNumber);
            kernel.showFreeList();
            kernel.showHashQueues();
            t1.releaseBuffer(blockNumber);
            kernel.showFreeList();
            kernel.showHashQueues();
            t2.start();
            t2.join();
            kernel.showFreeList();
            kernel.showHashQueues();
        }
        else if(choice == 4)
        {    
            t1.setPriority(9);
            t2.setPriority(1);
            t1.start();
            t1.join();
            System.out.print("Which to make busy : ");
            blockNumber = sc.nextInt();
            t1.makeNodeBusy(blockNumber);
            t1.join();
            t2.start();
            t1.join();
            System.out.println("Message from MAIN THREAD - (ouput lines will not be okay because of concurrency)");
            System.out.println("Wait for few sec(s) to get it free as THREAD "+t1.threadName+" is using it");
            System.out.println("\n");
            for(int i=0;i<10;++i)
            {
                if(i<8)
                    System.out.println("Seconds left - "+(10-i));
                else 
                    System.out.println("Last few seconds left");
                t1.sleep(1000);
            }
            t1.join();
            t1.releaseBuffer(blockNumber);
        }
        else if(choice == 5)
        {
            System.out.println("Output lines may be not come in order because of concurrency");
            t2.setPriority(9);
            t1.setPriority(1);
            t1.start();
            t1.join();
            kernel.showFreeList();
            kernel.showHashQueues();
            int i=0;
            while(kernel.getFreeListSize()!=0)
            {
                t1.blockRequest(i++);
                t1.sleep(500);
                kernel.showFreeList();
                kernel.showHashQueues();
            }
            t1.join();
            t2.start();
            t1.join();
            for(int j=0;j<10;++j)
            {
                System.out.println("Just "+(10-j)+" seconds left for a block to become free");
                t1.sleep(1000);
            }
            t1.releaseBuffer(5);       
        }
        else if(choice == 6)
        {
            System.out.println("Running automated program");
            t1.start();
            t1.join();
            kernel.showFreeList();
            kernel.showHashQueues();
            System.out.println("-------------------------------------------------------------------------");
            t2.start();
            t2.join();
            kernel.showFreeList();
            kernel.showHashQueues();
            System.out.println("-------------------------------------------------------------------------");
            Random rand = new Random();
            int r1 = rand.nextInt(50);
            int r2 = rand.nextInt(50);
            t2.sleep(20000);
            for(;;)
            {
                System.out.println("-------------------------------------------------------------------------");
                System.out.println("Thread 1 is asking for block number - "+r1);
                System.out.println("Thread 2 is asking for block number - "+r2);
                t1.blockRequest(r1);
                // t1.join();
                t2.blockRequest(r2);
                // t2.join();
                kernel.showFreeList();
                kernel.showHashQueues();
                System.out.println();
                r1 = rand.nextInt(50);
                r2 = rand.nextInt(50);
                t1.sleep(1000);
                t2.sleep(1000);
                t1.releaseBuffer(r1);
                t2.releaseBuffer(r2);
                System.out.println("-------------------------------------------------------------------------\n\n");

            }
        }
        else if(choice == 7)
        {
            // for menu driven
            int innerChoice;
            System.out.println("These are the Free List and HashQueue");
            kernel.showFreeList();
            kernel.showHashQueues();
            log l = new log(50);
            t1.sleep(10000);
            t2.start();
        }
        else 
        {
            System.out.println("Invalid input");
        }

    }
}