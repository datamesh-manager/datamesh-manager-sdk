package datameshmanager.sdk;

import datameshmanager.sdk.client.ApiException;
import datameshmanager.sdk.client.model.IntegrationAgent;
import datameshmanager.sdk.client.model.IntegrationAgentInfo;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataMeshManagerAgentRegistration {

  private static final Logger log = LoggerFactory.getLogger(DataMeshManagerAgentRegistration.class);

  private final DataMeshManagerClient client;

  /**
   * The unique identifier of the agent.
   * This is used to identify the agent in the Data Mesh Manager and to exchange state.
   * The identifier should be constant across restarts of the agent.
   */
  private final String id;

  /**
   * The type of the agent (e.g. databricks-assets, databricks-access, aws-costs, ...).
   */
  private final String type;

  public DataMeshManagerAgentRegistration(DataMeshManagerClient client, String agentId, String type) {
    this.client = client;
    this.id = Objects.requireNonNull(agentId, "Agent ID is required");
    this.type = Objects.requireNonNull(type, "Agent type is required");
  }

  public void register() {

    IntegrationAgent integrationAgent;
    try {
      log.debug("Checking if integration agent {} already exists", id);
      integrationAgent = client.getIntegrationsApi().getIntegrationAgent(id);
    } catch (ApiException e) {
      if (e.getCode() == 404) {
        integrationAgent = new IntegrationAgent();
      } else {
        throw e;
      }
    }

    integrationAgent
        .id(id)
        .info(new IntegrationAgentInfo()
            .type(type)
        );

    log.info("Registering integration agent {}", id);
    client.getIntegrationsApi().putIntegrationAgent(id, integrationAgent);
  }

  public void delete() {
    log.info("Deleting integration agent {}", this.id);
    try {
      client.getIntegrationsApi().deleteIntegrationAgent(this.id);
    } catch (ApiException e) {
      if (e.getCode() == 404) {
        log.error("Integration agent with id {} already deleted", this.id);
      } else {
        throw e;
      }
    }
  }

}
