/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.google.api.client.util.Preconditions;
import com.shilling.skillsheets.AbstractHasUuid;
import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountGroup;
import com.shilling.skillsheets.dao.Resource;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;

/**
 *
 * @author Jake Shilling <shilling.jake@gmail.com>
 */
abstract class WrappedResource<T extends Resource> 
        extends AbstractHasUuid {
    
    private final T resource;
    private final ReadWriteLock lock;
    
    protected WrappedResource (T resource) {
        super (resource.getUuid());
        
        Preconditions.checkArgument(!resource.isDeleted());
        
        this.resource = resource;
        this.lock = new ReentrantReadWriteLock();
    }
    
    protected T getResource() {
        Preconditions.checkState(!this.resource.isDeleted());
        return this.resource;
    }
    
    protected Lock readLock() {
        return this.lock.readLock();
    }
    
    protected Lock writeLock() {
        return this.lock.writeLock();
    }
    
    /**
     * Get the resources display name.
     * 
     * @return          Saved display name or Optional.empty()
     */
    abstract public Optional<String> getDisplayName ();
    
    /**
     * Set the display name. If the display name is set to <tt>null</tt> the
     * old display name will simply be deleted.
     * 
     * @param displayName               New display name
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If this operation is performed without
     *                                  editing permissions.
     */
    abstract public WrappedResource<T> setDisplayName (@Nullable String displayName) throws IllegalAccessException;
    
    /**
     * Change the owner of this resource. This operation must be performed by the
     * current owner.
     * 
     * @param account                   New owner
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If this operation is performed by someone
     *                                  other than the owner, or if account is not
     *                                  a teacher account.
     */
    abstract public WrappedResource<T> giveTo (Account account) throws IllegalAccessException;
    
    /**
     * Grant editing permissions to another user account. This operation must
     * be performed by someone who already has editing permissions.
     * 
     * @param account                   New editor
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If this operation is performed without
     *                                  editing permissions, or if account is not
     *                                  a teacher account.
     */
    abstract public WrappedResource<T> letEdit (Account account) throws IllegalAccessException;
    
    /**
     * Grant editing permissions to another user group. This operation must
     * be performed by someone who already has editing permissions.
     * 
     * @param group                     New editor
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If this operation is performed without
     *                                  editing permissions, or if group is not
     *                                  a team.
     */
    abstract public WrappedResource<T> letEdit (AccountGroup group) throws IllegalAccessException;
    
    /**
     * Grant viewing permissions to another user account. This operation must
     * be performed by someone who already has editing permissions.
     * 
     * @param account                   New viewer
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If this operation is performed without
     *                                  editing permissions.
     */
    abstract public WrappedResource<T> letView (Account account) throws IllegalAccessException;
    
    /**
     * Grant viewing permissions to another user group. This operation must
     * be performed by someone who already has editing permissions.
     * 
     * @param group                     New viewer
     * @return                          <tt>this</tt> for chaining
     * @throws IllegalAccessException   If this operation is performed without
     *                                  editing permissions.
     */
    abstract public WrappedResource<T> letView (AccountGroup group) throws IllegalAccessException;
    
    /**
     * Delete this resource from persistent storage. This operation can only
     * be performed by its owner.
     * 
     * @throws IllegalAccessException   If this operation is performed by someone
     *                                  other than the owner.
     */
    abstract public void delete () throws IllegalAccessException;
    
}
