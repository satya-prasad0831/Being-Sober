package com.example.beingsober.data.repository

import com.example.beingsober.data.local.dao.IncidentDao
import com.example.beingsober.data.local.entity.IncidentEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IncidentRepository @Inject constructor(
    private val incidentDao: IncidentDao
) {

    suspend fun insertIncident(
        incident: IncidentEntity
    ) {
        incidentDao.insertIncident(incident)
    }

    fun getAllIncidents(): Flow<List<IncidentEntity>> {
        return incidentDao.getAllIncidents()
    }

    suspend fun getIncidentCount(): Int {
        return incidentDao.getIncidentCount()
    }
}