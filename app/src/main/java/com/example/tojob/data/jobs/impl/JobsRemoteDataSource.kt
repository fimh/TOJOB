package com.example.tojob.data.jobs.impl

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.example.tojob.JobsQuery
import com.example.tojob.data.Result
import com.example.tojob.data.jobs.JobsRepository

class JobsRemoteDataSource(
    private val apolloClient: ApolloClient
) : JobsRepository {
    override suspend fun getJobs(type: String): Result<List<JobsQuery.Job>> {
        val data = try {
            apolloClient.query(JobsQuery(type = type)).execute().dataAssertNoErrors
        } catch (exception: ApolloException) {
            // All network or GraphQL errors are handled here
            return Result.Error(exception)
        }

        return Result.Success(data.jobs)
    }
}
