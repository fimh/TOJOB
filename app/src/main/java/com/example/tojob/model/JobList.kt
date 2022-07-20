package com.example.tojob.model

import com.example.tojob.JobsQuery

/**
 * A container of [Job]s, partitioned into different categories.
 */
data class JobList(
    val jobs: List<JobsQuery.Job>,
) {
    /**
     * Calculate the number of the companies
     */
    val companyNumber = jobs.mapNotNull { it.company }.toSet().size
}
