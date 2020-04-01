package net.aspecter.core.oldstorae;


public interface IStorageObjectHolder<T extends StorageObject>
{
	T getData();

	void acquireReadLock();

	void acquireWriteLock();

	void close();

	void unlock();
}
