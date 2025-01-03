package datameshmanager.sdk;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple in-memory implementation of DataMeshManagerStateRepository.
 */
public class DataMeshManagerStateRepositoryInMemory implements DataMeshManagerStateRepository {

  private final String connectorId;

  private final Map<String, Map<String, Object>> state = new HashMap<>(); // key: connectorId, value: state

  public DataMeshManagerStateRepositoryInMemory(String connectorId) {
    this.connectorId = connectorId;
  }

  @Override
  public Map<String, Object> getState() {
    return this.state.getOrDefault(connectorId, new HashMap<>());
  }

  @Override
  public void saveState(Map<String, Object> state) {
    this.state.put(connectorId, state);
  }
}
