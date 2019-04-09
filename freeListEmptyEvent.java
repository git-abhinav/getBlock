class freeListEmptyEvent
{
	boolean freeListEmpty;
	freeListEmptyEvent()
	{
		this.freeListEmpty = false;
	}
	void freeListEmptyNow()
	{
		this.freeListEmpty = true;
	}
	void freeListNotEmptyNow()
	{
		this.freeListEmpty = false;
	}
}