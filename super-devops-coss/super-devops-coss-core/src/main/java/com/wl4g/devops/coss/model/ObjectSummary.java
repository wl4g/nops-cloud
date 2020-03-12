package com.wl4g.devops.coss.model;

import com.wl4g.devops.coss.model.Owner;

/**
 * {@link ObjectValue} summary information.
 * 
 * @author Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2020年3月3日
 * @since
 */
public class ObjectSummary {

	/** The name of the bucket in which this object is stored */
	private String bucketName;

	/** The key under which this object is stored */
	private String key;

	/**
	 * The object ETag. ETag is a 128bit MD5 signature about the object in hex.
	 */
	private String eTag;

	/** object Size */
	private long size;

	/** Last modified time of the object. */
	private long mtime;

	/** Last access time of the object. */
	private long atime;

	/** The key under which this object is stored */
	private String storageType;

	/** Owner of the object. */
	private Owner owner;

	/**
	 * Constructor.
	 */
	public ObjectSummary() {
	}

	/**
	 * Gets the {@link Bucket} name.
	 * 
	 * @return The bucket name.
	 */
	public String getBucketName() {
		return bucketName;
	}

	/**
	 * Sets the {@link Bucket} name.
	 * 
	 * @param bucketName
	 *            The {@link Bucket} name.
	 */
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	/**
	 * Gets the object key.
	 * 
	 * @return Object key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the object key.
	 * 
	 * @param key
	 *            Object key.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Gets the object ETag. ETag is a 128bit MD5 signature about the object in
	 * hex.
	 * 
	 * @return ETag value.
	 */
	public String getETag() {
		return eTag;
	}

	/**
	 * Sets the object ETag.
	 * 
	 * @param eTag
	 *            ETag value.
	 */
	public void setETag(String eTag) {
		this.eTag = eTag;
	}

	/**
	 * Gets the object Size
	 * 
	 * @return Object size.
	 */
	public long getSize() {
		return size;
	}

	/**
	 * Sets the object size.
	 * 
	 * @param size
	 *            Object size.
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * Gets the last modified time of the object.
	 * 
	 * @return The last modified time.
	 */
	public long getMtime() {
		return mtime;
	}

	/**
	 * Sets the last modified time.
	 * 
	 * @param lastModified
	 *            Last modified time.
	 */
	public void setMtime(long mtime) {
		this.mtime = mtime;
	}

	/**
	 * Gets the last access time of the object.
	 * 
	 * @return The last access time.
	 */
	public long getAtime() {
		return atime;
	}

	/**
	 * Sets the last access time.
	 * 
	 * @param atime
	 *            Last access time.
	 */
	public void setAtime(long atime) {
		this.atime = atime;
	}

	/**
	 * Gets the owner of the object.
	 * 
	 * @return Object owner.
	 */
	public Owner getOwner() {
		return owner;
	}

	/**
	 * Sets the owner of the object.
	 * 
	 * @param owner
	 *            Object owner.
	 */
	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	/**
	 * Gets the storage class of the object.
	 * 
	 * @return Object storage class.
	 */
	public String getStorageType() {
		return storageType;
	}

	/**
	 * Sets the storage class of the object.
	 * 
	 * @param storageType
	 *            Object storage class.
	 */
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

}
