package ru.vafeen.data.local_database.dao.base

import kotlinx.coroutines.flow.Flow

/**
 * Interface of additions for basic [DataAccessObject][ru.vafeen.data.local_database.dao.base.DataAccessObject]
 *
 * Addition: getting all entities T as Flow
 */
internal interface FlowGetAllImplementation<T> {
    /**
     * Getting all entities T as Flow
     */
    fun getAll(): Flow<List<T>>
}