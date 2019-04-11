class main
{
	public static void main(String args[]) throws InterruptedException
	{
		myThread kernel = new myThread("kernel");
		kernel.setPriority(9);
		kernel.kernel_set_parameter(4,4);
		kernel.join();
		kernel.save_kernel_reference();
		kernel.join();
		kernel.intializeFreeList();
		kernel.join();
		kernel.showFreeList();
		kernel.join();
		kernel.showHashQueue();
		kernel.join();

		myThread t = new myThread("Thread1");
		myThread t2 = new myThread("Thread2");
		myThread t3 = new myThread("Thread3");
		myThread t4 = new myThread("Thread4");
		
		for(int i=1;i<=7;++i)
		{	
			t = new myThread("Thread"+Integer.toString(i));
			t.start();
			t.join();
			kernel.showHashQueue();
			kernel.showFreeList();
			// kernel.sleep(2000);
		}
		kernel.dontGiveUpLock = true;
		for(int i=8;i<=20;++i)
		{	
			t = new myThread("Thread"+Integer.toString(i));
			t.start();
			kernel.showHashQueue();
			kernel.join();
			kernel.showFreeList();
			kernel.join();
		}
	}
}