package com.argusoft.medplat.common.dao;

import com.argusoft.medplat.common.dto.SyncServerDto;
import com.argusoft.medplat.common.dto.SyncWithServerDto;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.UUID;


/**
 * <p>Defines database method for server management</p>
 * @author ashish
 * @since 31/08/2020 10:30
 */
public interface ServerManagementDao {
    /**
     * Returns remote configuration based on given criteria
     * @param id An id value
     * @param serverName A server name
     * @param password A server password
     * @param serverUrl A server url
     * @return An instance of JsonNode
     */
    JsonNode getRemoteConfigurations(Integer id,String serverName,String password,String serverUrl);

    /**
     * Creates system sync of give server and configuration
     * @param serverId An id of server
     * @param configId An id of configuration
     * @return A created row id
     */
    Integer insertSystemSyncWith(Integer serverId,Integer configId);

    /**
     * Delete an active server record of given server
     * @param serverIds A list of server id
     * @param featureUUID An uuid of feature
     */
    void deleteActiveServerRecord(List<Integer> serverIds,UUID featureUUID);

    /**
     * Deletes non sync server entry
     * @param serverIds A list of server id
     * @param featureUUID An uuid of feature
     */
    void deleteNoNSyncServerEntry(List<Integer> serverIds,UUID featureUUID);

    /**
     * Deletes non active server record
     * @param serverIds A list of server id
     * @param featureUUID An uuid of feature
     */
    void deleteNoNSyncNonActiveServerEntry(List<Integer> serverIds,UUID featureUUID);

    /**
     * Creates or updates given sync server
     * @param syncServerDto An instance of SyncWithServerDto
     */
    void addOrUpdateServer(SyncServerDto syncServerDto);

    /**
     * Creates mapper record of given server
     * @param syncWithServerDto An instance of SyncWithServerDto
     * @param serverId A server id
     * @return An id of created row
     */
    Integer createMapperRecord(SyncWithServerDto syncWithServerDto, Integer serverId);

    /**
     * Returns a list of active server id of feature
     * @param featureUUID An uuid of feature
     * @return A list of server id
     */
    List<Integer> getActiveServerIdFromFeature(UUID featureUUID);
}
