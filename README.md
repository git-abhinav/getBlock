# AOS Buffer Cace Simulation Assignment

[Analysis report PDF](./Abhinav_Saurav-Bharti.pdf)

[Report Summaries](./reports/)

## By : **Abhinav Kumar, Saurav Bharti**

> Language used : Java 

> Concurrency by : mutli threading

The assignment is mainly done by 4 classes

1. bufferHeader
2. bufferCache
3. myThread
4. main (main runner)

bufferHeader is defined according to the structure given in the AOS book.

bufferCache uses bufferheader

myThread is the custom thread class which uses buffercache 

main class uses the myThread class and creates a multithreaded environment for simulation 

>Working

In the program there are mainly 3-4 threads

One is thread named - kernel, it has got some extra privileges like it has access to all the kernel data structures which is not accessable by other threads directly.

Other threads will request for the block and which may release the blocks timely or may cause other threads to wait and may make free list empty which will cause waiting to be done by all other threads.




> Running 

The program is a menu driven customer event generator to show a particular case of getBock algorithm in a multithreaded environment

You have to enter your choice of which case you want to see, how getblock algorithm handles it.

During concurrent execution of multiple threads SOP lines may get interleaved 
