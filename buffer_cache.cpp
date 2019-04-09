#include <iostream> 		//
#include <stdlib.h>		//sleep(seconds in ms)
#include <sys/types.h> 		// 
#include <unistd.h>    		// 
using namespace std;
class bufferHeader
{
	private:
	int deviceNumber;
	int blockNumber;
	bool status[5];
		/*
		 * locked
		 * valid
		 * delayedWrite
	 	 * readingOrWriting
		 * waitingForFree
		*/
	bufferHeader *next;
        bufferHeader *previous;
	public:
	bufferHeader(int blockNumber = 0)
	{
		this->blockNumber = blockNumber;
	}
	bufferHeader* getNext()
	{
		return this->next;
	}
	bufferHeader* getPrevious()
        {
                return this->previous;
        }
	bufferHeader* getReference()
	{
		return this;
	}
        void storeNext(bufferHeader *reference)
	{
		this->next = reference;
	}
	void storePrevious(bufferHeader *reference)
	{
		this->previous = reference;
	}
};
class freeList
{
	private:
	bufferHeader *head, *tail;
	
	public:
	freeList()
	{
		/*
		 * Every buffer will be in freeList
		*/
	}
	void insert(int blockNumber = 0)
	{
		if(head == NULL)
                {
                        head = new bufferHeader(blockNumber);
                        tail = head;
                        head->storeNext(head);
                        head->storePrevious(head);
                }
		if(head == tail)
                {
                        bufferHeader *newNode = new bufferHeader(blockNumber);
                        head->storeNext(newNode);
                        newNode->storePrevious(head);
                        tail = newNode;
                        tail->storeNext(head);
                        head->storePrevious(tail);
                }
                else
                {
                        bufferHeader *newNode = new bufferHeader(blockNumber);
                        tail->storeNext(newNode);
                        newNode->storePrevious(tail);
                        tail = newNode;
                        tail->storeNext(head);
                        head->storePrevious(tail);
                }

	}
	
};
class hashQueue
{
	private:
	bufferHeader* head;
        bufferHeader* tail;

	public:
	hashQueue()
	{
		head = NULL;
		tail = NULL;
	}
	bool insert(int blockNumber = 0)
	{
		if(head == NULL)
		{
                   	head = new bufferHeader(blockNumber);
			tail = head;
			head->storeNext(head);
			head->storePrevious(head);
		}
		else if(head == tail)
		{
			bufferHeader *newNode = new bufferHeader(blockNumber);
			head->storeNext(newNode);
			newNode->storePrevious(head);
			tail = newNode;
			tail->storeNext(head);
			head->storePrevious(tail);
		}
		else
		{
			bufferHeader *newNode = new bufferHeader(blockNumber);
			tail->storeNext(newNode);
			newNode->storePrevious(tail);
			tail = newNode;
			tail->storeNext(head);
			head->storePrevious(tail);
		}

	}

};

class bufferCache
{
	private:
	hashQueue hashQueues[4];
	/*
	 * 4 hash queues for storage
	 */
	freeList freelist;
	public:
	bufferCache()
	{
		
	}

};

class myDataStructure
{
	bool locked;
	public:
	void lock()
	{
		this->locked = true;
	}
	void unlock()
	{
		this->locked = false;
	}
	bool getStatus()
	{
		return this->locked;
	}

};


int main()
{
	// fork();
	//int id = fork();//getpid();
	//fork();
	//fork();
	
	//if(id == 0)
	//{
	
/*	cout << "Child\'s ppid is : " << getppid()<< endl ;
	}
	else
	{
		cout << "Parent" << getpid() << endl;
	}

*/















	//fork();
	//fork();
	myDataStructure ds;
	//int pid = getpid();
	ds.lock();
	cout << "locking by "<<getpid() <<endl ;
	int pid = fork();
	if(pid == 0)
		if(ds.getStatus())
			cout<<"Child waiting"<<endl;
	else
		ds.unlock();
	//cout << "Hello" << endl;
}



