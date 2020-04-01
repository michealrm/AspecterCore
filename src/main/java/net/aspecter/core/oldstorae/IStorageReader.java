package net.aspecter.core.oldstorae;


public interface IStorageReader
{
	 <T extends StorageObject> T load(final Class<? extends T> clazz) throws ObjectLoadException;
}
