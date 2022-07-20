package com.example.tojob.data.jobs.impl

import com.example.tojob.JobsQuery
import kotlin.random.Random

// Use fake data since https://api.graphql.jobs/ return errors sometimes
private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
private fun getRandomString(len: Int): String {
    return (1..len)
        .map { i -> Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}

fun getFakeCompany(): JobsQuery.Company {
    return JobsQuery.Company(
        name = getRandomString(8),
        logoUrl = "https://logo.clearbit.com/segment.com?size=200"
    )
}

fun getFakeJob(): JobsQuery.Job {
    return JobsQuery.Job(
        title = getRandomString(20),
        company = getFakeCompany(),
        description = getRandomString(Random.nextInt(100, 200))
    )
}

fun getFakeJobList(): List<JobsQuery.Job> {
    val ret = mutableListOf<JobsQuery.Job>()
    repeat(Random.nextInt(30, 60)) {
        ret.add(getFakeJob())
    }
    return ret
}
