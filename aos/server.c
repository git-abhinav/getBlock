#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <stdio.h>
#define MSGKEY 75

struct msgform
{
	long mtype;
	char mtext[256];
} msg;
int msgid;
int main()
{
	int i, pid, *pint;
	extern cleanup();
	for(int i=0;i<20;++i)
		signal(i, cleanup);
	msgid = msgget(MSGKEY, 0777|IPC_CREAT);
	for(;;)
	{
		msgrcv(msgid, &msg, 256, 1, 0);
		pint = (int*) msg.mtext;
		pid = *pint;
		printf("Server: receive from pid %d\n", pid);
		msg.mtype =  pid;
		*pint = getpid();
		msgsnd(msgid, &msg, sizeof(int), 0);
	}
}
int cleanup()
{
	msgctl(msgid, IPC_RMID, 0);
	exit(0);
}
