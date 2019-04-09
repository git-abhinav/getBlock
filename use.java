import java.util.*;
class use 
{
    public static void main(String args[]) throws InterruptedException
    {
        Scanner sc = new Scanner(System.in);
        j kernel = new j("kernel");
        int blockNumber;
        j t1 = new j("1");
        j t2 = new j("2");
        
        kernel.start();
        kernel.join();

        System.out.println("Which to make busy : ");
        blockNumber = sc.nextInt();

        t1.setPriority(9);
        t2.setPriority(1);
        t1.start();
        t1.makeNodeBusy(blockNumber);
        t1.join();
        t2.start();
        t1.join();
        System.out.println("Wait for 5 sec to get it free");
        t1.sleep(5000);
        t1.releaseBuffer(blockNumber);




        // t1.join();
        // t1.join();
        // t1.releaseBuffer(blockNumber);
        // t1.sleep(2000);
        // t2.join();
        // t1.sleep(1000);
        // t1.join();
        // t1.releaseBuffer(blockNumber);
        // t2.join();

        // System.out.println("Which block to get : ");
        // blockNumber = sc.nextInt();
        // t1.blockRequest(blockNumber);
        // kernel.showHashQueues();
        // kernel.showFreeList();
        // t1.makeNodeBusy(blockNumber);
        // t2.blockRequest(blockNumber);
        // // t1.sleep(2000);
        // t1.releaseBuffer(blockNumber);







        /*running fine*/
        // kernel.join();
        // j t1 = new j("1");
        // // t1.start();
        // System.out.println("Which block to get : ");
        // blockNumber = sc.nextInt();
        // t1.blockRequest(blockNumber);
        // kernel.showHashQueues();
        // kernel.showFreeList();

        // System.out.println("Make busy whome : ");
        // blockNumber = sc.nextInt();
        // t1.makeNodeBusy(blockNumber);
        // kernel.showHashQueues();
        // kernel.showFreeList();


        // System.out.println("Make which node delayed : ");
        // blockNumber = sc.nextInt();
        // t1.addDelayedWrite(blockNumber);
        // kernel.showHashQueues();
        // kernel.showFreeList();

        // System.out.println("Calling release");
        // t1.releaseBuffer(blockNumber);
        // kernel.showHashQueues();
        // kernel.showFreeList();



        // t1.join();
    }
}