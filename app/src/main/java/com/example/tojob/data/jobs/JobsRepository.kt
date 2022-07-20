package com.example.tojob.data.jobs

import com.example.tojob.JobsQuery
import com.example.tojob.data.Result

/**
 * Interface to the Jobs data layer.
 */
interface JobsRepository {

    /**
     * Get Job List.
     */
    suspend fun getJobs(type: String): Result<List<JobsQuery.Job>>
}
